package com.github.graywizard123.tgbot.db.repositories;

import com.github.graywizard123.tgbot.db.DataBaseManager;
import com.github.graywizard123.tgbot.db.models.MealModel;
import com.github.graywizard123.tgbot.db.models.OrderModel;
import com.github.graywizard123.tgbot.db.models.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderRepository {

    private static String serializeMeals(HashMap<MealModel, Integer> meals){
        StringBuilder resultBuilder = new StringBuilder();
        for (MealModel mealModel :
             meals.keySet()) {
            resultBuilder
                    .append(mealModel.getId())
                    .append("-")
                    .append(meals.get(mealModel))
                    .append(";");
        }
        return resultBuilder.toString();
    }

    private static HashMap<MealModel, Integer> deserializeMeals(String serializedMap) {
        HashMap<MealModel, Integer> meals = new HashMap<>();
        String[] serializedEntries = serializedMap.split(";");

        for (String serializedEntry : serializedEntries) {
            String[] entryData = serializedEntry.split("-");
            MealModel mealModel = MealRepository.getById(Integer.parseInt(entryData[0]));
            int count = Integer.parseInt(entryData[1]);
            meals.put(mealModel, count);
        }

        return meals;
    }

    public static OrderModel getById(long id) {
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM orders WHERE id="+ id);

            if (response.next()) {
                int userId = response.getInt("user_id");
                String phone = response.getString("phone");
                String address = response.getString("order_address");
                String serializedMeals = response.getString("meals");
                OrderModel.PaymentType paymentType = OrderModel.PaymentType.valueOf(response.getString("payment_type"));

                return new OrderModel(id, UserRepository.getById(userId), address, phone, deserializeMeals(serializedMeals), paymentType);
            } else {
                return null;
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return null;
    }

    public static List<OrderModel> getAll() {
        List<OrderModel> orderModels = new ArrayList<>();

        try {
            ResultSet response = DataBaseManager.executeQuery(String.format("SELECT * FROM %s", OrderModel.getTableName()));

            while (response.next()) {
                long id = response.getLong("id");
                UserModel userModel = UserRepository.getById(response.getLong("user_id"));
                String address = response.getString("order_address");
                String phone = response.getString("phone");
                HashMap<MealModel, Integer> meals = deserializeMeals(response.getString("meals"));
                OrderModel.PaymentType paymentType = OrderModel.PaymentType.valueOf(response.getString("payment_type"));

                orderModels.add(new OrderModel(id, userModel, address, phone, meals, paymentType));
            }

            return orderModels;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void add(OrderModel orderModel){
        try {
            if (orderModel.getId() == 0) {
                DataBaseManager.executeUpdate(String.format("INSERT INTO %s(user_id, order_address, phone, meals, payment_type) VALUES(%d, '%s', '%s', '%s', '%s')",
                        OrderModel.getTableName(),
                        orderModel.getFrom().getId(),
                        orderModel.getAddress(),
                        orderModel.getPhone(),
                        serializeMeals(orderModel.getMeals()),
                        orderModel.getPaymentType().name()));
            } else {
                DataBaseManager.executeUpdate(String.format("INSERT INTO %s(id, user_id, order_address, phone, meals, payment_type) VALUES(%d, %d, '%s', '%s', '%s', '%s')",
                        OrderModel.getTableName(),
                        orderModel.getId(),
                        orderModel.getFrom().getId(),
                        orderModel.getAddress(),
                        orderModel.getPhone(),
                        serializeMeals(orderModel.getMeals()),
                        orderModel.getPaymentType().name()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void remove(long id) {
        try {
            DataBaseManager.executeUpdate(String.format("DELETE FROM %s WHERE id=%d", OrderModel.getTableName(), id));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
