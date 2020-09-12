package com.github.graywizard123.tgbot.db.repositories;

import com.github.graywizard123.tgbot.App;
import com.github.graywizard123.tgbot.db.DataBaseManager;
import com.github.graywizard123.tgbot.db.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public static User getById(long id){
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM users WHERE id="+ id);

            if (response.first()) {
                long telegramId = response.getLong("telegram_id");
                String savedPhone = response.getNString("saved_phone");
                String savedAddress = response.getNString("saved_address");

                return new User(id, telegramId, savedPhone, savedAddress);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            App.LOGGER.error("Cannot get user with " + id + " id");
            throwables.printStackTrace();
            return null;
        }
    }

    public static User getByTelegramId(long telegramId) {
        try {
            ResultSet response = DataBaseManager.executeQuery("SELECT * FROM users WHERE telegram_id=\""+ telegramId + "\"");

            if (response.first()) {
                int id = response.getInt("id");
                String savedPhone = response.getNString("saved_phone");
                String savedAddress = response.getNString("saved_address");

                return new User(id, telegramId, savedPhone, savedAddress);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            App.LOGGER.error("Cannot get user with " + telegramId);
            throwables.printStackTrace();
            return null;
        }
    }

    public static void update(User user){
        try {
            DataBaseManager.executeUpdate(String.format("UPDATE users SET saved_phone=\"%s\", saved_address=\"%s\" WHERE id=%d",
                    user.getSavedPhone(),
                    user.getSavedAddress(),
                    user.getId()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void add(User user) {
        try {
            DataBaseManager.executeUpdate(String.format("INSERT INTO users(id, telegram_id, saved_phone, saved_address) VALUES(%d, \"%s\", \"%s\", \"%s\")",
                    user.getId() == 0 ? null : user.getId(),
                    user.getTelegramId(),
                    user.getSavedPhone(),
                    user.getSavedAddress()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
