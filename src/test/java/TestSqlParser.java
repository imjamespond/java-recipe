import org.junit.Test;
import org.teiid.api.exception.query.QueryParserException;
import org.teiid.common.buffer.TupleBatch;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.TeiidProcessingException;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.processor.ProcessorPlan;
import org.teiid.query.sql.lang.Command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestSqlParser {

    @Test
    public void test() throws TeiidProcessingException, TeiidComponentException {
        QueryParser qp = QueryParser.getQueryParser();
        Command cmd = qp.parseCommand("select * from " +
                "(select * from " +
                "(select username from users as uuu where uuu.username = \"foo\") " +
                "as uu) " +
                "as u");// "user" is preserved as a table name
        System.out.println(cmd.printCommandTree());
    }

}
