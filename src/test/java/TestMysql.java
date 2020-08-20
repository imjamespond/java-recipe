import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.teiid.adminapi.Model;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.metadata.MetadataFactory;
import org.teiid.query.metadata.NativeMetadataRepository;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.MetadataProcessor;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.JDBCMetadataProcessor;
import org.teiid.translator.jdbc.mysql.MySQLExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestMysql {

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
    public void test() throws TranslatorException, SQLException, ConnectorManagerRepository.ConnectorManagerException, VirtualDatabaseException, InterruptedException {

        EmbeddedConfiguration ec = new EmbeddedConfiguration();
        //set any configuration properties
        ec.setUseDisk(false);

        SocketConfiguration scTeiid = new SocketConfiguration();
        scTeiid.setProtocol(WireProtocol.teiid);
        scTeiid.setBindAddress("0.0.0.0");
        scTeiid.setPortNumber(33060);
        scTeiid.setMaxSocketThreads(2);
        ec.addTransport(scTeiid);

        SocketConfiguration scODBC = new SocketConfiguration();
        scODBC.setProtocol(WireProtocol.pg);
        scODBC.setBindAddress("0.0.0.0");
        scODBC.setPortNumber(33061);
        scODBC.setMaxSocketThreads(2);
        ec.addTransport(scODBC);

        ec.setMaxActivePlans(1 << 4);
        ec.setMaxAsyncThreads(1 << 4);
        ec.setMaxRowsFetchSize(1 << 12);

        es.start(ec);

        //example of adding a translator by pre-initialized ExecutionFactory and given translator name
        MySQLExecutionFactory ef = new MyMySQLExecutionFactory();
        ef.setSupportsDirectQueryProcedure(true);
        ef.start();
        es.addTranslator("translator-mysql", ef);

        //add a Connection Factory with a third-party connection pool
        HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(2);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://192.168.0.247:32495?autoReconnect=true&useSSL=false");
        ds.setUsername("root");
        ds.setPassword("dataSharing");
        es.addConnectionFactory("cf-1", ds);

        //add a vdb

        //physical model
        ModelMetaData mmd = new ModelMetaData();
        mmd.setModelType(Model.Type.PHYSICAL);
        mmd.setName("my-schema");
        /*
        Properties mmdProps = new Properties();
        mmdProps.setProperty("multisource.addColumn", "true");
        mmdProps.setProperty("multisource.columnName", "columnName");
        mmd.setProperties(mmdProps);
        mmd.setSupportsMultiSourceBindings(true);
        */
        mmd.addSourceMapping("my-schema", "translator-mysql", "cf-1");

        //virtual model
        ModelMetaData mmd1 = new ModelMetaData();
        mmd1.setName("virt");
        mmd1.setModelType(Model.Type.VIRTUAL);
        mmd1.addSourceMetadata("ddl", "create view \"my-view\" OPTIONS (UPDATABLE 'true') as " +
                "select fb.id, fb.name, random(fb.name) from \"my-schema\".\"testdb\".\"foobar\" fb");//data-quality::OSDQFunctions::random

        es.deployVDB("my-vdb", mmd,mmd1);


        /*
        * 测试
        *
        {
            TeiidDriver td = es.getDriver();
            Connection c = td.connect("jdbc:teiid:my-vdb@mm://localhost:33060", null);
            Statement s = c.createStatement();

            ResultSet rs = s.executeQuery("select * from \"my-schema\".\"testdb\".\"foobar\"");
            while (rs.next()) {
                long id = rs.getLong(1);
                String name = rs.getString(2);
                System.out.printf("id: %d, name: %s\n", id, name);
            }
        }

        {
            TeiidDriver td = es.getDriver();
            Connection c = td.connect("jdbc:teiid:my-vdb@mm://localhost:33060", null);
            Statement s = c.createStatement();

            ResultSet rs = s.executeQuery("select * from \"virt\".\"my-view\" ");
            while (rs.next()) {
                long id = rs.getLong(1);
                String name = rs.getString(2);
                String random = rs.getString(3);
                System.out.printf("id: %d, name: %s, random: %s \n", id, name, random);
            }
        }*/

        Thread.currentThread().join();
    }

    class MyMySQLExecutionFactory extends MySQLExecutionFactory{
        @Override
        public MetadataProcessor<Connection> getMetadataProcessor() {
            JDBCMetadataProcessor processor = (JDBCMetadataProcessor) super.getMetadataProcessor();
            processor.setUseFullSchemaName(true);//避免多个系统表存在相同的表
            return processor;
        }
    }

    class MyNativeMetadataRepository extends NativeMetadataRepository {
        @Override
        public void loadMetadata(MetadataFactory factory, ExecutionFactory executionFactory, Object connectionFactory) throws TranslatorException {
            System.out.println("MyNativeMetadataRepository::loadMetadata");
            super.loadMetadata(factory, executionFactory, connectionFactory);
        }
    }
}
