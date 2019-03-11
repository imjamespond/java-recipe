package org.keymobile.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.teiid.adminapi.Model;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.language.Command;
import org.teiid.language.QueryExpression;
import org.teiid.metadata.Column;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.metadata.Table;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class ServerTest {

    EmbeddedServer es;

    @Before
    public void setup() {
        es = new EmbeddedServer();
    }

    @After
    public void teardown() {
        if (es != null) {
            es.stop();
        }
    }

    @Test
    public void testDeploy() throws Exception {
        EmbeddedConfiguration ec = new EmbeddedConfiguration();
        ec.setUseDisk(false);
        es.start(ec);

        es.addTranslator("y", new FakeTranslator(false));
        final AtomicInteger counter = new AtomicInteger();
        EmbeddedServer.ConnectionFactoryProvider<AtomicInteger> cfp = new EmbeddedServer.SimpleConnectionFactoryProvider<AtomicInteger>(counter);

        es.addConnectionFactoryProvider("z", cfp);

        ModelMetaData mmd = new ModelMetaData();
        mmd.setName("my-schema");
        mmd.addSourceMapping("x", "y", "z");

        ModelMetaData mmd1 = new ModelMetaData();
        mmd1.setName("virt");
        mmd1.setModelType(Model.Type.VIRTUAL);
        mmd1.setSchemaSourceType("ddl");
        mmd1.setSchemaText("create view \"my-view\" OPTIONS (UPDATABLE 'true') as select * from \"my-table\"");

        es.deployVDB("test", mmd, mmd1);

        TeiidDriver td = es.getDriver();
        Connection c = td.connect("jdbc:teiid:test", null);
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("select * from \"my-view\"");
        assertFalse(rs.next());
        assertEquals("my-column", rs.getMetaData().getColumnLabel(1));

        s.execute("update \"my-view\" set \"my-column\" = 'a'");
        assertEquals(2, s.getUpdateCount());

        es.deployVDB("empty");
        c = es.getDriver().connect("jdbc:teiid:empty", null);
        s = c.createStatement();
        s.execute("select * from sys.tables");

        assertNotNull(es.getSchemaDdl("empty", "SYS"));
        assertNull(es.getSchemaDdl("empty", "xxx"));
    }


    @Translator(name="y")
    public static class FakeTranslator extends
            ExecutionFactory<AtomicInteger, Object> {

        private boolean batch;

        public FakeTranslator() {
            this.batch = false;
        }

        public FakeTranslator(boolean batch) {
            this.batch = batch;
        }

        @Override
        public Object getConnection(AtomicInteger factory)
                throws TranslatorException {
            return factory.incrementAndGet();
        }

        @Override
        public void closeConnection(Object connection, AtomicInteger factory) {

        }

        @Override
        public boolean supportsBulkUpdate() {
            return true;
        }

        @Override
        public void getMetadata(MetadataFactory metadataFactory, Object conn)
                throws TranslatorException {
            assertEquals(conn, Integer.valueOf(1));
            Table t = metadataFactory.addTable("my-table");
            t.setSupportsUpdate(true);
            Column c = metadataFactory.addColumn("my-column", TypeFacility.RUNTIME_NAMES.STRING, t);
            c.setUpdatable(true);
        }

        @Override
        public ResultSetExecution createResultSetExecution(
                QueryExpression command, ExecutionContext executionContext,
                RuntimeMetadata metadata, Object connection)
                throws TranslatorException {
            ResultSetExecution rse = new ResultSetExecution() {

                @Override
                public void execute() throws TranslatorException {

                }

                @Override
                public void close() {

                }

                @Override
                public void cancel() throws TranslatorException {

                }

                @Override
                public List<?> next() throws TranslatorException, DataNotAvailableException {
                    return null;
                }
            };
            return rse;
        }

        @Override
        public UpdateExecution createUpdateExecution(Command command,
                                                     ExecutionContext executionContext,
                                                     RuntimeMetadata metadata, Object connection)
                throws TranslatorException {
            UpdateExecution ue = new UpdateExecution() {

                @Override
                public void execute() throws TranslatorException {

                }

                @Override
                public void close() {

                }

                @Override
                public void cancel() throws TranslatorException {

                }

                @Override
                public int[] getUpdateCounts() throws DataNotAvailableException,
                        TranslatorException {
                    if (!batch) {
                        return new int[] {2};
                    }
                    return new int[] {1, 1, -1, 1, 1, 1, -1, 1, 1, 1, -1, 1, 1, 1, -1, 1};
                }
            };
            return ue;
        }

        @Override
        public boolean isSourceRequiredForMetadata() {
            return false;
        }
    }
}
