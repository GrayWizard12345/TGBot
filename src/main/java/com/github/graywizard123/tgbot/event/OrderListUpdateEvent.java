package com.github.graywizard123.tgbot.event;

import java.util.List;

public class OrderListUpdateEvent implements ITGBotEvent {

    private final List<Long> orderList;

    public OrderListUpdateEvent(List<Long> orderList) {
        this.orderList = orderList;
    }

    public List<Long> getOrderList() {
        return orderList;
    }

    @Override
    public Class<? extends ITGBotEvent> getType() {
        return getClass();
    }
}
