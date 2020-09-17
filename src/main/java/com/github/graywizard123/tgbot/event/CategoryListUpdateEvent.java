package com.github.graywizard123.tgbot.event;

import com.github.graywizard123.tgbot.db.models.CategoryModel;

import java.util.List;

public class CategoryListUpdateEvent implements ITGBotEvent {

    private final List<CategoryModel> categories;

    public CategoryListUpdateEvent(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    @Override
    public Class<? extends ITGBotEvent> getType() {
        return getClass();
    }
}
