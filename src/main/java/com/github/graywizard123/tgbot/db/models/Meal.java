package com.github.graywizard123.tgbot.db.models;

import com.github.graywizard123.tgbot.db.table.TableColumn;
import com.github.graywizard123.tgbot.db.table.TableColumnType;

public class Meal {

    private final long id;
    private String name;
    private String description;
    private int price;

    public Meal(long id, String name, String description, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static String getTableName() {
        return "meals";
    }

    public static TableColumn[] getTableColumns() {
        return new TableColumn[]{
                new TableColumn("id", TableColumnType.INTEGER, "AUTOINCREMENT", "NOT NULL", "PRIMARY KEY"),
                new TableColumn("name",TableColumnType.TEXT, "NOT NULL", "UNIQUE"),
                new TableColumn("description", TableColumnType.TEXT, "NOT NULL"),
                new TableColumn("price", TableColumnType.INTEGER, "NOT NULL")
        };
    }
}
