package com.github.graywizard123.tgbot.event;

import java.util.List;

public class TGBotOrderListUpdateEvent implements ITGBotEvent {

    private List<Long> orderList;

    public TGBotOrderListUpdateEvent(List<Long> orderList) {
        this.orderList = orderList;
    }

    public List<Long> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Long> orderList) {
        this.orderList = orderList;
    }

    @Override
    public Class<? extends ITGBotEvent> getType() {
        return getClass();
    }
}
