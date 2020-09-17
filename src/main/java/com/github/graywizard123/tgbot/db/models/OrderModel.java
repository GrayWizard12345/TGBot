package com.github.graywizard123.tgbot.db.models;

import com.github.graywizard123.tgbot.db.table.TableColumn;
import com.github.graywizard123.tgbot.db.table.TableColumnType;

import java.util.HashMap;

public class OrderModel {

    private final long id;
    private final UserModel from;
    private final String address;
    private final String phone;
    private final HashMap<MealModel, Integer> meals;

    public OrderModel(long id, UserModel from, String address, String phone, HashMap<MealModel, Integer> meals) {
        this.id = id;
        this.from = from;
        this.address = address;
        this.phone = phone;
        this.meals = meals;
    }

    public long getId() {
        return id;
    }

    public UserModel getFrom() {
        return from;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public HashMap<MealModel, Integer> getMeals() {
        return meals;
    }

    public static String getTableName(){
        return "orders";
    }

    public static TableColumn[] getTableColumns(){
        return new TableColumn[]{
                new TableColumn("id", TableColumnType.SERIAL, "NOT NULL", "UNIQUE"),
                new TableColumn("user_id", TableColumnType.INTEGER, "NOT NULL"),
                new TableColumn("order_addres", TableColumnType.TEXT, "NOT NULL"),
                new TableColumn("phone", TableColumnType.TEXT, "NOT NULL"),
                new TableColumn("meals_data", TableColumnType.TEXT, "NOT NULL")
        };
    }

}
