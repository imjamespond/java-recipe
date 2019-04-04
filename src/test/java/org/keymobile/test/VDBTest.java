package org.keymobile.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.metadata.MetadataRepository;
import org.teiid.query.metadata.ChainingMetadataRepository;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.security.Credentials;
import org.teiid.security.GSSResult;
import org.teiid.security.SecurityHelper;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.hsql.HsqlExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class VDBTest {
    EmbeddedServer es;

    @Before
    public void setup() throws TranslatorException {
        es = new EmbeddedServer();

        HsqlExecutionFactory ef = new HsqlExecutionFactory();
        ef.setSupportsDirectQueryProcedure(true);
        ef.start();
        es.addTranslator("translator-hsql", ef);

//        addMetadataRepository("chain" , new ChainingMetadataRepository(Arrays.asList(new MetadataRepository[] {
//                this.getMetadataRepository("native"), this.getMetadataRepository("resource-metadata")
//        })));
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
        ResultSet rs = c.createStatement().executeQuery("select * from \"contacts\"");
        while (rs.next()) {
            String username = rs.getString("name");
            System.out.println(username);
        }

    }

    private void deploy() throws Throwable {

        List<ModelMetaData> models = new ArrayList<ModelMetaData>();

        MyDataSource ds = new MyDataSource("jdbc:hsqldb:mydatabase", "SA","");
        ModelMetaData model = createVDBModel( "test-hsql", "hsql-1-1", "translator-hsql", ds.getJndiName(), ds);
        models.add(model);

        es.deployVDB("test-vdb", models.toArray(new ModelMetaData[0]));
    }

    private ModelMetaData createVDBModel(String dbName, String dbId, String dbTrans, String connJndiName, DataSource ds)
            throws Throwable {
        es.addConnectionFactory(connJndiName, ds);

        ModelMetaData model = new ModelMetaData();
        model.setModelType("PHYSICAL");
        model.setName(dbName);
//        model.addSourceMetadata("chain", "chain"); // refer to addMetadataRepository("chain"...)
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
}
