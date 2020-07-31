import org.junit.Test;
import org.teiid.jdbc.TeiidDriver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestCli {

    @Test
    public void test() throws SQLException {

        /*
        * 测试
        * */
        {
            TeiidDriver td = new TeiidDriver();
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
            TeiidDriver td = new TeiidDriver();
            Connection c = td.connect("jdbc:teiid:my-vdb@mm://localhost:33060", null);
            Statement s = c.createStatement();

            ResultSet rs = s.executeQuery("select * from \"virt\".\"my-view\" ");
            while (rs.next()) {
                long id = rs.getLong(1);
                String name = rs.getString(2);
                String random = rs.getString(3);
                System.out.printf("id: %d, name: %s, random: %s \n", id, name, random);
            }
        }
    }

}
