package com.github.graywizard123.tgbot.gui;

import com.github.graywizard123.tgbot.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SceneLoader {

    public Scene loadScene(String url) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        App.LOGGER.debug(url + " loading");
        fxmlLoader.setLocation(getClass().getResource(url));
        Parent root = fxmlLoader.load();
        App.LOGGER.debug(url + " loaded");
        return new Scene(root);
    }

    public HashMap<String, Scene> loadScenes() throws IOException {
        HashMap<String, Scene> scenes = new HashMap<>();
        List<String> sceneNames = getResourceFiles("/layouts/");

        for (String sceneName : sceneNames) {
            Scene scene = loadScene("/layouts/"+sceneName);
            scenes.put(sceneName.replace(".fxml", ""), scene);
        }

        return scenes;
    }

    private List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (
                InputStream in = getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
