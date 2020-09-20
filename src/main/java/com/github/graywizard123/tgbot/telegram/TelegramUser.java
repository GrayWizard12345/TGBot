package com.github.graywizard123.tgbot.telegram;

import java.util.HashMap;

public class TelegramUser {

    private long id;
    private ClientStage stage;
    private String phone;
    private String address;
    private HashMap<Integer, Long> basket;
    private long selectedMealId;

    public TelegramUser(long id, ClientStage stage) {
        this.id = id;
        this.stage = stage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ClientStage getStage() {
        return stage;
    }

    public void setStage(ClientStage stage) {
        this.stage = stage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HashMap<Integer, Long> getBasket() {
        return basket;
    }

    public void setBasket(HashMap<Integer, Long> basket) {
        this.basket = basket;
    }

    public long getSelectedMealId() {
        return selectedMealId;
    }

    public void setSelectedMealId(long selectedMealId) {
        this.selectedMealId = selectedMealId;
    }
}
