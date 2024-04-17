package com.scottlandyard;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Deque;

public class mainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("mainGameWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1020);
        stage.setTitle("Scottland Yard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

//        launch();
        StringBuilder imba = new StringBuilder();
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                for (int x = 1; x < 10; x++) {
                    imba.append('"').append("pow").append(i).append(j).append(x).append('"').append(", ");
                }
            }
        }
        System.out.println(imba.toString());
    }
}