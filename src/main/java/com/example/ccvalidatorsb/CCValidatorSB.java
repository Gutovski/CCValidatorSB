package com.example.ccvalidatorsb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class CCValidatorSB extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CCValidatorSB.class.getResource("CCValidatorSB.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 280, 160);
        stage.setTitle("Credit Card Validator (SceneBuilder)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}