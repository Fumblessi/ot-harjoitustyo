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
import java.util.*;
import java.io.FileInputStream;
import Hahmogeneraattori.dao.FileSettingsDao;
import Hahmogeneraattori.domain.Settings;
//import javafx.geometry.Insets;
import javafx.scene.paint.Color;

/**
 *
 * @author sampo
 */

public class Main extends Application {

    private Settings settings;
    private Scene generationScene;
    private Scene settingsScene;

    @Override
    public void init() throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));

        String settingsFile = properties.getProperty("settingsFile");
        FileSettingsDao settingsDao = new FileSettingsDao(settingsFile);
        this.settings = new Settings(settingsDao);
    }

    @Override
    public void start(Stage window) throws Exception {
        BorderPane layout = new BorderPane();
        layout.setPrefSize(600, 500);
        this.generationScene = new Scene(layout);
        //luodaan alkunäkymä

        VBox settingsLayout = new VBox();
        settingsLayout.setPrefSize(600, 500);
        this.settingsScene = new Scene(settingsLayout);
        //luodaan asetusnäkymä

        Button back = new Button("Tallenna");
        Button setDefault = new Button("Palauta alkuperäiset");
        HBox settingsButtons = new HBox();
        settingsButtons.getChildren().addAll(back, setDefault);
        //luodaan asetusnäkymästä paluupainike

        HBox statPool = new HBox();
        Label statPoolLabel = new Label("StatPool:");
        String initialStatPool = String.valueOf(this.settings.getStatPool());
        TextField statPoolAmount = new TextField(initialStatPool);
        Label statPoolError = new Label("");
        statPoolError.setTextFill(Color.RED);
        statPool.getChildren().addAll(statPoolLabel, statPoolAmount, statPoolError);

        HBox statLimits = new HBox();
        Label statMinLabel = new Label("StatMin:");
        String initialStatMin = String.valueOf(this.settings.getStatMin());
        TextField statMinAmount = new TextField(initialStatMin);
        Label statMaxLabel = new Label("StatMax:");
        String initialStatMax = String.valueOf(this.settings.getStatMax());
        TextField statMaxAmount = new TextField(initialStatMax);
        Label statLimitError = new Label("");
        statLimitError.setTextFill(Color.RED);
        statLimits.getChildren().addAll(statMinLabel, statMinAmount,
                statMaxLabel, statMaxAmount, statLimitError);

        settingsLayout.getChildren().addAll(settingsButtons, statPool, statLimits);

        HBox buttons = new HBox();
        Button generate = new Button("Generoi");
        Button settingsButton = new Button("Asetukset");
        buttons.getChildren().addAll(generate, settingsButton);
        layout.setTop(buttons);
        //luodaan alkunäkymän painikkeet

        generate.setOnAction((event) -> {
        });
        //generointinapin toiminnallisuus

        settingsButton.setOnAction((event) -> {
            window.setScene(this.settingsScene);
        });
        //asetukset -ikkunaan siirtyminen

        back.setOnAction((event) -> {
            int newStatPool = Integer.parseInt(statPoolAmount.getText());
            int newStatMin = Integer.parseInt(statMinAmount.getText());
            int newStatMax = Integer.parseInt(statMaxAmount.getText());
            
            if (newStatPool < 0 || newStatPool > 100) {
                statPoolError.setText("Valitse arvo väliltä 0-100!");
            } else if (newStatMin < 0 || newStatMin > newStatMax || 
                    6 * newStatMin > newStatPool || 6*newStatMax < newStatPool) {
                statLimitError.setText("Mahdottomat rajat!");
            } else {
                this.settings.setStatPool(newStatPool);
                this.settings.setStatMin(newStatMin);
                this.settings.setStatMax(newStatMax);
                statPoolError.setText("");
                statLimitError.setText("");
                window.setScene(this.generationScene);
            }
        });
        //asetukset-ikkunasta paluu

        setDefault.setOnAction((event) -> {
            statPoolAmount.setText("70");
            statMinAmount.setText("8");
            statMaxAmount.setText("18");
        });

        window.setTitle("Hahmogeneraattori");
        window.setScene(this.generationScene);
        window.show();
        //alkunäkymä
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        this.settings.update();
    }
}
