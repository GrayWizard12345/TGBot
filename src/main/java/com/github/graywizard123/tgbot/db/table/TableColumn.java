package com.github.graywizard123.tgbot.db.table;

public class TableColumn {

    private String name;
    private TableColumnType type;
    private String[] args;

    public TableColumn(String name, TableColumnType type, String[] args) {
        this.name = name;
        this.type = type;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public TableColumnType getType() {
        return type;
    }

    public String[] getArgs() {
        return args;
    }
}
