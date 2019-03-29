package org.keymobile.test;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.jdbc.postgresql.PostgreSQLExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class PsqlServerTest {

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
    public void testDeploy() throws Throwable {
        EmbeddedConfiguration ec = new EmbeddedConfiguration();
        ec.setUseDisk(false);

//        ec.setTransactionManager(sqlEngineHelper.getTransactionManager());
//        ec.setSecurityHelper(new SQLEngineSecurityHelper());
//
//        DefaultAuthorizationValidator authorizationValidator = new DefaultAuthorizationValidator();
//        authorizationValidator.setPolicyDecider(new DPPolicyDecider());
//        ec.setAuthorizationValidator(authorizationValidator);

//        if (System.getProperty("processorBatchSize") != null) {
//            try {
//                int processorBatchSize = Integer.parseInt(System.getProperty("processorBatchSize"));
//                ec.setProcessorBatchSize(processorBatchSize);
//                System.out.println("setProcessorBatchSize = " + processorBatchSize);
//            } catch (NumberFormatException ignore) {}
//        }


        SocketConfiguration scTeiid = new SocketConfiguration();
        scTeiid.setProtocol(WireProtocol.teiid);
        scTeiid.setBindAddress("0.0.0.0");
        scTeiid.setPortNumber(54321);
        scTeiid.setMaxSocketThreads(2);
        ec.addTransport(scTeiid);

        SocketConfiguration scODBC = new SocketConfiguration();
        scODBC.setProtocol(WireProtocol.pg);
        scODBC.setBindAddress("0.0.0.0");
        scODBC.setPortNumber(54322);
        scODBC.setMaxSocketThreads(2);
        ec.addTransport(scODBC);

        ec.setMaxActivePlans(1<<4);
        ec.setMaxAsyncThreads(1<<4);
        ec.setMaxRowsFetchSize(1<<12);

        es.start(ec);

        PostgreSQLExecutionFactory ef = new PostgreSQLExecutionFactory();
        ef.setSupportsDirectQueryProcedure(true);
        ef.start();
        es.addTranslator("translator-postgresql", ef);



//        ComboPooledDataSource cpds = new ComboPooledDataSource();
//        cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver
//        cpds.setJdbcUrl( "jdbc:postgresql://localhost:5432/blockchain_test" );
//        cpds.setUser("postgres");
//        cpds.setPassword("mysecretpassword");
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("postgresql");
        ds.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        Properties p = new Properties();
        p.setProperty ( "user" , "postgres" );
        p.setProperty ( "password" , "mysecretpassword" );
        p.setProperty ( "URL" , "jdbc:postgresql://localhost:5432/blockchain_test" );
//        ds.setXaDataSourceProperties ( p );
        ds.setXaProperties(p);
        ds.setPoolSize ( 5 );


        //change db and reset connection pool
//        cpds.setJdbcUrl();
        //for multiple users
//        cpds.getConnection(user,password);

        es.addConnectionFactory("teiid1", ds);


        ModelMetaData model = new ModelMetaData();
        model.setModelType("PHYSICAL");
        model.setName("psql-schema");
        model.addSourceMapping("psql-1", "translator-postgresql", "teiid1");


        es.deployVDB("psql-1", model);


//        TeiidDriver td = es.getDriver();
//        Connection c = td.connect("jdbc:teiid:psql-1@mm://localhost:54321", null);
//        Statement s = c.createStatement();

        ComboPooledDataSource c = new ComboPooledDataSource();
        c.setDriverClass( "org.teiid.jdbc.TeiidDriver" ); //loads the jdbc driver
        c.setJdbcUrl( "jdbc:teiid:psql-1@mm://localhost:54321" );
        Statement s = c.getConnection().createStatement();

        ResultSet rs = s.executeQuery("select * from \"bc_user\"");
        while (rs.next()) {
            String username = rs.getString("username");
            System.out.println(username);
        }
    }


}
