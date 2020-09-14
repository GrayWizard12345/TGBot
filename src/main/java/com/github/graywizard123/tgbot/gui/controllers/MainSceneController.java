package com.github.graywizard123.tgbot.gui.controllers;

import com.github.graywizard123.tgbot.event.EventManager;
import com.github.graywizard123.tgbot.event.OrderListUpdateEvent;
import com.github.graywizard123.tgbot.gui.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
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
    public ListView<Long> orderList;

    @FXML
    public TableView orderInfoView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openMenuButton.setOnAction(event -> openMenu());

        EventManager.registerListener(OrderListUpdateEvent.class, event -> updateOrderList((OrderListUpdateEvent) event));
    }

    private void updateOrderList(OrderListUpdateEvent event) {
        List<Long> orderListIDsRaw = event.getOrderList();
        ObservableList<Long> orderListIDs = FXCollections.observableList(orderListIDsRaw);
        orderList.setItems(orderListIDs);
    }

    private void openMenu() {
        SceneManager.setScene("menu");
    }
}
