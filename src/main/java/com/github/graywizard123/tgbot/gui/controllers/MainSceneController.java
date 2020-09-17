package com.github.graywizard123.tgbot.gui.controllers;

import com.github.graywizard123.tgbot.db.models.OrderModel;
import com.github.graywizard123.tgbot.db.repositories.OrderRepository;
import com.github.graywizard123.tgbot.event.EventManager;
import com.github.graywizard123.tgbot.event.OrderListUpdateEvent;
import com.github.graywizard123.tgbot.gui.SceneManager;
import com.github.graywizard123.tgbot.gui.model.MealTableItem;
import com.github.graywizard123.tgbot.telegram.TelegramManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @FXML
    public Button acceptButton, declineButton;

    @FXML
    public MenuItem openMenuButton, closeButton;

    @FXML
    public Label phoneField, addressField;

    @FXML
    public ListView<Long> orderListView;

    @FXML
    public TableView<MealTableItem> orderInfoView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventManager.registerListener(OrderListUpdateEvent.class, event -> updateOrderList((OrderListUpdateEvent) event));

        EventManager.callEvent(new OrderListUpdateEvent(OrderRepository.getAll()));

        openMenuButton.setOnAction(event -> openMenu());
        acceptButton.setOnAction(event -> accept());
        declineButton.setOnAction(event -> decline());
        orderListView.setOnMouseClicked(event -> updateOrderAdditionalData());
    }

    private void accept() {
        long orderId = orderListView.getSelectionModel().getSelectedItem();

        OrderModel orderModel = OrderRepository.getById(orderId);

        SendMessage sendMessageAction = new SendMessage(orderModel.getFrom().getTelegramId(), "Your order accepted");
        TelegramManager.execute(sendMessageAction);

        OrderRepository.remove(orderId);
    }

    private void decline() {
        long orderId = orderListView.getSelectionModel().getSelectedItem();

        OrderModel orderModel = OrderRepository.getById(orderId);

        SendMessage sendMessageAction = new SendMessage(orderModel.getFrom().getTelegramId(), "Your order declined");
        TelegramManager.execute(sendMessageAction);

        OrderRepository.remove(orderId);
    }

    private void updateOrderAdditionalData() {
        long orderId = orderListView.getSelectionModel().getSelectedItem();
        OrderModel orderModel = OrderRepository.getById(orderId);

        addressField.setText(orderModel.getAddress());
        phoneField.setText(orderModel.getPhone());

        ArrayList<MealTableItem> tableItems = new ArrayList<>();

        orderModel.getMeals().forEach((meal, count) -> {
            tableItems.add(new MealTableItem(meal.getName(), count, meal.getPrice() * count));
        });

        orderInfoView.setItems(FXCollections.observableList(tableItems));
    }

    private void updateOrderList(OrderListUpdateEvent event) {
        List<OrderModel> orderModelList = event.getOrderList();

        if(orderModelList != null) {
            List<Long> orderListIDsRaw = new ArrayList<>();

            for (OrderModel orderModel : orderModelList) {
                orderListIDsRaw.add(orderModel.getId());
            }

            ObservableList<Long> orderListIDs = FXCollections.observableList(orderListIDsRaw);
            orderListView.setItems(orderListIDs);
        }
    }

    private void openMenu() {
        SceneManager.setScene("menu");
    }
}
