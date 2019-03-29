package org.keymobile.test;


import com.atomikos.icatch.jta.UserTransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.jdbc.ConnectionImpl;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.jdbc.hsql.HsqlExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class HsqlServerTest {

    EmbeddedServer es;


    @Before
    public void setup() {
        ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
        Iterator<Driver> driversIterator = loadedDrivers.iterator();
        while(driversIterator.hasNext()) {
            Driver d = driversIterator.next();
            System.out.println(d.getClass());
        }


        es = new EmbeddedServer();
    }

    @After
    public void teardown() {
        if (es != null) {
            es.stop();
        }
    }

    public URLClassLoader loadLibs(String path) throws MalformedURLException {
        URL fileUrl = getClass().getResource(path);
        File dir = new File(fileUrl.getFile());
        List<URL> urlList = new ArrayList<>();
        if (dir.isDirectory()){
            for (final File fileEntry : dir.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    System.out.println(fileEntry.getName());
                    urlList.add(fileEntry.toURI().toURL());
                }
            }
        }
        URL[] urls = urlList.toArray(new URL[0]);
        URLClassLoader loader = new URLClassLoader(
                urls,
                this.getClass().getClassLoader()
        );

        return loader;
    }

    @Test
    public void testClassLoader() throws Throwable {
        URLClassLoader loader1 = this.loadLibs("/lib1");
        Class CCC1 = Class.forName("test.c.CCC", true, loader1);
        CCC1.newInstance();

        URLClassLoader loader2 = this.loadLibs("/lib2");
        Class CCC2 = Class.forName("test.c.CCC", true, loader2);
        CCC2.newInstance();

        CCC1.newInstance();
        CCC2.newInstance();


//        URLClassLoader child = new URLClassLoader(
//                new URL[] {this.getClass().getClassLoader().getResource("postgresql-42.2.5.jar")},
//                this.getClass().getClassLoader()
//        );
//        Class classToLoad = Class.forName("org.postgresql.Driver", true, child);
//        Driver dr = (Driver) classToLoad.newInstance();
//        System.out.println(dr.getClass().getName());
//
//        java.util.Properties info = new java.util.Properties();
//        info.put("user", "postgres");
//        info.put("password", "mysecretpassword");
//
//        Connection conn = dr.connect("jdbc:postgresql://dell:5432/blockchain_test", info);
//        Statement s = conn.createStatement();
//        ResultSet rs = s.executeQuery("select * from \"bc_user\"");
//
//        while (rs.next()) {
//            String username = rs.getString("username");
//            System.out.println(username);
//        }
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

        TransactionManager tm = new UserTransactionManager();
        ec.setTransactionManager(tm);

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
        c.setAutoCommit(false);
        PreparedStatement pst=c.prepareStatement("update contacts set phone=?");
        pst.clearParameters();
        pst.setString(1, "999");
        pst.executeUpdate();
        c.commit();

        ResultSet rs = c.createStatement().executeQuery("select * from \"contacts\"");
        while (rs.next()) {
            String username = rs.getString("name");
            System.out.println(username);
        }

    }

}
