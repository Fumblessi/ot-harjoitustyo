/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hahmogeneraattori.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.Scene;
//import javafx.geometry.Insets;
//import javafx.scene.paint.Color;
/**
 *
 * @author sampo
 */

public class Main extends Application {
    
    @Override
    public void start(Stage window) throws Exception {
        BorderPane layout = new BorderPane();        
        layout.setPrefSize(600, 500);
        Scene generationScene = new Scene(layout);
        //luodaan alkunäkymä
        
        VBox settingsLayout = new VBox();
        settingsLayout.setPrefSize(600, 500);    
        Scene settingsScene = new Scene(settingsLayout);
        //luodaan asetusnäkymä
        
        Button back = new Button("Takaisin");
        settingsLayout.getChildren().add(back);
        //luodaan asetusnäkymästä paluupainike

        HBox buttons = new HBox();
        Button generate = new Button("Generoi");
        Button settings = new Button("Asetukset");        
        buttons.getChildren().addAll(generate, settings);        
        layout.setTop(buttons);
        //luodaan alkunäkymän painikkeet

        generate.setOnAction((event) -> {});
        //generointinapin toiminnallisuus
        
        settings.setOnAction((event) -> {
            window.setScene(settingsScene);
        });
        //asetukset -ikkunaan siirtyminen
        
        back.setOnAction((event) -> {
            window.setScene(generationScene); 
        });
        //asetukset-ikkunasta paluu
        
        window.setTitle("Hahmogeneraattori");
        window.setScene(generationScene);
        window.show();
        //alkunäkymä
    }
    
    public static void main(String[] args) {
        launch(Main.class);
    }
    
}


