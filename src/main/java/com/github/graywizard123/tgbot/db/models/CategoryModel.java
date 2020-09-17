package com.github.graywizard123.tgbot.db.models;

import com.github.graywizard123.tgbot.db.table.TableColumn;
import com.github.graywizard123.tgbot.db.table.TableColumnType;

import java.util.List;

public class CategoryModel {

    private final long id;
    private String title;
    private List<MealModel> mealModels;

    public CategoryModel(long id, String title, List<MealModel> mealModels) {
        this.id = id;
        this.title = title;
        this.mealModels = mealModels;
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

    public List<MealModel> getMeals() {
        return mealModels;
    }

    public void setMeals(List<MealModel> mealModels) {
        this.mealModels = mealModels;
    }

    public static String getTableName(){
        return "categories";
    }

    public static TableColumn[] getTableColumns(){
        return new TableColumn[]{
                new TableColumn("id", TableColumnType.SERIAL, "UNIQUE", "NOT NULL"),
                new TableColumn("title", TableColumnType.TEXT, "UNIQUE"),
                new TableColumn("meals", TableColumnType.TEXT)
        };
    }
}
