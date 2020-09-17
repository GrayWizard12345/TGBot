package com.github.graywizard123.tgbot.gui.controllers;

import com.github.graywizard123.tgbot.db.models.CategoryModel;
import com.github.graywizard123.tgbot.db.repositories.CategoryRepository;
import com.github.graywizard123.tgbot.event.CategoryListUpdateEvent;
import com.github.graywizard123.tgbot.event.EventManager;
import com.github.graywizard123.tgbot.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CategoryAddUIController implements Initializable {

    @FXML
    public Button applyButton;

    @FXML
    public TextField nameField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applyButton.setOnAction(event -> {
            String title = nameField.getText();

            CategoryModel categoryModel = new CategoryModel(0, title, new ArrayList<>());
            CategoryRepository.add(categoryModel);

            EventManager.callEvent(new CategoryListUpdateEvent(CategoryRepository.getAll()));

            SceneManager.setScene("menu");
        });
    }

}
