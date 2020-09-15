package com.github.graywizard123.tgbot.db.repositories;

import com.github.graywizard123.tgbot.App;
import com.github.graywizard123.tgbot.db.DataBaseManager;
import com.github.graywizard123.tgbot.db.models.Category;
import com.github.graywizard123.tgbot.db.models.Meal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MealRepository {

    public static Meal getById(long id){
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM meals WHERE id="+ id);

            if (response.next()) {
                String name = response.getString("name");
                String description = response.getString("description");
                int price = response.getInt("price");
                long categoryId = response.getLong("category_id");

                return new Meal(id, name, description, price, categoryId);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            App.LOGGER.error("Cannot get meal with " + id + " id");
            throwables.printStackTrace();
            return null;
        }
    }

    public static Meal getByName(String name){
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM meals WHERE name=\""+name+"\"");

            if (response.next()) {
                long id = response.getLong("id");
                String description = response.getString("description");
                int price = response.getInt("price");
                long categoryId = response.getLong("category_id");

                return new Meal(id, name, description, price, categoryId);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static List<Meal> getAll(){
        try {
            List<Meal> list = new ArrayList<>();
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM meals");

            while (response.next()) {
                int id = response.getInt("id");
                String name = response.getString("name");
                String description = response.getString("description");
                int price = response.getInt("price");
                long categoryId = response.getLong("category_id");

                list.add(new Meal(id, name, description, price, categoryId));
            }

            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static void add(Meal meal){
        try {
            if (meal.getId() == 0) {
                DataBaseManager.executeUpdate(String.format("INSERT INTO meals(name, description, price, category_id) VALUES(\"%s\", \"%s\", %d, %d)",
                        meal.getName(),
                        meal.getDescription(),
                        meal.getPrice(),
                        meal.getCategoryId()
                ));
            } else {
                DataBaseManager.executeUpdate(String.format("INSERT INTO meals(id, name, description, price, category_id) VALUES(%d, \"%s\", \"%s\", %d, %d)",
                        meal.getId(),
                        meal.getName(),
                        meal.getDescription(),
                        meal.getPrice(),
                        meal.getCategoryId()
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void remove(long id) {
        try {
            DataBaseManager.executeUpdate(String.format("DELETE FROM %s WHERE id=%d", Meal.getTableName(), id));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void remove(String name) {
        try {
            DataBaseManager.executeUpdate(String.format("DELETE FROM %s WHERE name=\"%s\"", Meal.getTableName(), name));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void update(Meal meal){
        try {
            DataBaseManager.executeUpdate(String.format("UPDATE users SET name=\"%s\", description=\"%s\", price=%d WHERE id=%d",
                    meal.getName(),
                    meal.getDescription(),
                    meal.getPrice(),
                    meal.getId()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
