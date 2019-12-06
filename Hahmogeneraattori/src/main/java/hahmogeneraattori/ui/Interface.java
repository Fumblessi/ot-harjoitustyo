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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.FileInputStream;
import hahmogeneraattori.dao.FileSettingsDao;
import hahmogeneraattori.dao.GeneratorDatabaseDao;
import hahmogeneraattori.dao.SQLGeneratorDatabaseDao;
import hahmogeneraattori.domain.Settings;
import hahmogeneraattori.domain.Generator;
import hahmogeneraattori.domain.Proficiency;
import java.sql.*;
import javafx.scene.paint.Color;

/**
 *
 * @author sampo
 */
public class Interface extends Application {

    private Settings settings;
    private GeneratorDatabaseDao generatorDatabaseDao;
    private Generator generator;
    private Scene startScene;
    private Scene settingsScene;
    private Scene addingScene;
    private Scene profAddScene;

    @Override
    public void init() throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));

        String settingsFile = properties.getProperty("settingsFile");
        FileSettingsDao settingsDao = new FileSettingsDao(settingsFile);
        this.generatorDatabaseDao = new SQLGeneratorDatabaseDao();
        this.settings = new Settings(settingsDao, this.generatorDatabaseDao);
        this.generator = new Generator(this.settings);
    }

    @Override
    public void start(Stage window) throws Exception {
        //initializeDatabase();
        //alusta tarvittaessa tietokanta
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
        Button add = new Button("Lisää tietokantaan");
        HBox settingsButtons = new HBox();
        settingsButtons.getChildren().addAll(back, setDefault, add);
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
            statPoolError.setText("");
            statLimitError.setText("");
            
            if (!isInteger(statPoolAmount.getText())
                    || !isInteger(statPoolVarAmount.getText())
                    || !isInteger(statMinAmount.getText())
                    || !isInteger(statMaxAmount.getText())) {
                if (!isInteger(statPoolAmount.getText())
                        || !isInteger(statPoolVarAmount.getText())) {
                    statPoolError.setText("Kirjoita syöte kokonaislukuna!");
                }
                if (!isInteger(statMinAmount.getText())
                        || !isInteger(statMaxAmount.getText())) {
                    statLimitError.setText("Kirjoita syötä kokonaislukuna!");
                }
            } else {
                int newStatPool = Integer.parseInt(statPoolAmount.getText());
                int newStatVar = Integer.parseInt(statPoolVarAmount.getText());
                int newStatMin = Integer.parseInt(statMinAmount.getText());
                int newStatMax = Integer.parseInt(statMaxAmount.getText());
                boolean newRacialBonus = racialBonus.isSelected();

                if (newStatPool < 0 || newStatPool > 100 || newStatMin < 0
                        || newStatMin > newStatMax || 6 * newStatMin
                        > (newStatPool - newStatVar) || 6 * newStatMax
                        < (newStatPool + newStatVar)) {
                    if (newStatPool < 0 || newStatPool > 100) {
                        statPoolError.setText("Valitse arvo väliltä 0-100!");
                    }
                    if (newStatMin < 0 || newStatMax < 0) {
                        statLimitError.setText("Valitse epänegatiiviset arvot!");
                    } else if (newStatMin > newStatMax
                            || 6 * newStatMin > (newStatPool - newStatVar)
                            || 6 * newStatMax < (newStatPool + newStatVar)) {
                        statLimitError.setText("Mahdottomat rajat!");
                    }
                } else {
                    this.settings.setStatPool(newStatPool);
                    this.settings.setStatVar(newStatVar);
                    this.settings.setStatMin(newStatMin);
                    this.settings.setStatMax(newStatMax);
                    this.settings.setRacialBonus(newRacialBonus);
                    this.generator.getNewSettings(this.settings);
                    window.setScene(this.startScene);
                }
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

        VBox addingLayout = new VBox();
        addingLayout.setPrefSize(170, 170);

        Button addProf = new Button("Proficiency");
        Button addRacial = new Button("Racial");
        Button addClass = new Button("Class");
        Button addBg = new Button("Background");
        Button addFeat = new Button("Feat");
        addingLayout.getChildren().addAll(addProf, addRacial, addClass, addBg, addFeat);

        Stage addingWindow = new Stage();
        this.addingScene = new Scene(addingLayout);
        addingWindow.setTitle("Lisää...");
        addingWindow.setScene(this.addingScene);

        add.setOnAction((event) -> {
            addingWindow.show();
        });

        VBox profAddLayout = new VBox();

        HBox profNameLayout = new HBox();
        Label profNameLabel = new Label("Nimi: ");
        TextField profNameText = new TextField();
        profNameLayout.getChildren().addAll(profNameLabel, profNameText);

        HBox profTypeLayout = new HBox();
        ToggleGroup profGroup = new ToggleGroup();
        RadioButton typeSkill = new RadioButton("Skill");
        typeSkill.setToggleGroup(profGroup);
        typeSkill.setSelected(true);
        RadioButton typeArmor = new RadioButton("Armor");
        typeArmor.setToggleGroup(profGroup);
        RadioButton typeWeapon = new RadioButton("Weapon");
        typeWeapon.setToggleGroup(profGroup);
        RadioButton typeTool = new RadioButton("Tool");
        typeTool.setToggleGroup(profGroup);
        RadioButton typeLanguage = new RadioButton("Language");
        typeLanguage.setToggleGroup(profGroup);
        profTypeLayout.getChildren().addAll(typeSkill, typeArmor, typeWeapon,
                typeTool, typeLanguage);

        Button addNewProf = new Button("Lisää");

        profAddLayout.getChildren().addAll(profNameLayout, profTypeLayout, addNewProf);

        Stage profAddWindow = new Stage();
        this.profAddScene = new Scene(profAddLayout);
        profAddWindow.setTitle("Lisää Proficiency");
        profAddWindow.setScene(this.profAddScene);

        addProf.setOnAction((event) -> {
            profAddWindow.show();
            addingWindow.close();
        });

        addNewProf.setOnAction((event) -> {
            profAddWindow.close();
            String profName = profNameText.getText();
            profNameText.setText("");
            String profType = "";
            if (typeLanguage.isSelected()) {
                profType = "Language";
            } else if (typeArmor.isSelected()) {
                profType = "Armor";
            } else if (typeWeapon.isSelected()) {
                profType = "Weapon";
            } else if (typeTool.isSelected()) {
                profType = "Tool";
            } else {
                profType = "Skill";
            }
            try {
                this.settings.addNewProfToDB(new Proficiency(profName, profType));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        Label stats = new Label("");
        Label proficiencies = new Label("");
        HBox characterAttributes = new HBox();
        characterAttributes.getChildren().addAll(stats, proficiencies);
        layout.setCenter(characterAttributes);

        generate.setOnAction((event) -> {
            this.generator.generate();
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

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void initializeDatabase() {
        //mikäli tietokanta on poistettu tai se halutaan alustaa
        //kokonaan uudestaan, voi ajaa tämän metodin, jolla
        //tietokanta luodaan uudestaan

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./generatordb", "sa", "")) {
            //poistetaan vanhat taulut
            conn.prepareStatement("DROP TABLE Proficiency IF EXISTS").executeUpdate();
            conn.prepareStatement("DROP TABLE Racial IF EXISTS").executeUpdate();
            conn.prepareStatement("DROP TABLE RacialProficiency IF EXISTS").executeUpdate();
            conn.prepareStatement("DROP TABLE Background IF EXISTS").executeUpdate();
            conn.prepareStatement("DROP TABLE BackgroundProficiency IF EXISTS").executeUpdate();
            conn.prepareStatement("DROP TABLE Class IF EXISTS").executeUpdate();
            conn.prepareStatement("DROP TABLE SubClass IF EXISTS").executeUpdate();
            conn.prepareStatement("DROP TABLE ClassProficiency IF EXISTS").executeUpdate();
            conn.prepareStatement("DROP TABLE Feat IF EXISTS").executeUpdate();
            conn.prepareStatement("DROP TABLE FeatProficiency IF EXISTS").executeUpdate();
            //luodaan uudet taulut
            conn.prepareStatement("CREATE TABLE Proficiency(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                    + "type VARCHAR(255), PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
            conn.prepareStatement("CREATE TABLE Racial(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                    + "PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
            conn.prepareStatement("CREATE TABLE RacialProficiency(racial_id INTEGER, prof_id INTEGER, "
                    + "FOREIGN KEY (racial_id) REFERENCES Racial(id), FOREIGN KEY (prof_id) "
                    + "REFERENCES Proficiency(id));").executeUpdate();
            conn.prepareStatement("CREATE TABLE Background(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                    + "PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
            conn.prepareStatement("CREATE TABLE BackgroundProficiency(bg_id INTEGER, prof_id INTEGER, "
                    + "FOREIGN KEY (bg_id) REFERENCES Background(id), FOREIGN KEY (prof_id) "
                    + "REFERENCES Proficiency(id));").executeUpdate();
            conn.prepareStatement("CREATE TABLE Class(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                    + "PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
            conn.prepareStatement("CREATE TABLE SubClass(id INTEGER AUTO_INCREMENT, class_id INTEGER, "
                    + "name VARCHAR(255), PRIMARY KEY (id), UNIQUE KEY (id), FOREIGN KEY "
                    + "(class_id) REFERENCES Class(id));").executeUpdate();
            conn.prepareStatement("CREATE TABLE ClassProficiency(class_id INTEGER, prof_id INTEGER, "
                    + "FOREIGN KEY (class_id) REFERENCES Class(id), FOREIGN KEY (prof_id) "
                    + "REFERENCES Proficiency(id));").executeUpdate();
            conn.prepareStatement("CREATE TABLE Feat(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                    + "stats VARCHAR(255), PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
            conn.prepareStatement("CREATE TABLE FeatProficiency(feat_id INTEGER, prof_id INTEGER, "
                    + "FOREIGN KEY (feat_id) REFERENCES Feat(id), FOREIGN KEY (prof_id) "
                    + "REFERENCES Proficiency(id));").executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
