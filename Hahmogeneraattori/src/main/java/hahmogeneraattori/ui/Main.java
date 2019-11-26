/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.Scene;
import java.util.Properties;
import java.io.FileInputStream;
import hahmogeneraattori.dao.FileSettingsDao;
import hahmogeneraattori.domain.Settings;
import hahmogeneraattori.domain.Generator;
import hahmogeneraattori.domain.RPGCharacter;
//import javafx.geometry.Insets;
import javafx.scene.paint.Color;

/**
 *
 * @author sampo
 */

public class Main extends Application {

    private Settings settings;
    private Generator generator;
    private RPGCharacter character;
    private Scene startScene;
    private Scene settingsScene;
    private Scene instructionScene;
    private Scene generationScene;

    @Override
    public void init() throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));

        String settingsFile = properties.getProperty("settingsFile");
        FileSettingsDao settingsDao = new FileSettingsDao(settingsFile);
        this.settings = new Settings(settingsDao);
        this.generator = new Generator(this.settings);
    }

    @Override
    public void start(Stage window) throws Exception {
        BorderPane layout = new BorderPane();
        layout.setPrefSize(300, 200);
        this.startScene = new Scene(layout);
        //luodaan alkunäkymä

        VBox settingsLayout = new VBox();
        settingsLayout.setPrefSize(600, 400);
        this.settingsScene = new Scene(settingsLayout);
        //luodaan asetusnäkymä

        Button back = new Button("Tallenna ja palaa");
        Button setDefault = new Button("Palauta alkuperäiset");
        Button instructions = new Button("Ohje");
        HBox settingsButtons = new HBox();
        settingsButtons.getChildren().addAll(back, setDefault, instructions);
        //luodaan asetusnäkymästä paluupainike

        HBox statPool = new HBox();
        Label statPoolLabel = new Label("Piirteiden summa:");
        String initialStatPool = String.valueOf(this.settings.getStatPool());
        TextField statPoolAmount = new TextField(initialStatPool);
        Label statPoolVariance = new Label("Varianssi: +-");
        String initialStatVar = String.valueOf(this.settings.getStatVar());
        TextField statPoolVarAmount = new TextField(initialStatVar);
        Label statPoolError = new Label("");
        statPoolError.setTextFill(Color.RED);
        statPool.getChildren().addAll(statPoolLabel, statPoolAmount, 
                statPoolVariance, statPoolVarAmount);

        HBox statLimits = new HBox();
        Label statMinLabel = new Label("Piirreminimi:");
        String initialStatMin = String.valueOf(this.settings.getStatMin());
        TextField statMinAmount = new TextField(initialStatMin);
        Label statMaxLabel = new Label("Piirremaksimi:");
        String initialStatMax = String.valueOf(this.settings.getStatMax());
        TextField statMaxAmount = new TextField(initialStatMax);
        Label statLimitError = new Label("");
        statLimitError.setTextFill(Color.RED);
        statLimits.getChildren().addAll(statMinLabel, statMinAmount,
                statMaxLabel, statMaxAmount);
        
        CheckBox racialBonus = new CheckBox("Lisää rotubonukset");
        racialBonus.setSelected(this.settings.getRacialBonus());

        settingsLayout.getChildren().addAll(settingsButtons, statPool, statPoolError, 
                statLimits, statLimitError, racialBonus);

        HBox buttons = new HBox();
        Button generate = new Button("Generoi");
        Button settingsButton = new Button("Asetukset");
        buttons.getChildren().addAll(generate, settingsButton);
        layout.setTop(buttons);
        //luodaan alkunäkymän painikkeet

        settingsButton.setOnAction((event) -> {
            window.setScene(this.settingsScene);
        });
        //asetukset -ikkunaan siirtyminen

        back.setOnAction((event) -> {
            int newStatPool = Integer.parseInt(statPoolAmount.getText());
            int newStatVar = Integer.parseInt(statPoolVarAmount.getText());
            int newStatMin = Integer.parseInt(statMinAmount.getText());
            int newStatMax = Integer.parseInt(statMaxAmount.getText());
            boolean newRacialBonus = racialBonus.isSelected();
            
            if (newStatPool < 0 || newStatPool > 100) {
                statPoolError.setText("Valitse arvo väliltä 0-100!");
            } else if (newStatMin < 0 || newStatMin > newStatMax || 
                    6 * newStatMin > (newStatPool - newStatVar) || 
                    6*newStatMax < (newStatPool + newStatVar)) {
                statLimitError.setText("Mahdottomat rajat!");
            } else {
                this.settings.setStatPool(newStatPool);
                this.settings.setStatVar(newStatVar);
                this.settings.setStatMin(newStatMin);
                this.settings.setStatMax(newStatMax);
                this.settings.setRacialBonus(newRacialBonus);
                this.generator.getNewSettings(this.settings);
                statPoolError.setText("");
                statLimitError.setText("");
                window.setScene(this.startScene);
            }
        });
        //asetukset-ikkunasta paluu

        setDefault.setOnAction((event) -> {
            statPoolAmount.setText("70");
            statPoolVarAmount.setText("5");
            statMinAmount.setText("8");
            statMaxAmount.setText("18");
            racialBonus.setSelected(true);
        });
        
        BorderPane instructionLayout = new BorderPane();
        Label instructionText = new Label(" - Valitse StatPoolin* arvo väliltä 0-100."
                + "\n - Valitse generoitavan hahmon stateille sopiva** minimi ja maksimi."
                + "\n\n *StatPool on generoitavan hahmojen stattien (Strength,"
                + "Dexterity, Constitution,\n   Intelligence, Wisdom ja Charisma) summa."
                + "\n **StatMin täytyy valita niin, että StatPoolista riittää pisteitä"
                + " vähintään arvon\n    StatMin verran jokaiseen stattiin, ja "
                + "vastaavasti StatMax niin, ettei\n    StatPooliin jää ylimääräisiä "
                + "pisteitä stattien arpomisen jälkeen.");
        instructionLayout.setCenter(instructionText);
        Stage instructionWindow = new Stage();
        this.instructionScene = new Scene(instructionLayout);
        instructionWindow.setTitle("Ohje");
        instructionWindow.setScene(instructionScene);      
        
        instructions.setOnAction((event) -> {
            instructionWindow.show();    
        });
        
        Label stats = new Label("");
        Label proficiencies = new Label("");
        HBox characterAttributes = new HBox();
        characterAttributes.getChildren().addAll(stats, proficiencies);
        layout.setCenter(characterAttributes);
        
        generate.setOnAction((event) -> {
            this.generator.generate();
            this.character = this.generator.getCharacter();
            stats.setText(this.generator.generateStatList());
        });

        window.setTitle("Hahmogeneraattori");
        window.setScene(this.startScene);
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
