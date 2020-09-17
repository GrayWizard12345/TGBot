package com.github.graywizard123.tgbot.db.repositories;

import com.github.graywizard123.tgbot.db.DataBaseManager;
import com.github.graywizard123.tgbot.db.models.CategoryModel;
import com.github.graywizard123.tgbot.db.models.MealModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private static String serializeMeals(List<MealModel> mealModels) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < mealModels.size(); i++) {
            builder.append(mealModels.get(i).getId());
            if (i != mealModels.size() - 1) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    private static List<MealModel> deserializeMeals(String serializedMeals) {
        String[] buffer = serializedMeals.split(",");
        List<MealModel> mealModels = new ArrayList<>();

        for (String mealID : buffer) {
            if (!mealID.isEmpty() && !mealID.equals("null"))
                mealModels.add(MealRepository.getById(Long.parseLong(mealID)));
        }

        return mealModels;
    }

    public static void add(CategoryModel categoryModel) {
        try {
            if (categoryModel.getId() == 0) {
                DataBaseManager.executeUpdate(String.format("INSERT INTO %s(title, meals) VALUES('%s', '%s')",
                        CategoryModel.getTableName(),
                        categoryModel.getTitle(),
                        serializeMeals(categoryModel.getMeals())));
            } else {
                DataBaseManager.executeUpdate(String.format("INSERT INTO %s(id, title, meals) VALUES(%d, '%s', '%s')",
                        CategoryModel.getTableName(),
                        categoryModel.getId(),
                        categoryModel.getTitle(),
                        serializeMeals(categoryModel.getMeals())));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static CategoryModel getById(long id) {
        try {
            ResultSet response = DataBaseManager.executeQuery(String.format("SELECT * FROM %s WHERE id=%d", CategoryModel.getTableName(), id));

            if (response.next()) {
                String title = response.getString("title");
                List<MealModel> mealModels = deserializeMeals(response.getString("meals"));

                return new CategoryModel(id, title, mealModels);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static CategoryModel getByTitle(String title) {
        try {
            ResultSet response = DataBaseManager.executeQuery(String.format("SELECT * FROM %s WHERE title='%s'", CategoryModel.getTableName(), title));

            if (response.next()) {
                long id = response.getLong("id");
                List<MealModel> mealModels = deserializeMeals(response.getString("meals"));

                return new CategoryModel(id, title, mealModels);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static void update(CategoryModel categoryModel) {
        try {
            DataBaseManager.executeUpdate(String.format("UPDATE %s SET title='%s', meals='%s' WHERE id=%d",
                    CategoryModel.getTableName(),
                    categoryModel.getTitle(),
                    serializeMeals(categoryModel.getMeals()),
                    categoryModel.getId()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void remove(String title) {
        try {
            DataBaseManager.executeUpdate(String.format("DELETE FROM %s WHERE title='%s'", CategoryModel.getTableName(), title));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<CategoryModel> getAll() {
        try {
            List<CategoryModel> categories = new ArrayList<>();
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM " + CategoryModel.getTableName());

            while (response.next()) {
                long id = response.getLong("id");
                String title = response.getString("title");
                List<MealModel> mealModels = deserializeMeals(response.getString("meals"));

                categories.add(new CategoryModel(id, title, mealModels));
            }

            return categories;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
