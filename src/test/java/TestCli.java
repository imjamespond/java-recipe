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

            ResultSet rs = s.executeQuery("select * from \"my-schema2\".\"testdb2\".\"user\" u1 left join \"my-schema\".\"testdb\".\"user\" u2 on u1.username = u2.username where u2.username in ('user100','user1000', 'user9999') order by u1.username desc");
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                System.out.printf("id: %s, name: %s\n", id, name);
            }
        }

        /*{
            TeiidDriver td = new TeiidDriver();
            Connection c = td.connect("jdbc:teiid:my-vdb@mm://localhost:33060", null);
            Statement s = c.createStatement();

            ResultSet rs = s.executeQuery("select * from \"virt\".\"my-view\" ");
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                String random = rs.getString(3);
                System.out.printf("id: %s, name: %s, random: %s \n", id, name, random);
            }
        }*/
    }

}
