package com.github.graywizard123.tgbot.db.models;

import com.github.graywizard123.tgbot.db.table.TableColumn;
import com.github.graywizard123.tgbot.db.table.TableColumnType;

public class User {

    private final int id;
    private final String telegramId;
    private String savedPhone;
    private String savedAddress;

    public User(int id, String telegramId, String savedPhone, String savedAddress) {
        this.id = id;
        this.telegramId = telegramId;
        this.savedPhone = savedPhone;
        this.savedAddress = savedAddress;
    }

    public int getId() {
        return id;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public String getSavedPhone() {
        return savedPhone;
    }

    public String getSavedAddress() {
        return savedAddress;
    }

    public void setSavedPhone(String savedPhone) {
        this.savedPhone = savedPhone;
    }

    public void setSavedAddress(String savedAddress) {
        this.savedAddress = savedAddress;
    }

    public static String getTableName() {
        return "users";
    }

    public static TableColumn[] getTableColumns() {
        return new TableColumn[]{
                new TableColumn("id", TableColumnType.INTEGER, "NOT NULL", "UNIQUE", "AUTOINCREMENT", "PRIMARY KEY"),
                new TableColumn("telegram_id", TableColumnType.TEXT, "NOT NULL", "UNIQUE"),
                new TableColumn("saved_phone", TableColumnType.TEXT),
                new TableColumn("saved_address", TableColumnType.TEXT)
        };
    }
}
