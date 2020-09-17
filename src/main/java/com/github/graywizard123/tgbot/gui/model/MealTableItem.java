package com.github.graywizard123.tgbot.gui.model;

public class MealTableItem {

    private final String name;
    private final int count;
    private final int price;

    public MealTableItem(String name, int count, int price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }
}
