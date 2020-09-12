package com.github.graywizard123.tgbot.db.models;

import com.github.graywizard123.tgbot.db.table.TableColumn;
import com.github.graywizard123.tgbot.db.table.TableColumnType;

import java.util.HashMap;

public class Order {

    private final long id;
    private final User from;
    private final String address;
    private final String phone;
    private final HashMap<Meal, Integer> meals;

    public Order(long id, User from, String address, String phone, HashMap<Meal, Integer> meals) {
        this.id = id;
        this.from = from;
        this.address = address;
        this.phone = phone;
        this.meals = meals;
    }

    public long getId() {
        return id;
    }

    public User getFrom() {
        return from;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public HashMap<Meal, Integer> getMeals() {
        return meals;
    }

    public static String getTableName(){
        return "orders";
    }

    public static TableColumn[] getTableColumns(){
        return new TableColumn[]{
                new TableColumn("id", TableColumnType.INTEGER, "NOT NULL", "UNIQUE", "AUTOINCREMENT", "PRIMARY KEY"),
                new TableColumn("user_id", TableColumnType.INTEGER, "NOT NULL"),
                new TableColumn("address", TableColumnType.TEXT, "NOT NULL"),
                new TableColumn("phone", TableColumnType.TEXT, "NOT NULL"),
                new TableColumn("meals_data", TableColumnType.TEXT, "NOT NULL")
        };
    }

}
