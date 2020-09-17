package com.github.graywizard123.tgbot.db.models;

import com.github.graywizard123.tgbot.db.table.TableColumn;
import com.github.graywizard123.tgbot.db.table.TableColumnType;

public class UserModel {

    private final long id;
    private final long telegramId;
    private String savedPhone;
    private String savedAddress;

    public UserModel(long id, long telegramId, String savedPhone, String savedAddress) {
        this.id = id;
        this.telegramId = telegramId;
        this.savedPhone = savedPhone;
        this.savedAddress = savedAddress;
    }

    public long getId() {
        return id;
    }

    public long getTelegramId() {
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
                new TableColumn("id", TableColumnType.SERIAL, "UNIQUE", "NOT NULL"),
                new TableColumn("telegram_id", TableColumnType.TEXT, "NOT NULL", "UNIQUE"),
                new TableColumn("saved_phone", TableColumnType.TEXT),
                new TableColumn("saved_address", TableColumnType.TEXT)
        };
    }
}
