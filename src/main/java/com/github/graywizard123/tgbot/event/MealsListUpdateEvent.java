package com.github.graywizard123.tgbot.event;

import com.github.graywizard123.tgbot.db.models.Meal;

import java.util.List;

public class MealsListUpdateEvent implements ITGBotEvent {

    private final List<Meal> meals;

    public MealsListUpdateEvent(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    @Override
    public Class<? extends ITGBotEvent> getType() {
        return getClass();
    }
}
