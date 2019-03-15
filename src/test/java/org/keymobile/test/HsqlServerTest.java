package org.keymobile.test;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.jdbc.hsql.HsqlExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;

public class HsqlServerTest {

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
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mydatabase","SA","");
        try {
            conn.createStatement().executeUpdate("create table contacts (name varchar(45),email varchar(45),phone varchar(45))");
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }


        EmbeddedConfiguration ec = new EmbeddedConfiguration();
        ec.setUseDisk(false);


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

        HsqlExecutionFactory ef = new HsqlExecutionFactory();
        ef.setSupportsDirectQueryProcedure(true);
        ef.start();
        es.addTranslator("translator-hsql", ef);

        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        es.addConnectionFactory("teiid1", new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection("jdbc:hsqldb:mydatabase","SA","");
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
        });


        ModelMetaData model = new ModelMetaData();
        model.setModelType("PHYSICAL");
        model.setName("test-hsql");
        model.addSourceMapping("hsql-1", "translator-hsql", "teiid1");


        es.deployVDB("hsql-1", model);


        TeiidDriver td = es.getDriver();
        Connection c = td.connect("jdbc:teiid:hsql-1@mm://localhost:54321", null);
//        PreparedStatement pst=c.prepareStatement("insert into contacts values(?,?,?)");
//        pst.clearParameters();
//        pst.setString(1, "foobar");
//        pst.setString(2, "hehe");
//        pst.setString(3, "123321");
//        pst.executeUpdate();
        ResultSet rs = c.createStatement().executeQuery("select * from \"contacts\"");
        while (rs.next()) {
            String username = rs.getString("name");
            System.out.println(username);
        }

    }


}
