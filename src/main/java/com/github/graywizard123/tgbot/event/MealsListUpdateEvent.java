package com.github.graywizard123.tgbot.event;

import com.github.graywizard123.tgbot.db.models.MealModel;

import java.util.List;

public class MealsListUpdateEvent implements ITGBotEvent {

    private final List<MealModel> mealModels;

    public MealsListUpdateEvent(List<MealModel> mealModels) {
        this.mealModels = mealModels;
    }

    public List<MealModel> getMeals() {
        return mealModels;
    }

    @Override
    public Class<? extends ITGBotEvent> getType() {
        return getClass();
    }
}
