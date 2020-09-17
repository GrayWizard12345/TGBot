package com.github.graywizard123.tgbot.gui.controllers;

import com.github.graywizard123.tgbot.db.models.CategoryModel;
import com.github.graywizard123.tgbot.db.models.MealModel;
import com.github.graywizard123.tgbot.db.repositories.CategoryRepository;
import com.github.graywizard123.tgbot.db.repositories.MealRepository;
import com.github.graywizard123.tgbot.event.CategoryListUpdateEvent;
import com.github.graywizard123.tgbot.event.EventManager;
import com.github.graywizard123.tgbot.event.MealsListUpdateEvent;
import com.github.graywizard123.tgbot.gui.SceneManager;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
        EventManager.registerListener(CategoryListUpdateEvent.class, event -> updateCategoriesView((CategoryListUpdateEvent) event));
        EventManager.callEvent(new MealsListUpdateEvent(MealRepository.getAll()));
        EventManager.callEvent(new CategoryListUpdateEvent(CategoryRepository.getAll()));

        priceField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(500, 500000, 500, 500));

        addMealButton.setOnAction(event -> addMeal());
        removeMealButton.setOnAction(event -> removeMeal());
        applyButton.setOnAction(event -> SceneManager.setScene("main"));

        addCategoryButton.setOnAction(event -> SceneManager.setScene("category_add_ui"));
        deleteCategoryButton.setOnAction(event -> SceneManager.setScene("category_remove_ui"));

        mealsListView.setEditable(false);
        mealsListView.setOnMouseClicked(this::updateAdditionalData);
    }

    private void updateCategoriesView(CategoryListUpdateEvent event) {
        List<CategoryModel> categories = event.getCategories();
        List<String> categoriesTitles = new ArrayList<>();

        for (CategoryModel categoryModel : categories) {
            categoriesTitles.add(categoryModel.getTitle());
        }

        categoryChoiceBox.setItems(FXCollections.observableList(categoriesTitles));
    }

    private void removeMeal() {
        MealRepository.remove(mealsListView.getSelectionModel().getSelectedItem());
        EventManager.callEvent(new MealsListUpdateEvent(MealRepository.getAll()));
    }

    private void addMeal() {
        String categoryTitle = categoryChoiceBox.getValue();
        CategoryModel categoryModel = CategoryRepository.getByTitle(categoryTitle);

        MealRepository.add(new MealModel(0, nameField.getText(), descriptionField.getText(), priceField.getValue(), categoryModel.getId()));

        MealModel mealModel = MealRepository.getByName(nameField.getText());
        Objects.requireNonNull(categoryModel)
                        .getMeals()
                        .add(mealModel);

        CategoryRepository.update(categoryModel);

        EventManager.callEvent(new MealsListUpdateEvent(MealRepository.getAll()));
    }

    private void updateAdditionalData(Event event) {
        MealModel mealModel = MealRepository.getByName(mealsListView.getSelectionModel().getSelectedItem());
        nameField.setText(mealModel.getName());
        descriptionField.setText(mealModel.getDescription());
        priceField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(500, 500000, mealModel.getPrice(), 500));
        categoryChoiceBox.setValue(CategoryRepository.getById(mealModel.getCategoryId()).getTitle());
    }

    private void updateMealsView(MealsListUpdateEvent event) {
        ArrayList<String> titlesList = new ArrayList<>();
        for (MealModel mealModel : event.getMeals())
            titlesList.add(mealModel.getName());
        mealsListView.setItems(FXCollections.observableList(titlesList));
    }
}
