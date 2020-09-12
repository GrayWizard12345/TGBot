package com.github.graywizard123.tgbot.db.models;

import com.github.graywizard123.tgbot.db.table.TableColumn;

public interface IModel {

    String getTableName();

    TableColumn[] getTablesColumns();

}
