package com.github.graywizard123.tgbot.db.repositories;

import com.github.graywizard123.tgbot.db.DataBaseManager;
import com.github.graywizard123.tgbot.db.models.Category;
import com.github.graywizard123.tgbot.db.models.Meal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private static String serializeMeals(List<Meal> meals) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0;i < meals.size();i++) {
            builder.append(meals.get(i));
            if (i != meals.size() - 1) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    private static List<Meal> deserializeMeals(String serializedMeals) {
        String[] buffer = serializedMeals.split(",");
        List<Meal> meals = new ArrayList<>();

        for (String mealID : buffer) {
            meals.add(MealRepository.getById(Long.parseLong(mealID)));
        }

        return meals;
    }

    public static void add(Category category) {
        try {
            DataBaseManager.executeUpdate(String.format("INSERT INTO %s(?, ?, ?) VALUES(%d, \"%s\", \"%s\")",
                    Category.getTableName(),
                    category.getId() == 0 ? null : category.getId(),
                    category.getTitle(),
                    serializeMeals(category.getMeals())));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Category getById(long id) {
        try {
            ResultSet response = DataBaseManager.executeQuery(String.format("SELECT * FROM %s WHERE id=%d", Category.getTableName(), id));

            if (response.first()) {
                String title = response.getNString("title");
                List<Meal> meals = deserializeMeals(response.getNString("meals"));

                return new Category(id, title, meals);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static Category getByTitle(String title) {
        try {
            ResultSet response = DataBaseManager.executeQuery(String.format("SELECT * FROM %s WHERE title=\"%s\"", Category.getTableName(), title));

            if (response.first()) {
                long id = response.getLong("id");
                List<Meal> meals = deserializeMeals(response.getNString("meals"));

                return new Category(id, title, meals);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static void update(Category category) {
        try {
            DataBaseManager.executeUpdate(String.format("UPDATE %s SET title=\"%s\", meals=\"%s\" WHERE id=%d",
                    Category.getTableName(),
                    category.getTitle(),
                    serializeMeals(category.getMeals()),
                    category.getId()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<Category> getAll() {
        try {
            List<Category> categories = new ArrayList<>();
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM " + Category.getTableName());

            while (response.next()) {
                long id = response.getLong("id");
                String title = response.getNString("title");
                List<Meal> meals = deserializeMeals(response.getNString("meals"));

                categories.add(new Category(id, title, meals));
            }

            return categories;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}