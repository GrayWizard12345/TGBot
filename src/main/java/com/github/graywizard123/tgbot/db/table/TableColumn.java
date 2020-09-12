package com.github.graywizard123.tgbot.db.table;

public class TableColumn {

    private final String name;
    private final TableColumnType type;
    private final String[] args;

    public TableColumn(String name, TableColumnType type, String... args) {
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
