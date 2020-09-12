package com.github.graywizard123.tgbot.db.repositories;

import com.github.graywizard123.tgbot.db.DataBaseManager;
import com.github.graywizard123.tgbot.db.models.Meal;
import com.github.graywizard123.tgbot.db.models.Order;
import com.github.graywizard123.tgbot.db.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderRepository {

    private static String serializeMeals(HashMap<Meal, Integer> meals){
        StringBuilder resultBuilder = new StringBuilder();
        for (Meal meal:
             meals.keySet()) {
            resultBuilder
                    .append(meal.getId())
                    .append("-")
                    .append(meals.get(meal))
                    .append(";");
        }
        return resultBuilder.toString();
    }

    private static HashMap<Meal, Integer> deserializeMeals(String serializedMap){
        HashMap<Meal, Integer> meals = new HashMap<>();
        String[] serializedEntries = serializedMap.split(";");

        for (String serializedEntry : serializedEntries) {
            String[] entryData = serializedEntry.split("-");
            Meal meal = MealRepository.getById(Integer.parseInt(entryData[0]));
            int count = Integer.parseInt(entryData[1]);
            meals.put(meal, count);
        }

        return meals;
    }

    public static Order getById(long id) {
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM orders WHERE id="+ id);

            if (response.first()) {
                int userId = response.getInt("user_id");
                String phone = response.getNString("phone");
                String address = response.getNString("address");
                String serializedMeals = response.getNString("meals");

                return new Order(id, UserRepository.getById(userId), address, phone, deserializeMeals(serializedMeals));
            } else {
                return null;
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return null;
    }

    public static void add(Order order){
        try {
            DataBaseManager.executeUpdate(String.format("INSERT INTO users(id, user_id, address, phone, meals) VALUES(%d, %d, \"%s\", \"%s\", \"%s\")",
                    order.getId() == 0 ? null : order.getId(),
                    order.getFrom().getId(),
                    order.getAddress(),
                    order.getPhone(),
                    serializeMeals(order.getMeals())));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<Integer> getListOfIds(){
        List<Integer> ids = new ArrayList<>();
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT id FROM orders");

            while (response.next()){
                ids.add(response.getInt(0));
            }

            return ids;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
