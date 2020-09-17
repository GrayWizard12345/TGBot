package com.github.graywizard123.tgbot.db.repositories;

import com.github.graywizard123.tgbot.App;
import com.github.graywizard123.tgbot.db.DataBaseManager;
import com.github.graywizard123.tgbot.db.models.MealModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MealRepository {

    public static MealModel getById(long id){
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM meals WHERE id="+ id);

            if (response.next()) {
                String name = response.getString("name");
                String description = response.getString("description");
                int price = response.getInt("price");
                long categoryId = response.getLong("category_id");

                return new MealModel(id, name, description, price, categoryId);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            App.LOGGER.error("Cannot get meal with " + id + " id");
            throwables.printStackTrace();
            return null;
        }
    }

    public static MealModel getByName(String name){
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM meals WHERE name='"+name+"'");

            if (response.next()) {
                long id = response.getLong("id");
                String description = response.getString("description");
                int price = response.getInt("price");
                long categoryId = response.getLong("category_id");

                return new MealModel(id, name, description, price, categoryId);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static List<MealModel> getAll(){
        try {
            List<MealModel> list = new ArrayList<>();
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM meals");

            while (response.next()) {
                int id = response.getInt("id");
                String name = response.getString("name");
                String description = response.getString("description");
                int price = response.getInt("price");
                long categoryId = response.getLong("category_id");

                list.add(new MealModel(id, name, description, price, categoryId));
            }

            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static void add(MealModel mealModel){
        try {
            if (mealModel.getId() == 0) {
                DataBaseManager.executeUpdate(String.format("INSERT INTO meals(name, description, price, category_id) VALUES('%s', '%s', %d, %d)",
                        mealModel.getName(),
                        mealModel.getDescription(),
                        mealModel.getPrice(),
                        mealModel.getCategoryId()
                ));
            } else {
                DataBaseManager.executeUpdate(String.format("INSERT INTO meals(id, name, description, price, category_id) VALUES(%d, '%s', '%s', %d, %d)",
                        mealModel.getId(),
                        mealModel.getName(),
                        mealModel.getDescription(),
                        mealModel.getPrice(),
                        mealModel.getCategoryId()
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void remove(long id) {
        try {
            DataBaseManager.executeUpdate(String.format("DELETE FROM %s WHERE id=%d", MealModel.getTableName(), id));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void remove(String name) {
        try {
            DataBaseManager.executeUpdate(String.format("DELETE FROM %s WHERE name='%s'", MealModel.getTableName(), name));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void update(MealModel mealModel){
        try {
            DataBaseManager.executeUpdate(String.format("UPDATE users SET name='%s', description='%s', price=%d WHERE id=%d",
                    mealModel.getName(),
                    mealModel.getDescription(),
                    mealModel.getPrice(),
                    mealModel.getId()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
