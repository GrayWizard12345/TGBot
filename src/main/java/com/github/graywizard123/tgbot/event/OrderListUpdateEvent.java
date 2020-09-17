package com.github.graywizard123.tgbot.event;

import com.github.graywizard123.tgbot.db.models.OrderModel;

import java.util.List;

public class OrderListUpdateEvent implements ITGBotEvent {

    private final List<OrderModel> orderModelList;

    public OrderListUpdateEvent(List<OrderModel> orderModelList) {
        this.orderModelList = orderModelList;
    }

    public List<OrderModel> getOrderList() {
        return orderModelList;
    }

    @Override
    public Class<? extends ITGBotEvent> getType() {
        return getClass();
    }
}
