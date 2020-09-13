package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teiid.query.sql.lang.FromClause;
import org.teiid.query.sql.lang.Select;

import java.util.List;

public class SqlUtil {
    static final Logger log = LoggerFactory.getLogger(SqlUtil.class);
    static public void Select(Select select){
        log.debug(select.toString());
    }

    static public void From(List<FromClause> clauseList){
        for(FromClause clause: clauseList){
            log.debug(clause.toString());
        }
    }
}
