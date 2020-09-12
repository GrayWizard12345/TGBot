package com.github.graywizard123.tgbot.db.repositories;

import com.github.graywizard123.tgbot.App;
import com.github.graywizard123.tgbot.db.DataBaseManager;
import com.github.graywizard123.tgbot.db.models.Meal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MealRepository {

    public static Meal getById(int id){
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM meals WHERE id="+ id);

            if (response.first()) {
                String name = response.getNString("name");
                String description = response.getNString("description");
                int price = response.getInt("price");

                return new Meal(id, name, description, price);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            App.LOGGER.error("Cannot get meal with " + id + " id");
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
                String name = response.getNString("name");
                String description = response.getNString("description");
                int price = response.getInt("price");

                list.add(new Meal(id, name, description, price));
            }

            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static void add(Meal meal){
        try {
            DataBaseManager.executeUpdate(String.format("INSERT INTO meals(id, name, description, price) VALUES(%d, \"%s\", \"%s\", %d)",
                    meal.getId() == 0 ? null : meal.getId(),
                    meal.getName(),
                    meal.getDescription(),
                    meal.getPrice()));
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
