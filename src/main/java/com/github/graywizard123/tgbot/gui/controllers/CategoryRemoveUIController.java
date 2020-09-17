package com.github.graywizard123.tgbot.gui.controllers;

import com.github.graywizard123.tgbot.db.models.CategoryModel;
import com.github.graywizard123.tgbot.db.repositories.CategoryRepository;
import com.github.graywizard123.tgbot.db.repositories.MealRepository;
import com.github.graywizard123.tgbot.event.CategoryListUpdateEvent;
import com.github.graywizard123.tgbot.event.EventManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CategoryRemoveUIController implements Initializable {

    @FXML
    public Button removeButton;

    @FXML
    public ListView<String> categoryListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventManager.registerListener(CategoryListUpdateEvent.class, event -> {
            ArrayList<String> titlesList = new ArrayList<>();
            for (CategoryModel categoryModel : ((CategoryListUpdateEvent) event).getCategories())
                titlesList.add(categoryModel.getTitle());

            categoryListView.setItems(FXCollections.observableList(titlesList));
        });

        removeButton.setOnAction(event -> removeCategory());
    }

    private void removeCategory() {
        String title = categoryListView.getSelectionModel().getSelectedItem();

        CategoryModel categoryModel = CategoryRepository.getByTitle(title);
        categoryModel.getMeals().forEach(meal -> MealRepository.remove(meal.getId()));

        CategoryRepository.remove(title);

        EventManager.callEvent(new CategoryListUpdateEvent(CategoryRepository.getAll()));
    }
}
