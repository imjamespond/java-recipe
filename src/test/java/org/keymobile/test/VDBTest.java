package org.keymobile.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.core.util.StringUtil;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.metadata.*;
import org.teiid.query.metadata.ChainingMetadataRepository;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.security.Credentials;
import org.teiid.security.GSSResult;
import org.teiid.security.SecurityHelper;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.JDBCMetadataProcessor;
import org.teiid.translator.jdbc.hsql.HsqlExecutionFactory;
import org.teiid.translator.jdbc.postgresql.PostgreSQLExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;
import org.teiid.util.FullyQualifiedName;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class VDBTest {
    EmbeddedServer es;

    @Before
    public void setup() throws TranslatorException {
        es = new EmbeddedServer();

        HsqlExecutionFactory hsqlEF = new HsqlExecutionFactory();
        hsqlEF.setSupportsDirectQueryProcedure(true);
        hsqlEF.start();
        es.addTranslator("translator-hsql", hsqlEF);


        PostgreSQLExecutionFactory psqlEF = new PostgreSQLExecutionFactory();
        psqlEF.setSupportsDirectQueryProcedure(true);
        psqlEF.start();
        es.addTranslator("translator-postgresql", psqlEF);

        es.addMetadataRepository("chain" , new ChainingMetadataRepository(Arrays.asList(new MetadataRepository[] {
                new MyMetadataRepo()
        })));


    }

    @After
    public void teardown() {
        if (es != null) {
            es.stop();

        }
    }

    @Test
    public void test() throws Throwable {
        EmbeddedConfiguration ec = new EmbeddedConfiguration();

        ec.setUseDisk(false);

        SocketConfiguration scTeiid = new SocketConfiguration();
        scTeiid.setProtocol(WireProtocol.teiid);
        scTeiid.setBindAddress("0.0.0.0");
        scTeiid.setPortNumber(54321);
        scTeiid.setMaxSocketThreads(2);
        ec.addTransport(scTeiid);


//        ec.setTransactionManager(sqlEngineHelper.getTransactionManager());
        ec.setSecurityHelper(new MySecurityHelper());

        /**
         * A policy decider that reports authorization decisions for further action.
         * 策略决定器 会 报告 对将来行为授权决定
         * A decider may be called many times for a single user command.  Typically there will be 1 call for every
         * 一个决定器 可以被一个用户的命令 调用 多次, 特别对每一个 临表访问函数 都有一个调用?
         * command/subquery/temp table access/function call.
         */
//        DefaultAuthorizationValidator authorizationValidator = new DefaultAuthorizationValidator();
//        authorizationValidator.setPolicyDecider(new DPPolicyDecider());
//        ec.setAuthorizationValidator(authorizationValidator);

//        ec.setProcessorBatchSize(processorBatchSize);

        es.start(ec);


        deploy();

        TeiidDriver td = es.getDriver();
        Connection c = td.connect("jdbc:teiid:test-vdb@mm://localhost:54321", null);
        ResultSet rs;
//        rs = c.createStatement().executeQuery("select * from \"contacts\"");
//        while (rs.next()) {
//            System.out.printf("%s, %s\n", rs.getString("username"), rs.getString("email"));
//        }

        rs = c.createStatement().executeQuery("select username from \"bc_user\" UNION select username from \"contacts\" order by username asc");
        while (rs.next()) {
            String username = rs.getString("username");
            System.out.println(username);
        }

    }

    private void deploy() throws Throwable {

        List<ModelMetaData> models = new ArrayList<ModelMetaData>();

        MyDataSource ds;
        ds = new MyDataSource("jdbc:hsqldb:mydatabase", "SA","");
        ModelMetaData model;
        model = createVDBModel( "test-hsql", "hsql-1-1", "translator-hsql", ds.getJndiName(), ds);
        models.add(model);

        ds = new MyDataSource("jdbc:postgresql://localhost:5432/blockchain_test", "postgres","mysecretpassword");
        model = createVDBModel( "test-psql", "psql-1-1", "translator-postgresql", ds.getJndiName(), ds);
        models.add(model);

        es.deployVDB("test-vdb", models.toArray(new ModelMetaData[0]));
    }

    private ModelMetaData createVDBModel(String dbName, String dbId, String dbTrans, String connJndiName, DataSource ds)
            throws Throwable {
        es.addConnectionFactory(connJndiName, ds);

        ModelMetaData model = new ModelMetaData();
        model.setModelType("PHYSICAL");
        model.setName(dbName);
        model.addSourceMetadata("chain", "chain"); // replace default NativeRepo
        model.addSourceMapping(dbId, dbTrans, connJndiName);

        return model;
    }

    class MySecurityHelper implements SecurityHelper {

        @Override
        public Object associateSecurityContext(Object o) {
            return null;
        }

        @Override
        public void clearSecurityContext() {

        }

        @Override
        public Object getSecurityContext() {
            return null;
        }

        @Override
        public Subject getSubjectInContext(String s) {
            return null;
        }

        @Override
        public Subject getSubjectInContext(Object o) {
            return null;
        }

        @Override
        public Object authenticate(String s, String s1, Credentials credentials, String s2) throws LoginException {
            return null;
        }

        @Override
        public GSSResult negotiateGssLogin(String s, byte[] bytes) throws LoginException {
            return null;
        }
    }


    class MyDataSource implements DataSource {

        String url;
        String user;
        String password;

        public MyDataSource(String url, String user, String password) {
            this.url = url;
            this.user = user;
            this.password = password;
        }

        String getJndiName() {
            return url + ":" +user;
        }

        @Override
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(url, user, password);
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return getConnection();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }
    }

    class MyMetadataRepo implements MetadataRepository {
        public void loadMetadata(MetadataFactory metadataFactory, ExecutionFactory executionFactory, Object connectionFactory) {
            List<String> sourceNames = metadataFactory.getModel().getSourceNames();


            JDBCMetadataProcessor processor = new JDBCMetadataProcessor();
            MyDataSource ds = (MyDataSource)connectionFactory;

            if (ds.url.startsWith("jdbc:hsqldb:mydatabase")) {
                String tableCatalog = "public", tableSchema = "public", tableName = "contacts";
                String fullName = processor.getFullyQualifiedName(tableCatalog, tableSchema, tableName, false);
                Table table = processor.addTable(metadataFactory, tableCatalog, tableSchema, tableName, null, fullName);

                Column column;
                column = metadataFactory.addColumn("USERNAME", "string", table);
                column = metadataFactory.addColumn("EMAIL", "string", table);
            }

            if (ds.url.startsWith("jdbc:postgresql://localhost")) {
                String tableCatalog = "", tableSchema = "public", tableName = "bc_user";
                String fullName = processor.getFullyQualifiedName(tableCatalog, tableSchema, tableName, false);
                Table table = processor.addTable(metadataFactory, tableCatalog, tableSchema, tableName, null, fullName);

                Column column;
                column = metadataFactory.addColumn("USERNAME", "string", table);
            }
        }

    }
}
