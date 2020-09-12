package com.github.graywizard123.tgbot.db.table;

import com.github.graywizard123.tgbot.db.models.IModel;
import org.jetbrains.annotations.NotNull;

public class TableUtils {

    public static String fromTableColumnToSQL(@NotNull TableColumn column){
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(column.getName());
        resultBuilder.append(' ');
        resultBuilder.append(column.getType().name());
        resultBuilder.append(' ');
        for (String arg:
             column.getArgs()) {
            resultBuilder.append(arg);
            resultBuilder.append(' ');
        }
        return resultBuilder.toString();
    }

    public static String fromModelToSQL(@NotNull IModel model){
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("(\n");
        TableColumn[] columns = model.getTablesColumns();
        for (int i = 0; i < columns.length; i++) {
            TableColumn column = columns[i];
            resultBuilder.append(fromTableColumnToSQL(column));
            if (i != columns.length - 1){
                resultBuilder.append(",");
            }
            resultBuilder.append("\n");
        }
        resultBuilder.append(')');
        return resultBuilder.toString();
    }

}
