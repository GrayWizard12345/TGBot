package com.github.graywizard123.tgbot.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class SceneManager {

    private static HashMap<String, Scene> scenes;
    private static final SceneLoader sceneLoader = new SceneLoader();
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        SceneManager.primaryStage = primaryStage;
    }

    public static void init(){
        try {
            scenes = sceneLoader.loadScenes();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void setScene(String sceneName) {
        primaryStage.setScene(scenes.get(sceneName));
    }
}
