package com.github.graywizard123.tgbot.db.models;

import com.github.graywizard123.tgbot.db.table.TableColumn;
import com.github.graywizard123.tgbot.db.table.TableColumnType;

public class MealModel {

    private final long id;
    private String name;
    private String description;
    private int price;
    private Long categoryId;

    public MealModel(long id, String name, String description, int price, Long categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public static TableColumn[] getTableColumns() {
        return new TableColumn[]{
                new TableColumn("id", TableColumnType.SERIAL, "UNIQUE", "NOT NULL"),
                new TableColumn("name",TableColumnType.TEXT, "NOT NULL", "UNIQUE"),
                new TableColumn("description", TableColumnType.TEXT),
                new TableColumn("price", TableColumnType.INTEGER, "NOT NULL"),
                new TableColumn("category_id", TableColumnType.INTEGER, "NOT NULL")
        };
    }
}
