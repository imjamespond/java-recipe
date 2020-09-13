package org.teiid.metadata;

import com.test.Utils;
import org.teiid.connector.DataPlugin;

import java.util.NavigableMap;

public class SchemaTest extends Schema {

    @Override
    public void addTable(Table table) {
        table.setParent(this);
        if (this.tables.put(table.getName(), table) != null) {
            throw new DuplicateRecordException(DataPlugin.Event.TEIID60013, DataPlugin.Util.gs(DataPlugin.Event.TEIID60013, table.getName()));
        }
        this.resolvingOrder.add(table);

        System.out.printf("SchemaTest::addTable: %s, size: %d\n", table.getFullName(), tables.size());
    }
    @Override
    public NavigableMap<String, Table> getTables() {
        Utils.PrintCaller((file,line,method,thread)->{
            System.out.printf("SchemaTest::getTables, size: %d, caller: %s-%s(%d).%s\n", tables.size(), thread,file,line,method);
        });


        return tables;
    }
    @Override
    public Table getTable(String tableName) {
        Utils.PrintCaller((file,line,method,thread)->{
            System.out.printf("SchemaTest::getTable: %s, caller: %s-%s(%d).%s\n", tableName, thread,file,line,method);
        });

        return tables.get(tableName);
    }

}
