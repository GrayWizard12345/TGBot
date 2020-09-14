package com.github.graywizard123.tgbot.gui.controllers;

import com.github.graywizard123.tgbot.db.models.Meal;
import com.github.graywizard123.tgbot.db.repositories.CategoryRepository;
import com.github.graywizard123.tgbot.db.repositories.MealRepository;
import com.github.graywizard123.tgbot.event.EventManager;
import com.github.graywizard123.tgbot.event.MealsListUpdateEvent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuSceneController implements Initializable {

    @FXML
    public Button addMealButton, removeMealButton, applyButton;

    @FXML
    public MenuItem addCategoryButton, deleteCategoryButton;

    @FXML
    public TextField nameField;

    @FXML
    public Spinner<Integer> priceField;

    @FXML
    public TextArea descriptionField;

    @FXML
    public ChoiceBox<String> categoryChoiceBox;

    @FXML
    public ListView<String> mealsListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventManager.registerListener(MealsListUpdateEvent.class, event -> updateMealsView((MealsListUpdateEvent) event));
        EventManager.callEvent(new MealsListUpdateEvent(MealRepository.getAll()));

        priceField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(500, -1, 500, 500));

        addMealButton.setOnAction(event -> addMeal());
        removeMealButton.setOnAction(event -> removeMeal());
        mealsListView.setEditable(false);
        mealsListView.setOnEditStart(event -> updateAdditionalData(event));
    }

    private void removeMeal() {
        MealRepository.remove(mealsListView.getSelectionModel().getSelectedItem());
        EventManager.callEvent(new MealsListUpdateEvent(MealRepository.getAll()));
    }

    private void addMeal() {
        MealRepository.add(new Meal(0, nameField.getText(), descriptionField.getText(), priceField.getValue(), CategoryRepository.getByTitle(categoryChoiceBox.getValue())));

        Meal meal = MealRepository.getByName(nameField.getText());
        Objects.requireNonNull(
                CategoryRepository
                        .getByTitle(categoryChoiceBox.getValue()))
                        .getMeals()
                        .add(meal);

        EventManager.callEvent(new MealsListUpdateEvent(MealRepository.getAll()));
    }

    private void updateAdditionalData(ListView.EditEvent<String> event) {
        Meal meal = MealRepository.getByName(event.toString());
        nameField.setText(meal.getName());
        descriptionField.setText(meal.getDescription());
        priceField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(500, -1, meal.getPrice(), 500));
        categoryChoiceBox.setValue(meal.getCategory().getTitle());
    }

    private void updateMealsView(MealsListUpdateEvent event) {
        ArrayList<String> titlesList = new ArrayList<>();
        for (Meal meal : event.getMeals())
            titlesList.add(meal.getName());
        mealsListView.setItems(FXCollections.observableList(titlesList));
    }
}
