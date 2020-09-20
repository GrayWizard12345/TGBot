package com.github.graywizard123.tgbot.db.repositories;

import com.github.graywizard123.tgbot.App;
import com.github.graywizard123.tgbot.db.DataBaseManager;
import com.github.graywizard123.tgbot.db.models.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public static UserModel getById(long id){
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM users WHERE id="+ id);

            if (response.next()) {
                long telegramId = response.getLong("telegram_id");
                String savedPhone = response.getString("saved_phone");
                String savedAddress = response.getString("saved_address");

                return new UserModel(id, telegramId, savedPhone, savedAddress);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            App.LOGGER.error("Cannot get user with " + id + " id");
            throwables.printStackTrace();
            return null;
        }
    }

    public static UserModel getByTelegramId(long telegramId) {
        try {
            ResultSet response = DataBaseManager.executeQuery(String.format("SELECT * FROM users WHERE telegram_id=%d", telegramId));

            if (response.next()) {
                int id = response.getInt("id");
                String savedPhone = response.getString("saved_phone");
                String savedAddress = response.getString("saved_address");

                return new UserModel(id, telegramId, savedPhone, savedAddress);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            App.LOGGER.error("Cannot get user with " + telegramId);
            throwables.printStackTrace();
            return null;
        }
    }

    public static void update(UserModel userModel){
        try {
            DataBaseManager.executeUpdate(String.format("UPDATE users SET saved_phone='%s', saved_address='%s' WHERE id=%d",
                    userModel.getSavedPhone(),
                    userModel.getSavedAddress(),
                    userModel.getId()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void add(UserModel userModel) {
        try {
            if (userModel.getId() == 0) {
                DataBaseManager.executeUpdate(String.format("INSERT INTO users(telegram_id, saved_phone, saved_address) VALUE(%d, '%s', '%s')",
                        userModel.getTelegramId(),
                        userModel.getSavedPhone() == null ? "" : userModel.getSavedPhone(),
                        userModel.getSavedAddress() == null ? "" : userModel.getSavedAddress()));
            } else  {
                DataBaseManager.executeUpdate(String.format("INSERT INTO users(id, telegram_id, saved_phone, saved_address) VALUES(%d, %d, '%s', '%s')",
                        userModel.getId(),
                        userModel.getTelegramId(),
                        userModel.getSavedPhone() == null ? "" : userModel.getSavedPhone(),
                        userModel.getSavedAddress() == null ? "" : userModel.getSavedAddress()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
