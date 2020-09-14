package com.github.graywizard123.tgbot.db.models;

import com.github.graywizard123.tgbot.db.table.TableColumn;
import com.github.graywizard123.tgbot.db.table.TableColumnType;

import java.util.List;

public class Category {

    private final long id;
    private String title;
    private List<Meal> meals;

    public Category(long id, String title, List<Meal> meals) {
        this.id = id;
        this.title = title;
        this.meals = meals;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public static String getTableName(){
        return "categories";
    }

    public static TableColumn[] getTableColumns(){
        return new TableColumn[]{
                new TableColumn("id", TableColumnType.INTEGER, "NOT NULL", "UNIQUE", "AUTOINCREMENT", "PRIMARY KEY"),
                new TableColumn("title", TableColumnType.TEXT, "NOT NULL", "UNIQUE"),
                new TableColumn("meals", TableColumnType.TEXT, "NOT NULL")
        };
    }
}
