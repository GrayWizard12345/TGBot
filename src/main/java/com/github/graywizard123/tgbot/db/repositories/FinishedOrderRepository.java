package com.github.graywizard123.tgbot.db.repositories;

import com.github.graywizard123.tgbot.db.DataBaseManager;
import com.github.graywizard123.tgbot.db.models.FinishedOrderModel;
import com.github.graywizard123.tgbot.db.models.MealModel;
import com.github.graywizard123.tgbot.db.models.OrderModel;
import com.github.graywizard123.tgbot.db.models.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FinishedOrderRepository {

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

    public static List<FinishedOrderModel> getAll() {
        List<FinishedOrderModel> orderModels = new ArrayList<>();

        try {
            ResultSet response = DataBaseManager.executeQuery(String.format("SELECT * FROM %s", OrderModel.getTableName()));

            while (response.next()) {
                long id = response.getLong("id");
                UserModel userModel = UserRepository.getById(response.getLong("user_id"));
                String address = response.getString("order_address");
                String phone = response.getString("phone");
                HashMap<MealModel, Integer> meals = deserializeMeals(response.getString("meals"));
                FinishedOrderModel.PaymentType paymentType = FinishedOrderModel.PaymentType.valueOf(response.getString("payment_type"));
                boolean isAccepted = response.getBoolean("is_accepted");
                Date date = new Date(response.getLong("date"));

                orderModels.add(new FinishedOrderModel(id, userModel, address, phone, meals, paymentType, isAccepted, date));
            }

            return orderModels;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void add(FinishedOrderModel orderModel){
        try {
            if (orderModel.getId() == 0) {
                DataBaseManager.executeUpdate(String.format("INSERT INTO %s(user_id, order_address, phone, meals, payment_type, is_accepted, date) VALUES(%d, '%s', '%s', '%s', '%s', %b, %d)",
                        OrderModel.getTableName(),
                        orderModel.getFrom().getId(),
                        orderModel.getAddress(),
                        orderModel.getPhone(),
                        serializeMeals(orderModel.getMeals()),
                        orderModel.getPaymentType().name(),
                        orderModel.isAccepted(),
                        orderModel.getDate().getTime()
                ));
            } else {
                DataBaseManager.executeUpdate(String.format("INSERT INTO %s(id, user_id, order_address, phone, meals, payment_type, is_accepted, date) VALUES(%d, %d, '%s', '%s', '%s', %b, %d)",
                        OrderModel.getTableName(),
                        orderModel.getId(),
                        orderModel.getFrom().getId(),
                        orderModel.getAddress(),
                        orderModel.getPhone(),
                        serializeMeals(orderModel.getMeals()),
                        orderModel.isAccepted(),
                        orderModel.getDate().getTime()
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
