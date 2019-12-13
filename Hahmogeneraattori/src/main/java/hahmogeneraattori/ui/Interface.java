/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import hahmogeneraattori.dao.FileSettingsDao;
import hahmogeneraattori.dao.GeneratorDatabaseDao;
import hahmogeneraattori.dao.SQLGeneratorDatabaseDao;
import hahmogeneraattori.domain.*;
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

    private Stage primaryWindow;
    private Stage databaseWindow;
    private Stage modifyWindow;

    private Scene startScene;
    private Scene settingsScene;
    private Scene raceDatabaseScene;
    private Scene profDatabaseScene;
    private Scene racialDatabaseScene;
    private Scene classDatabaseScene;
    private Scene bgDatabaseScene;
    private Scene featDatabaseScene;
    private Scene raceAddScene;
    private Scene raceModScene;
    private Scene profAddScene;
    private Scene profModScene;
    private Scene racialAddScene;
    private Scene racialModScene;
    private Scene classAddScene;
    private Scene classModScene;
    private Scene bgAddScene;
    private Scene bgModScene;
    private Scene featAddScene;
    private Scene featModScene;

    @Override
    public void init() throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));

        String settingsFile = properties.getProperty("settingsFile");
        FileSettingsDao settingsDao = new FileSettingsDao(settingsFile);

        String connectionPath = properties.getProperty("spring.datasource.url");
        String username = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");

        this.generatorDatabaseDao = new SQLGeneratorDatabaseDao(connectionPath,
                username, password);
        this.settings = new Settings(settingsDao);
        this.generator = new Generator(this.settings, this.generatorDatabaseDao);

        //this.generator.initializeDatabase();
        //alusta tarvittaessa tietokanta
    }

    @Override
    public void start(Stage window) {
        this.primaryWindow = window;
        this.modifyWindow = new Stage();
        BorderPane layout = new BorderPane();
        layout.setPrefSize(300, 200);
        this.startScene = new Scene(layout);

        HBox buttons = new HBox();
        Button generate = new Button("Generoi");
        Button settingsButton = new Button("Asetukset");
        buttons.getChildren().addAll(generate, settingsButton);
        layout.setTop(buttons);

        VBox settingsLayout = new VBox();
        settingsLayout.setPrefSize(600, 400);
        this.settingsScene = new Scene(settingsLayout);

        Button back = new Button("Tallenna ja palaa");
        Button setDefault = new Button("Palauta alkuperäiset");
        MenuBar databaseBar = new MenuBar();
        Menu database = new Menu("Tietokanta");
        databaseBar.getMenus().add(database);

        MenuItem modifyRace = new MenuItem("Race");
        MenuItem modifyProf = new MenuItem("Proficiency");
        MenuItem modifyRacial = new MenuItem("Racial");
        MenuItem modifyClass = new MenuItem("Class");
        MenuItem modifyBg = new MenuItem("Background");
        MenuItem modifyFeat = new MenuItem("Feat");
        database.getItems().addAll(modifyRace, modifyProf, modifyRacial, 
                modifyClass, modifyBg, modifyFeat);

        HBox settingsButtons = new HBox();
        settingsButtons.getChildren().addAll(back, setDefault, databaseBar);

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

        settingsButton.setOnAction((event) -> {
            settingsLayout.getChildren().addAll(settingsButtons, statPool, statPoolError,
                    statLimits, statLimitError, racialBonus);
            this.primaryWindow.setScene(this.settingsScene);
        });

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
                        < (newStatPool + newStatVar) || newStatVar < 0) {
                    if (newStatPool < 0 || newStatPool > 100 || newStatVar < 0) {
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
                    settingsLayout.getChildren().clear();
                    this.databaseWindow.close();
                    this.primaryWindow.setScene(this.startScene);
                }
            }
        });

        setDefault.setOnAction((event) -> {
            statPoolAmount.setText("70");
            statPoolVarAmount.setText("5");
            statMinAmount.setText("8");
            statMaxAmount.setText("18");
            racialBonus.setSelected(true);
        });

        this.databaseWindow = new Stage();
        this.databaseWindow.setTitle("Tietokanta");
        
        VBox raceAddLayout = new VBox();

        HBox raceAddNameLayout = new HBox();
        Label raceAddNameLabel = new Label("Nimi: ");
        TextField raceAddNameText = new TextField();
        Label raceAddNameError = new Label("");
        raceAddNameError.setTextFill(Color.RED);
        raceAddNameLayout.getChildren().addAll(raceAddNameLabel, raceAddNameText);
        
        Button addNewRace = new Button("Lisää");
        Button backFromAddingRace = new Button("Takaisin");
        HBox raceAddingButtons = new HBox();
        raceAddingButtons.getChildren().addAll(addNewRace, backFromAddingRace);
        raceAddLayout.getChildren().addAll(raceAddNameLayout, raceAddNameError,
                raceAddingButtons);
        
        VBox raceModifyLayout = new VBox();
        
        HBox raceModNameLayout = new HBox();
        Label raceModNameLabel = new Label("Nimi: ");
        TextField raceModNameText = new TextField();
        Label raceModNameError = new Label("");
        raceModNameError.setTextFill(Color.RED);
        raceModNameLayout.getChildren().addAll(raceModNameLabel, raceModNameText);
        
        Button modifyThisRace = new Button("Päivitä");
        Button backFromModifyingRace = new Button("Takaisin");
        HBox raceModifyingButtons = new HBox();
        raceModifyingButtons.getChildren().addAll(modifyThisRace, backFromModifyingRace);
        raceModifyLayout.getChildren().addAll(raceModNameLayout, raceModNameError,
                raceModifyingButtons);
        
        VBox raceDatabaseLayout = new VBox();
        
        TableView<Race> races = new TableView();

        TableColumn<Race, String> raceNameColumn = new TableColumn<>("Nimi");
        raceNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        raceNameColumn.prefWidthProperty().bind(races.widthProperty());

        races.getSortOrder().add(raceNameColumn);
        races.getColumns().setAll(raceNameColumn);

        Button addRace = new Button("Lisää uusi");
        Button modifyExistingRace = new Button("Muokkaa");
        Button deleteRace = new Button("Poista");
        Button backFromRace = new Button("Takaisin");
        HBox raceDbButtons = new HBox();
        raceDbButtons.getChildren().addAll(addRace, modifyExistingRace, deleteRace,
                backFromRace);

        Label raceDatabaseErrorText = new Label("");
        raceDatabaseErrorText.setTextFill(Color.RED);

        raceDatabaseLayout.getChildren().addAll(races, raceDbButtons, raceDatabaseErrorText);
        this.raceDatabaseScene = new Scene(raceDatabaseLayout);

        modifyRace.setOnAction((event) -> {
            refreshRaces(races);
            this.databaseWindow.setScene(this.raceDatabaseScene);
            this.databaseWindow.show();
        });

        backFromRace.setOnAction((event) -> {
            raceDatabaseErrorText.setText("");
            this.databaseWindow.close();
        });
        
        this.raceAddScene = new Scene(raceAddLayout);
        this.raceModScene = new Scene(raceModifyLayout);

        addRace.setOnAction((event) -> {
            raceAddNameText.setText("");
            raceAddNameError.setText("");
            raceDatabaseErrorText.setText("");
            this.modifyWindow.setTitle("Lisää Race");
            this.modifyWindow.setScene(this.raceAddScene);
            this.modifyWindow.show();
        });

        modifyExistingRace.setOnAction((event) -> {
            Race raceToBeModified = races.getSelectionModel().getSelectedItem();
            
            if (!(raceToBeModified == null)) {
                raceDatabaseErrorText.setText("");
                this.modifyWindow.setTitle("Muokkaa Racea");
                this.modifyWindow.setScene(this.raceModScene);
                this.modifyWindow.show();
                raceModNameText.setText(raceToBeModified.getName());                
            } else {
                raceDatabaseErrorText.setText("Valitse muokattava race!");
            }
        });

        addNewRace.setOnAction((event) -> {
            String raceName = raceAddNameText.getText();
            
            if (!raceName.isEmpty()) {
                try {
                    this.generator.addNewRaceToDb(new Race(raceName));
                } catch (SQLException e) {
                    raceAddNameError.setText(e.getMessage());
                }
                raceDatabaseErrorText.setText("");
                raceAddNameText.setText("");
                raceAddNameError.setText("");
                this.modifyWindow.close();
                refreshRaces(races);
            } else {
                if (raceName.isEmpty()) {
                    raceAddNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    raceAddNameError.setText("");
                }
            }
        });

        backFromAddingRace.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        modifyThisRace.setOnAction((event) -> {
            String raceName = raceModNameText.getText();

            if (!raceName.isEmpty()) {
                int id = races.getSelectionModel().getSelectedItem().getId();

                Race raceToMod = new Race(id, raceName);
                try {
                    this.generator.updateRaceToDb(raceToMod);
                } catch (SQLException ex) {
                    raceModNameError.setText(ex.getMessage());
                }
                raceModNameText.setText("");
                raceModNameError.setText("");
                this.modifyWindow.close();
                refreshRaces(races);
            } else {
                if (raceName.isEmpty()) {
                    raceModNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    raceModNameError.setText("");
                }
            }
        });

        backFromModifyingRace.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        deleteRace.setOnAction((event) -> {
            deleteRace(races, raceDatabaseErrorText);
        });

        VBox profDatabaseLayout = new VBox();
        profDatabaseLayout.setPrefSize(500, 500);
        TableView<Proficiency> profs = new TableView();

        TableColumn<Proficiency, String> profNameColumn = new TableColumn<>("Nimi");
        profNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        profNameColumn.prefWidthProperty().bind(profs.widthProperty().multiply(0.33));

        TableColumn<Proficiency, String> profTypeColumn = new TableColumn<>("Tyyppi");
        profTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        profTypeColumn.prefWidthProperty().bind(profs.widthProperty().multiply(0.33));

        TableColumn<Proficiency, String> profSubtypeColumn = new TableColumn<>("Alityyppi");
        profSubtypeColumn.setCellValueFactory(new PropertyValueFactory<>("subtype"));
        profSubtypeColumn.prefWidthProperty().bind(profs.widthProperty().multiply(0.33));

        profs.getSortOrder().add(profTypeColumn);
        profs.getColumns().setAll(profNameColumn, profTypeColumn, profSubtypeColumn);

        Button addProf = new Button("Lisää uusi");
        Button modifyExistingProf = new Button("Muokkaa");
        Button deleteProf = new Button("Poista");
        Button backFromProf = new Button("Takaisin");
        HBox profDbButtons = new HBox();
        profDbButtons.getChildren().addAll(addProf, modifyExistingProf, deleteProf,
                backFromProf);
        
        Label profDatabaseErrorText = new Label("");
        profDatabaseErrorText.setTextFill(Color.RED);

        profDatabaseLayout.getChildren().addAll(profs, profDbButtons, profDatabaseErrorText);
        this.profDatabaseScene = new Scene(profDatabaseLayout);
        
        VBox profAddLayout = new VBox();
        profAddLayout.setPrefSize(400, 200);

        HBox profAddNameLayout = new HBox();
        Label profAddNameLabel = new Label("Nimi: ");
        TextField profAddNameText = new TextField();
        Label profAddNameError = new Label("");
        profAddNameError.setTextFill(Color.RED);
        profAddNameLayout.getChildren().addAll(profAddNameLabel, profAddNameText);

        HBox profAddTypeLayout = new HBox();
        ToggleGroup profAddGroup = new ToggleGroup();
        RadioButton addTypeSkill = new RadioButton("Skill");
        addTypeSkill.setToggleGroup(profAddGroup);
        addTypeSkill.setSelected(true);
        RadioButton addTypeArmor = new RadioButton("Armor");
        addTypeArmor.setToggleGroup(profAddGroup);
        RadioButton addTypeWeapon = new RadioButton("Weapon");
        addTypeWeapon.setToggleGroup(profAddGroup);
        RadioButton addTypeTool = new RadioButton("Tool");
        addTypeTool.setToggleGroup(profAddGroup);
        RadioButton addTypeLanguage = new RadioButton("Language");
        addTypeLanguage.setToggleGroup(profAddGroup);
        RadioButton addTypeSave = new RadioButton("Save");
        addTypeSave.setToggleGroup(profAddGroup);
        profAddTypeLayout.getChildren().addAll(addTypeSkill, addTypeArmor, addTypeWeapon,
                addTypeTool, addTypeLanguage, addTypeSave);

        HBox profAddSubtypeToolLayout = new HBox();
        ToggleGroup profAddSubtypeToolGroup = new ToggleGroup();
        RadioButton addSubtypeArtisan = new RadioButton("Artisan");
        addSubtypeArtisan.setSelected(true);
        addSubtypeArtisan.setToggleGroup(profAddSubtypeToolGroup);
        RadioButton addSubtypeGamingSet = new RadioButton("Gaming Set");
        addSubtypeGamingSet.setToggleGroup(profAddSubtypeToolGroup);
        RadioButton addSubtypeInstrument = new RadioButton("Instrument");
        addSubtypeInstrument.setToggleGroup(profAddSubtypeToolGroup);
        RadioButton addSubtypeOther = new RadioButton("Other");
        addSubtypeOther.setToggleGroup(profAddSubtypeToolGroup);
        profAddSubtypeToolLayout.getChildren().addAll(addSubtypeArtisan,
                addSubtypeGamingSet, addSubtypeInstrument, addSubtypeOther);

        HBox profAddSubtypeWeaponLayout = new HBox();
        ToggleGroup profAddSubtypeWeaponGroup = new ToggleGroup();
        RadioButton addSubtypeSimple = new RadioButton("Simple");
        addSubtypeSimple.setSelected(true);
        addSubtypeSimple.setToggleGroup(profAddSubtypeWeaponGroup);
        RadioButton addSubtypeMartial = new RadioButton("Martial");
        addSubtypeMartial.setToggleGroup(profAddSubtypeWeaponGroup);
        profAddSubtypeWeaponLayout.getChildren().addAll(addSubtypeSimple,
                addSubtypeMartial);

        HBox profAddSubtypeLanguageLayout = new HBox();
        ToggleGroup profAddSubtypeLanguageGroup = new ToggleGroup();
        RadioButton addSubtypeCommon = new RadioButton("Common");
        addSubtypeCommon.setSelected(true);
        addSubtypeCommon.setToggleGroup(profAddSubtypeLanguageGroup);
        RadioButton addSubtypeRare = new RadioButton("Rare");
        addSubtypeRare.setToggleGroup(profAddSubtypeLanguageGroup);
        RadioButton addSubtypeLegendary = new RadioButton("Legendary");
        addSubtypeLegendary.setToggleGroup(profAddSubtypeLanguageGroup);
        profAddSubtypeLanguageLayout.getChildren().addAll(addSubtypeCommon,
                addSubtypeRare, addSubtypeLegendary);
        
        Button addNewProf = new Button("Lisää");
        Button backFromAddingProf = new Button("Takaisin");
        HBox profAddingButtons = new HBox();
        profAddingButtons.getChildren().addAll(addNewProf, backFromAddingProf);
        profAddLayout.getChildren().addAll(profAddNameLayout, profAddNameError,
                profAddTypeLayout, profAddingButtons);
        
        addTypeSkill.setOnAction((event) -> {
            if (addTypeSkill.isSelected()) {
                profAddLayout.getChildren().clear();
                profAddLayout.getChildren().addAll(profAddNameLayout, profAddNameError,
                        profAddTypeLayout, profAddingButtons);
            }
        });

        addTypeArmor.setOnAction((event) -> {
            if (addTypeArmor.isSelected()) {
                profAddLayout.getChildren().clear();
                profAddLayout.getChildren().addAll(profAddNameLayout, profAddNameError,
                        profAddTypeLayout, profAddingButtons);
            }
        });

        addTypeWeapon.setOnAction((event) -> {
            if (addTypeWeapon.isSelected()) {
                profAddLayout.getChildren().clear();
                profAddLayout.getChildren().addAll(profAddNameLayout, profAddNameError,
                        profAddTypeLayout, profAddSubtypeWeaponLayout, profAddingButtons);
            }
        });

        addTypeTool.setOnAction((event) -> {
            if (addTypeTool.isSelected()) {
                profAddLayout.getChildren().clear();
                profAddLayout.getChildren().addAll(profAddNameLayout, profAddNameError,
                        profAddTypeLayout, profAddSubtypeToolLayout, profAddingButtons);
            }
        });

        addTypeLanguage.setOnAction((event) -> {
            if (addTypeLanguage.isSelected()) {
                profAddLayout.getChildren().clear();
                profAddLayout.getChildren().addAll(profAddNameLayout, profAddNameError,
                        profAddTypeLayout, profAddSubtypeLanguageLayout, profAddingButtons);
            }
        });
        
        addTypeSave.setOnAction((event) -> {
            if (addTypeSave.isSelected()) {
                profAddLayout.getChildren().clear();
                profAddLayout.getChildren().addAll(profAddNameLayout, profAddNameError,
                        profAddTypeLayout, profAddingButtons);
            }
        });
        
        VBox profModifyLayout = new VBox();
        profModifyLayout.setPrefSize(400, 200);
        
        HBox profModNameLayout = new HBox();
        Label profModNameLabel = new Label("Nimi: ");
        TextField profModNameText = new TextField();
        Label profModNameError = new Label("");
        profModNameError.setTextFill(Color.RED);
        profModNameLayout.getChildren().addAll(profModNameLabel, profModNameText);

        HBox profModTypeLayout = new HBox();
        ToggleGroup profModGroup = new ToggleGroup();
        RadioButton modTypeSkill = new RadioButton("Skill");
        modTypeSkill.setToggleGroup(profModGroup);
        RadioButton modTypeArmor = new RadioButton("Armor");
        modTypeArmor.setToggleGroup(profModGroup);
        RadioButton modTypeWeapon = new RadioButton("Weapon");
        modTypeWeapon.setToggleGroup(profModGroup);
        RadioButton modTypeTool = new RadioButton("Tool");
        modTypeTool.setToggleGroup(profModGroup);
        RadioButton modTypeLanguage = new RadioButton("Language");
        modTypeLanguage.setToggleGroup(profModGroup);
        RadioButton modTypeSave = new RadioButton("Save");
        modTypeSave.setToggleGroup(profModGroup);
        profModTypeLayout.getChildren().addAll(modTypeSkill, modTypeArmor, modTypeWeapon,
                modTypeTool, modTypeLanguage, modTypeSave);

        HBox profModSubtypeToolLayout = new HBox();
        ToggleGroup profModSubtypeToolGroup = new ToggleGroup();
        RadioButton modSubtypeArtisan = new RadioButton("Artisan");
        modSubtypeArtisan.setSelected(true);
        modSubtypeArtisan.setToggleGroup(profModSubtypeToolGroup);
        RadioButton modSubtypeGamingSet = new RadioButton("Gaming Set");
        modSubtypeGamingSet.setToggleGroup(profModSubtypeToolGroup);
        RadioButton modSubtypeInstrument = new RadioButton("Instrument");
        modSubtypeInstrument.setToggleGroup(profModSubtypeToolGroup);
        RadioButton modSubtypeOther = new RadioButton("Other");
        modSubtypeOther.setToggleGroup(profModSubtypeToolGroup);
        profModSubtypeToolLayout.getChildren().addAll(modSubtypeArtisan,
                modSubtypeGamingSet, modSubtypeInstrument, modSubtypeOther);

        HBox profModSubtypeWeaponLayout = new HBox();
        ToggleGroup profModSubtypeWeaponGroup = new ToggleGroup();
        RadioButton modSubtypeSimple = new RadioButton("Simple");
        modSubtypeSimple.setSelected(true);
        modSubtypeSimple.setToggleGroup(profModSubtypeWeaponGroup);
        RadioButton modSubtypeMartial = new RadioButton("Martial");
        modSubtypeMartial.setToggleGroup(profModSubtypeWeaponGroup);
        profModSubtypeWeaponLayout.getChildren().addAll(modSubtypeSimple,
                modSubtypeMartial);

        HBox profModSubtypeLanguageLayout = new HBox();
        ToggleGroup profModSubtypeLanguageGroup = new ToggleGroup();
        RadioButton modSubtypeCommon = new RadioButton("Common");
        modSubtypeCommon.setSelected(true);
        modSubtypeCommon.setToggleGroup(profModSubtypeLanguageGroup);
        RadioButton modSubtypeRare = new RadioButton("Rare");
        modSubtypeRare.setToggleGroup(profModSubtypeLanguageGroup);
        RadioButton modSubtypeLegendary = new RadioButton("Legendary");
        modSubtypeLegendary.setToggleGroup(profModSubtypeLanguageGroup);
        profModSubtypeLanguageLayout.getChildren().addAll(modSubtypeCommon,
                modSubtypeRare, modSubtypeLegendary);

        Button modifyThisProf = new Button("Päivitä");
        Button backFromModifyingProf = new Button("Takaisin");
        HBox profModifyingButtons = new HBox();
        profModifyingButtons.getChildren().addAll(modifyThisProf, backFromModifyingProf);
        profModifyLayout.getChildren().addAll(profModNameLayout, profModNameError,
                profModTypeLayout, profModifyingButtons);

        modTypeSkill.setOnAction((event) -> {
            if (modTypeSkill.isSelected()) {
                profModifyLayout.getChildren().clear();
                profModifyLayout.getChildren().addAll(profModNameLayout,
                        profModNameError, profModTypeLayout, profModifyingButtons);
            }
        });

        modTypeArmor.setOnAction((event) -> {
            if (modTypeArmor.isSelected()) {
                profModifyLayout.getChildren().clear();
                profModifyLayout.getChildren().addAll(profModNameLayout,
                        profModNameError, profModTypeLayout, profModifyingButtons);
            }
        });

        modTypeWeapon.setOnAction((event) -> {
            if (modTypeWeapon.isSelected()) {
                profModifyLayout.getChildren().clear();
                profModifyLayout.getChildren().addAll(profModNameLayout,
                        profModNameError, profModTypeLayout,
                        profModSubtypeWeaponLayout, profModifyingButtons);
            }
        });

        modTypeTool.setOnAction((event) -> {
            if (modTypeTool.isSelected()) {
                profModifyLayout.getChildren().clear();
                profModifyLayout.getChildren().addAll(profModNameLayout,
                        profModNameError, profModTypeLayout,
                        profModSubtypeToolLayout, profModifyingButtons);
            }
        });

        modTypeLanguage.setOnAction((event) -> {
            if (modTypeLanguage.isSelected()) {
                profModifyLayout.getChildren().clear();
                profModifyLayout.getChildren().addAll(profModNameLayout,
                        profModNameError, profModTypeLayout,
                        profModSubtypeLanguageLayout, profModifyingButtons);
            }
        });
        
        modTypeSave.setOnAction((event) -> {
            if (modTypeSave.isSelected()) {
                profModifyLayout.getChildren().clear();
                profModifyLayout.getChildren().addAll(profModNameLayout, profModNameError,
                        profModTypeLayout, profModifyingButtons);
            }
        });
        
        this.profAddScene = new Scene(profAddLayout);
        this.profModScene = new Scene(profModifyLayout);

        modifyProf.setOnAction((event) -> {
            refreshProfs(profs);
            this.databaseWindow.setScene(this.profDatabaseScene);
            this.databaseWindow.show();
        });

        backFromProf.setOnAction((event) -> {
            profDatabaseErrorText.setText("");
            this.databaseWindow.close();
        });

        addProf.setOnAction((event) -> {
            profAddLayout.getChildren().clear();
            profAddLayout.getChildren().addAll(profAddNameLayout,
                profAddNameError, profAddTypeLayout, profAddingButtons);
            profAddNameText.setText("");
            profAddNameError.setText("");
            addTypeSkill.setSelected(true);
            addSubtypeSimple.setSelected(true);
            addSubtypeArtisan.setSelected(true);
            addSubtypeCommon.setSelected(true);
            profDatabaseErrorText.setText("");
            this.modifyWindow.setTitle("Lisää Proficiency");
            this.modifyWindow.setScene(this.profAddScene);
            this.modifyWindow.show();
        });

        modifyExistingProf.setOnAction((event) -> {
            profModNameText.setText("");
            Proficiency profToBeModified = profs.getSelectionModel().getSelectedItem();
            if (!(profToBeModified == null)) {
                profDatabaseErrorText.setText("");
                this.modifyWindow.setTitle("Muokkaa Proficiencyä");
                this.modifyWindow.setScene(this.profModScene);
                this.modifyWindow.show();
                profModNameText.setText(profToBeModified.getName());
                String profType = profToBeModified.getType();

                switch (profType) {
                    case "Skill":
                        modTypeSkill.setSelected(true);
                        profModifyLayout.getChildren().clear();
                        profModifyLayout.getChildren().addAll(profModNameLayout,
                            profModNameError, profModTypeLayout, profModifyingButtons);
                        break;
                    case "Armor":
                        modTypeArmor.setSelected(true);
                        profModifyLayout.getChildren().clear();
                        profModifyLayout.getChildren().addAll(profModNameLayout,
                            profModNameError, profModTypeLayout, profModifyingButtons);
                        break;
                    case "Weapon":
                        modTypeWeapon.setSelected(true);
                        profModifyLayout.getChildren().clear();
                        profModifyLayout.getChildren().addAll(profModNameLayout,
                            profModNameError, profModTypeLayout, profModSubtypeWeaponLayout, 
                            profModifyingButtons);
                        String profSubtypeWep = profToBeModified.getSubtype();
                        switch (profSubtypeWep) {
                            case "Simple":
                                modSubtypeSimple.setSelected(true);
                                break;
                            case "Martial":
                                modSubtypeMartial.setSelected(true);
                                break;
                        }
                        break;
                    case "Tool":
                        modTypeTool.setSelected(true);
                        profModifyLayout.getChildren().clear();
                        profModifyLayout.getChildren().addAll(profModNameLayout,
                            profModNameError, profModTypeLayout, profModSubtypeToolLayout, 
                            profModifyingButtons);
                        String profSubtypeTool = profToBeModified.getSubtype();
                        switch (profSubtypeTool) {
                            case "Artisan":
                                modSubtypeArtisan.setSelected(true);
                                break;
                            case "Gaming Set":
                                modSubtypeGamingSet.setSelected(true);
                                break;
                            case "Instrument":
                                modSubtypeInstrument.setSelected(true);
                                break;
                            case "Other":
                                modSubtypeOther.setSelected(true);
                                break;
                        }
                        break;
                    case "Language":
                        modTypeLanguage.setSelected(true);
                        profModifyLayout.getChildren().clear();
                        profModifyLayout.getChildren().addAll(profModNameLayout,
                            profModNameError, profModTypeLayout, profModSubtypeLanguageLayout, 
                            profModifyingButtons);
                        String profSubtypeLang = profToBeModified.getSubtype();
                        switch (profSubtypeLang) {
                            case "Common":
                                modSubtypeCommon.setSelected(true);
                                break;
                            case "Rare":
                                modSubtypeRare.setSelected(true);
                                break;
                            case "Legendary":
                                modSubtypeLegendary.setSelected(true);
                                break;
                        }
                        break;
                    default:
                        modTypeSave.setSelected(true);
                        profModifyLayout.getChildren().clear();
                        profModifyLayout.getChildren().addAll(profModNameLayout,
                            profModNameError, profModTypeLayout, profModifyingButtons);
                        break;
                }
            } else {
                profDatabaseErrorText.setText("Valitse muokattava proficiency!");
            }
        });

        addNewProf.setOnAction((event) -> {
            String profName = profAddNameText.getText();
            profAddNameText.setText("");
            String profType = "";
            String profSubtype = "";
            if (addTypeLanguage.isSelected()) {
                profType = "Language";
                if (addSubtypeCommon.isSelected()) {
                    profSubtype = "Common";
                } else if (addSubtypeRare.isSelected()) {
                    profSubtype = "Rare";
                } else {
                    profSubtype = "Legendary";
                }
            } else if (addTypeArmor.isSelected()) {
                profType = "Armor";
                profSubtype = "";
            } else if (addTypeWeapon.isSelected()) {
                profType = "Weapon";
                if (addSubtypeSimple.isSelected()) {
                    profSubtype = "Simple";
                } else {
                    profSubtype = "Martial";
                }
            } else if (addTypeTool.isSelected()) {
                profType = "Tool";
                if (addSubtypeArtisan.isSelected()) {
                    profSubtype = "Artisan";
                } else if (addSubtypeGamingSet.isSelected()) {
                    profSubtype = "Gaming Set";
                } else if (addSubtypeInstrument.isSelected()) {
                    profSubtype = "Instrument";
                } else {
                    profSubtype = "Other";
                }
            } else if (addTypeSave.isSelected()) {
                profType = "Save";
                profSubtype = "";
            } else {
                profType = "Skill";
                profSubtype = "";
            }
            if (!profName.isEmpty()) {
                try {
                    this.generator.addNewProfToDb(new Proficiency(profName, profType,
                            profSubtype));
                } catch (SQLException ex) {
                    profAddNameError.setText(ex.getMessage());
                }
                profAddNameError.setText("");
                addTypeSkill.setSelected(true);
                addSubtypeSimple.setSelected(true);
                addSubtypeArtisan.setSelected(true);
                addSubtypeCommon.setSelected(true);
                this.modifyWindow.close();
                refreshProfs(profs);
            } else {
                profAddNameError.setText("Syöte ei voi olla tyhjä!");
            }
        });

        backFromAddingProf.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        modifyThisProf.setOnAction((event) -> {
            String profName = profModNameText.getText();
            profModNameText.setText("");
            String profType = "";
            String profSubtype = "";
            if (modTypeLanguage.isSelected()) {
                profType = "Language";
                if (modSubtypeCommon.isSelected()) {
                    profSubtype = "Common";
                } else if (modSubtypeRare.isSelected()) {
                    profSubtype = "Rare";
                } else {
                    profSubtype = "Legendary";
                }
            } else if (modTypeArmor.isSelected()) {
                profType = "Armor";
                profSubtype = "";
            } else if (modTypeWeapon.isSelected()) {
                profType = "Weapon";
                if (modSubtypeSimple.isSelected()) {
                    profSubtype = "Simple";
                } else {
                    profSubtype = "Martial";
                }
            } else if (modTypeTool.isSelected()) {
                profType = "Tool";
                if (modSubtypeArtisan.isSelected()) {
                    profSubtype = "Artisan";
                } else if (modSubtypeGamingSet.isSelected()) {
                    profSubtype = "Gaming Set";
                } else if (modSubtypeInstrument.isSelected()) {
                    profSubtype = "Instrument";
                } else {
                    profSubtype = "Other";
                }
            } else if (modTypeSave.isSelected()) {
                profType = "Save";
                profSubtype = "";
            } else {
                profType = "Skill";
                profSubtype = "";
            }
            int id = profs.getSelectionModel().getSelectedItem().getId();
            Proficiency newProf = new Proficiency(id, profName, profType, profSubtype);
            if (!profName.isEmpty()) {
                try {
                    this.generator.updateProfToDb(newProf);
                } catch (SQLException ex) {
                    profAddNameError.setText(ex.getMessage());
                }
                profModNameError.setText("");
                this.modifyWindow.close();
                refreshProfs(profs);
            } else {
                profModNameError.setText("Syöte ei voi olla tyhjä!");
            }
        });

        backFromModifyingProf.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        deleteProf.setOnAction((event) -> {
            deleteProficiency(profs, profDatabaseErrorText);
        });

        VBox racialDatabaseLayout = new VBox();

        TableView<Racial> racials = new TableView();

        TableColumn<Racial, String> racialNameColumn = new TableColumn<>("Nimi");
        racialNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        racialNameColumn.prefWidthProperty().bind(racials.widthProperty());

        racials.getColumns().setAll(racialNameColumn);

        Button addRacial = new Button("Lisää uusi");
        Button modifyExistingRacial = new Button("Muokkaa");
        Button deleteRacial = new Button("Poista");
        Button backFromRacial = new Button("Takaisin");
        HBox racialDbButtons = new HBox();

        Label racialDatabaseErrorText = new Label("");
        racialDatabaseErrorText.setTextFill(Color.RED);
        racialDbButtons.getChildren().addAll(addRacial, modifyExistingRacial,
                deleteRacial, backFromRacial);

        racialDatabaseLayout.getChildren().addAll(racials, racialDbButtons,
                racialDatabaseErrorText);
        this.racialDatabaseScene = new Scene(racialDatabaseLayout);

        HBox racialAddLayout = new HBox();

        VBox racialAddLeftLayout = new VBox();
        VBox racialAddCenterLayout = new VBox();
        VBox racialAddRightLayout = new VBox();

        VBox racialAddNameLayout = new VBox();
        Label racialAddNameLabel = new Label("Nimi: ");
        TextField addRacialNameText = new TextField("");
        Label addRacialNameError = new Label("");
        addRacialNameError.setTextFill(Color.RED);
        racialAddNameLayout.getChildren().addAll(racialAddNameLabel, addRacialNameText);

        VBox addRacialStats = new VBox();
        Label addRacialStatsLabel = new Label("Racial antaa statteja: ");
        TextField addRacialStatsText = new TextField("0");
        Label addRacialStatsError = new Label("");
        Label addRacialRandomProfsLabel = new Label("Racial antaa epävarmoja proficiencyjä:");
        TextField addRacialRandomProfs = new TextField("0");
        Label addRacialRandomProfsError = new Label("");
        Label addRacialRandomLangsLabel = new Label("Racial antaa kieliä:");
        TextField addRacialRandomLangs = new TextField("0");
        Label addRacialRandomLangsError = new Label("");
        Label addRacialExtraProfsLabel = new Label("Racial antaa extra-proficiencyjä:");
        TextField addRacialExtraProfs = new TextField("0");
        Label addRacialExtraProfsError = new Label("");

        addRacialStatsError.setTextFill(Color.RED);
        addRacialRandomProfsError.setTextFill(Color.RED);
        addRacialRandomLangsError.setTextFill(Color.RED);
        addRacialExtraProfsError.setTextFill(Color.RED);
        addRacialStats.getChildren().addAll(addRacialStatsLabel, addRacialStatsText,
                addRacialStatsError);

        CheckBox addRacialFeat = new CheckBox("Racial antaa featin");

        Label addRacialExtraProfTypeLabel = new Label("Mitä tyyppiä extra-proficiencyt ovat?");

        ToggleGroup addRacialExtraProfGroup = new ToggleGroup();
        RadioButton addRacialExtraSkill = new RadioButton("Skill");
        addRacialExtraSkill.setSelected(true);
        RadioButton addRacialExtraTool = new RadioButton("Tool");
        RadioButton addRacialExtraArtisan = new RadioButton("Artisan");
        RadioButton addRacialExtraGamingSet = new RadioButton("Gaming Set");
        RadioButton addRacialExtraInstrument = new RadioButton("Instrument");
        RadioButton addRacialExtraSkillTool = new RadioButton("Skill/Tool");
        RadioButton addRacialExtraArtisanInstrument = 
                new RadioButton("Artisan/Instrument");
        RadioButton addRacialExtraArtisanGamingSet = 
                new RadioButton("Artisan/Gaming Set");
        RadioButton addRacialExtraGamingSetInstrument = 
                new RadioButton("Gaming Set/Instrument");
        addRacialExtraSkill.setToggleGroup(addRacialExtraProfGroup);
        addRacialExtraTool.setToggleGroup(addRacialExtraProfGroup);
        addRacialExtraArtisan.setToggleGroup(addRacialExtraProfGroup);
        addRacialExtraGamingSet.setToggleGroup(addRacialExtraProfGroup);
        addRacialExtraInstrument.setToggleGroup(addRacialExtraProfGroup);
        addRacialExtraSkillTool.setToggleGroup(addRacialExtraProfGroup);
        addRacialExtraArtisanInstrument.setToggleGroup(addRacialExtraProfGroup);
        addRacialExtraArtisanGamingSet.setToggleGroup(addRacialExtraProfGroup);
        addRacialExtraGamingSetInstrument.setToggleGroup(addRacialExtraProfGroup);

        Button addNewRacial = new Button("Lisää");
        Button backFromAddingRacial = new Button("Takaisin");
        HBox racialAddingButtons = new HBox();
        racialAddingButtons.getChildren().addAll(addNewRacial, backFromAddingRacial);

        racialAddLeftLayout.getChildren().addAll(racialAddNameLayout, addRacialNameError,
                addRacialStats, addRacialStatsError, addRacialRandomProfsLabel, 
                addRacialRandomProfs, addRacialRandomProfsError, 
                addRacialRandomLangsLabel, addRacialRandomLangs,
                addRacialRandomLangsError, addRacialExtraProfsLabel, 
                addRacialExtraProfs, addRacialExtraProfsError,
                addRacialFeat, addRacialExtraProfTypeLabel, addRacialExtraSkill, 
                addRacialExtraTool, addRacialExtraArtisan, addRacialExtraGamingSet, 
                addRacialExtraInstrument, addRacialExtraSkillTool, 
                addRacialExtraArtisanInstrument, addRacialExtraArtisanGamingSet, 
                addRacialExtraGamingSetInstrument, racialAddingButtons);

        Label addCertainRacialProfLabel = new Label("Valitse varmat proficiencyt");
        TableView<Proficiency> addRacialCertainProfTable = createProfTable();
        Label addUncertainRacialProfLabel = new Label("Valitse epävarmat proficiencyt");
        TableView<Proficiency> addRacialUncertainProfTable = createProfTable();

        racialAddCenterLayout.getChildren().addAll(addCertainRacialProfLabel,
                addRacialCertainProfTable);
        racialAddRightLayout.getChildren().addAll(addUncertainRacialProfLabel,
                addRacialUncertainProfTable);

        racialAddLayout.getChildren().addAll(racialAddLeftLayout, racialAddCenterLayout,
                racialAddRightLayout);

        HBox racialModifyLayout = new HBox();

        VBox racialModLeftLayout = new VBox();
        VBox racialModCenterLayout = new VBox();
        VBox racialModRightLayout = new VBox();

        VBox racialModNameLayout = new VBox();
        Label modRacialNameLabel = new Label("Nimi: ");
        TextField modRacialNameText = new TextField("");
        Label modRacialNameError = new Label("");
        modRacialNameError.setTextFill(Color.RED);
        racialModNameLayout.getChildren().addAll(modRacialNameLabel, modRacialNameText);

        VBox modRacialStats = new VBox();
        Label modRacialStatsLabel = new Label("Racial antaa statteja: ");
        TextField modRacialStatsText = new TextField("0");
        Label modRacialStatsError = new Label("");
        Label modRacialRandomProfsLabel = new Label("Racial antaa epävarmoja proficiencyjä:");
        TextField modRacialRandomProfs = new TextField("0");
        Label modRacialRandomProfsError = new Label("");
        Label modRacialRandomLangsLabel = new Label("Racial antaa kieliä:");
        TextField modRacialRandomLangs = new TextField("0");
        Label modRacialRandomLangsError = new Label("");
        Label modRacialExtraProfsLabel = new Label("Racial antaa extra-proficiencyjä:");
        TextField modRacialExtraProfs = new TextField("0");
        Label modRacialExtraProfsError = new Label("");

        modRacialStatsError.setTextFill(Color.RED);
        modRacialRandomProfsError.setTextFill(Color.RED);
        modRacialRandomLangsError.setTextFill(Color.RED);
        modRacialExtraProfsError.setTextFill(Color.RED);
        modRacialStats.getChildren().addAll(modRacialStatsLabel, modRacialStatsText,
                modRacialStatsError);

        CheckBox modRacialFeat = new CheckBox("Racial antaa featin");

        Label modRacialExtraProfTypeLabel = new Label("Mitä tyyppiä extra-proficiencyt ovat?");

        ToggleGroup modRacialExtraProfGroup = new ToggleGroup();
        RadioButton modRacialExtraSkill = new RadioButton("Skill");
        modRacialExtraSkill.setSelected(true);
        RadioButton modRacialExtraTool = new RadioButton("Tool");
        RadioButton modRacialExtraArtisan = new RadioButton("Artisan");
        RadioButton modRacialExtraGamingSet = new RadioButton("Gaming Set");
        RadioButton modRacialExtraInstrument = new RadioButton("Instrument");
        RadioButton modRacialExtraSkillTool = new RadioButton("Skill/Tool");
        RadioButton modRacialExtraArtisanInstrument = 
                new RadioButton("Artisan/Instrument");
        RadioButton modRacialExtraArtisanGamingSet = 
                new RadioButton("Artisan/Gaming Set");
        RadioButton modRacialExtraGamingSetInstrument = 
                new RadioButton("Gaming Set/Instrument");
        modRacialExtraSkill.setToggleGroup(modRacialExtraProfGroup);
        modRacialExtraTool.setToggleGroup(modRacialExtraProfGroup);
        modRacialExtraArtisan.setToggleGroup(modRacialExtraProfGroup);
        modRacialExtraGamingSet.setToggleGroup(modRacialExtraProfGroup);
        modRacialExtraInstrument.setToggleGroup(modRacialExtraProfGroup);
        modRacialExtraSkillTool.setToggleGroup(modRacialExtraProfGroup);
        modRacialExtraArtisanInstrument.setToggleGroup(modRacialExtraProfGroup);
        modRacialExtraArtisanGamingSet.setToggleGroup(modRacialExtraProfGroup);
        modRacialExtraGamingSetInstrument.setToggleGroup(modRacialExtraProfGroup);

        Button modifyThisRacial = new Button("Päivitä");
        Button backFromModifyingRacial = new Button("Takaisin");
        HBox racialModifyingButtons = new HBox();
        racialModifyingButtons.getChildren().addAll(modifyThisRacial,
                backFromModifyingRacial);

        racialModLeftLayout.getChildren().addAll(racialModNameLayout, modRacialNameError,
                modRacialStats, modRacialStatsError, modRacialRandomProfsLabel, 
                modRacialRandomProfs, modRacialRandomProfsError, 
                modRacialRandomLangsLabel, modRacialRandomLangs,
                modRacialRandomLangsError, modRacialExtraProfsLabel, 
                modRacialExtraProfs, modRacialExtraProfsError, modRacialFeat, 
                modRacialExtraProfTypeLabel, modRacialExtraSkill, 
                modRacialExtraTool, modRacialExtraArtisan, modRacialExtraGamingSet, 
                modRacialExtraInstrument, modRacialExtraSkillTool, 
                modRacialExtraArtisanInstrument, modRacialExtraArtisanGamingSet, 
                modRacialExtraGamingSetInstrument, racialModifyingButtons);

        Label modCertainRacialProfLabel = new Label("Valitse varmat proficiencyt");
        TableView<Proficiency> modRacialCertainProfTable = createProfTable();
        Label modUncertainRacialProfLabel = new Label("Valitse epävarmat proficiencyt");
        TableView<Proficiency> modRacialUncertainProfTable = createProfTable();

        racialModCenterLayout.getChildren().addAll(modCertainRacialProfLabel,
                modRacialCertainProfTable);
        racialModRightLayout.getChildren().addAll(modUncertainRacialProfLabel,
                modRacialUncertainProfTable);

        racialModifyLayout.getChildren().addAll(racialModLeftLayout,
                racialModCenterLayout, racialModRightLayout);

        this.racialAddScene = new Scene(racialAddLayout);
        this.racialModScene = new Scene(racialModifyLayout);
        
        modifyRacial.setOnAction((event) -> {
            refreshRacials(racials);
            this.databaseWindow.setScene(this.racialDatabaseScene);
            this.databaseWindow.show();
        });

        backFromRacial.setOnAction((event) -> {
            racialDatabaseErrorText.setText("");
            this.databaseWindow.close();
        });

        addRacial.setOnAction((event) -> {
            refreshProfs(addRacialCertainProfTable);
            refreshProfs(addRacialUncertainProfTable);
            racialDatabaseErrorText.setText("");
            addRacialNameText.setText("");
            addRacialNameError.setText("");
            addRacialStatsText.setText("0");
            addRacialStatsError.setText("");
            addRacialRandomProfs.setText("0");
            addRacialRandomProfsError.setText("");
            addRacialRandomLangs.setText("0");
            addRacialRandomLangsError.setText("");
            addRacialExtraProfs.setText("0");
            addRacialExtraProfsError.setText("");
            addRacialFeat.setSelected(false);
            addRacialExtraSkill.setSelected(true);
            this.modifyWindow.setTitle("Lisää Racial");
            this.modifyWindow.setScene(this.racialAddScene);
            this.modifyWindow.show();
        });

        modifyExistingRacial.setOnAction((event) -> {
            refreshProfs(modRacialCertainProfTable);
            refreshProfs(modRacialUncertainProfTable);
            modRacialNameText.setText("");
            Racial racialToBeModified = racials.getSelectionModel().getSelectedItem();
            if (!(racialToBeModified == null)) {
                racialDatabaseErrorText.setText("");
                this.modifyWindow.setTitle("Muokkaa Racialia");
                this.modifyWindow.setScene(this.racialModScene);
                this.modifyWindow.show();
                modRacialNameText.setText(racialToBeModified.getName());
                String oldStats = "" + racialToBeModified.getStats();
                String oldRandomProfs = "" + racialToBeModified.getRandomProfs();
                String oldRandomLangs = "" + racialToBeModified.getRandomLangs();
                String oldExtraProfs = "" + racialToBeModified.getExtraProfs();
                String oldExtraProfType = racialToBeModified.getExtraProfType();
                modRacialStatsText.setText(oldStats);
                modRacialRandomProfs.setText(oldRandomProfs);
                modRacialRandomLangs.setText(oldRandomLangs);
                modRacialExtraProfs.setText(oldExtraProfs);
                switch (oldExtraProfType) {
                    case "Skill":
                        modRacialExtraSkill.setSelected(true);
                        break;
                    case "Tool":
                        modRacialExtraTool.setSelected(true);
                        break;
                    case "Artisan":
                        modRacialExtraArtisan.setSelected(true);
                        break;
                    case "Gaming Set":
                        modRacialExtraGamingSet.setSelected(true);
                        break;
                    case "Instrument":
                        modRacialExtraInstrument.setSelected(true);
                        break;
                    case "Skill/Tool":
                        modRacialExtraSkillTool.setSelected(true);
                        break;
                    case "Artisan/Instrument":
                        modRacialExtraArtisanInstrument.setSelected(true);
                        break;
                    case "Artisan/Gaming Set":
                        modRacialExtraArtisanGamingSet.setSelected(true);
                        break;
                    default:
                        modRacialExtraGamingSetInstrument.setSelected(true);
                        break;
                }
                modRacialFeat.setSelected(racialToBeModified.getFeat());

                for (Proficiency certainProf : racialToBeModified.getCertainProfs()) {
                    modRacialCertainProfTable.getSelectionModel().select(certainProf);
                }
                for (Proficiency uncertainProf : racialToBeModified.getUncertainProfs()) {
                    modRacialUncertainProfTable.getSelectionModel().select(uncertainProf);
                }
            } else {
                racialDatabaseErrorText.setText("Valitse muokattava racial!");
            }
        });

        addNewRacial.setOnAction((event) -> {
            String racialName = addRacialNameText.getText();

            if (!racialName.isEmpty() && isInteger(addRacialStatsText.getText())
                    && isInteger(addRacialRandomProfs.getText())
                    && isInteger(addRacialRandomLangs.getText())
                    && isInteger(addRacialExtraProfs.getText())) {
                int racialStats = Integer.parseInt(addRacialStatsText.getText());
                int randomProfs = Integer.parseInt(addRacialRandomProfs.getText());
                int randomLangs = Integer.parseInt(addRacialRandomLangs.getText());
                int extraProfs = Integer.parseInt(addRacialExtraProfs.getText());
                boolean racialFeat = addRacialFeat.isSelected();
                String extraProfType = "";
                if (extraProfs != 0) {
                    if (addRacialExtraSkill.isSelected()) {
                        extraProfType = "Skill";
                    } else if (addRacialExtraTool.isSelected()) {
                        extraProfType = "Tool";
                    } else if (addRacialExtraArtisan.isSelected()) {
                        extraProfType = "Artisan";
                    } else if (addRacialExtraGamingSet.isSelected()) {
                        extraProfType = "Gaming Set";
                    } else if (addRacialExtraInstrument.isSelected()) {
                        extraProfType = "Instrument";
                    } else if (addRacialExtraSkillTool.isSelected()) {
                        extraProfType = "Skill/Tool";
                    } else if (addRacialExtraArtisanInstrument.isSelected()) {
                        extraProfType = "Artisan/Instrument";
                    } else if (addRacialExtraArtisanGamingSet.isSelected()) {
                        extraProfType = "Artisan/Gaming Set";
                    } else {
                        extraProfType = "Gaming Set/Instrument";
                    }
                }
                Racial racialToAdd = new Racial(racialName, racialStats, racialFeat,
                        randomProfs, randomLangs, extraProfs, extraProfType);

                ObservableList<Proficiency> certainProfsToAdd = addRacialCertainProfTable.
                        getSelectionModel().getSelectedItems();

                ObservableList<Proficiency> uncertainProfsToAdd = addRacialUncertainProfTable.
                        getSelectionModel().getSelectedItems();

                for (Proficiency prof : certainProfsToAdd) {
                    racialToAdd.addCertainProf(prof);
                }
                for (Proficiency prof : uncertainProfsToAdd) {
                    racialToAdd.addUncertainProf(prof);
                }
                try {
                    this.generator.addNewRacialToDb(racialToAdd);
                } catch (SQLException ex) {
                    addRacialNameError.setText(ex.getMessage());
                }
                racialDatabaseErrorText.setText("");
                addRacialNameText.setText("");
                addRacialNameError.setText("");
                addRacialStatsText.setText("0");
                addRacialStatsError.setText("");
                addRacialRandomProfs.setText("0");
                addRacialRandomProfsError.setText("");
                addRacialRandomLangs.setText("0");
                addRacialRandomLangsError.setText("");
                addRacialExtraProfs.setText("0");
                addRacialExtraProfsError.setText("");
                addRacialFeat.setSelected(false);
                addRacialExtraSkill.setSelected(true);
                this.modifyWindow.close();
                refreshRacials(racials);
            } else {
                if (racialName.isEmpty()) {
                    addRacialNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    addRacialNameError.setText("");
                }
                if (!isInteger(addRacialStatsText.getText())) {
                    addRacialStatsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    addRacialStatsError.setText("");
                }
                if (!isInteger(addRacialRandomProfs.getText())) {
                    addRacialRandomProfsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    addRacialRandomProfsError.setText("");
                }
                if (!isInteger(addRacialRandomLangs.getText())) {
                    addRacialRandomLangsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    addRacialRandomLangsError.setText("");
                }
                if (!isInteger(addRacialExtraProfs.getText())) {
                    addRacialExtraProfsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    addRacialExtraProfsError.setText("");
                }
            }
        });

        backFromAddingRacial.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        modifyThisRacial.setOnAction((event) -> {
            String racialName = modRacialNameText.getText();

            if (!racialName.isEmpty() && isInteger(modRacialStatsText.getText())
                    && isInteger(modRacialRandomProfs.getText())
                    && isInteger(modRacialRandomLangs.getText())
                    && isInteger(modRacialExtraProfs.getText())) {
                int racialStats = Integer.parseInt(modRacialStatsText.getText());
                int randomProfs = Integer.parseInt(modRacialRandomProfs.getText());
                int randomLangs = Integer.parseInt(modRacialRandomLangs.getText());
                int extraProfs = Integer.parseInt(modRacialExtraProfs.getText());
                boolean racialFeat = modRacialFeat.isSelected();
                int id = racials.getSelectionModel().getSelectedItem().getId();
                String extraProfType = "";
                if (extraProfs != 0) {
                    if (modRacialExtraSkill.isSelected()) {
                        extraProfType = "Skill";
                    } else if (modRacialExtraTool.isSelected()) {
                        extraProfType = "Tool";
                    } else if (modRacialExtraArtisan.isSelected()) {
                        extraProfType = "Artisan";
                    } else if (modRacialExtraGamingSet.isSelected()) {
                        extraProfType = "Gaming Set";
                    } else if (modRacialExtraInstrument.isSelected()) {
                        extraProfType = "Instrument";
                    } else if (modRacialExtraSkillTool.isSelected()) {
                        extraProfType = "Skill/Tool";
                    } else if (modRacialExtraArtisanInstrument.isSelected()) {
                        extraProfType = "Artisan/Instrument";
                    } else if (modRacialExtraArtisanGamingSet.isSelected()) {
                        extraProfType = "Artisan/Gaming Set";
                    } else {
                        extraProfType = "Gaming Set/Instrument";
                    }
                }
                Racial racialToMod = new Racial(id, racialName, racialStats, racialFeat, 
                    randomProfs, randomLangs, extraProfs, extraProfType);

                ObservableList<Proficiency> certainProfsToMod = modRacialCertainProfTable.
                        getSelectionModel().getSelectedItems();
                
                ObservableList<Proficiency> uncertainProfsToMod = 
                        modRacialUncertainProfTable.getSelectionModel().
                        getSelectedItems();

                for (Proficiency prof : certainProfsToMod) {
                    racialToMod.addCertainProf(prof);
                }
                for (Proficiency prof : uncertainProfsToMod) {
                    racialToMod.addUncertainProf(prof);
                }
                try {
                    this.generator.updateRacialToDb(racialToMod);
                } catch (SQLException ex) {
                    modRacialNameError.setText(ex.getMessage());
                }
                racialDatabaseErrorText.setText("");
                modRacialNameText.setText("");
                modRacialNameError.setText("");
                modRacialStatsText.setText("0");
                modRacialStatsError.setText("");
                modRacialRandomProfs.setText("0");
                modRacialRandomProfsError.setText("");
                modRacialRandomLangs.setText("0");
                modRacialRandomLangsError.setText("");
                modRacialExtraProfs.setText("0");
                modRacialExtraProfsError.setText("");
                modRacialFeat.setSelected(false);
                modRacialExtraSkill.setSelected(true);
                this.modifyWindow.close();
                refreshRacials(racials);
            } else {
                if (racialName.isEmpty()) {
                    modRacialNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    modRacialNameError.setText("");
                }
                if (!isInteger(modRacialStatsText.getText())) {
                    modRacialStatsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    modRacialStatsError.setText("");
                }
                if (!isInteger(modRacialRandomProfs.getText())) {
                    modRacialRandomProfsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    modRacialRandomProfsError.setText("");
                }
                if (!isInteger(modRacialRandomLangs.getText())) {
                    modRacialRandomLangsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    modRacialRandomLangsError.setText("");
                }
                if (!isInteger(modRacialExtraProfs.getText())) {
                    modRacialExtraProfsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    modRacialExtraProfsError.setText("");
                }
            }
        });

        backFromModifyingRacial.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        deleteRacial.setOnAction((event) -> {
            deleteRacial(racials, racialDatabaseErrorText);
        });

        VBox classDatabaseLayout = new VBox();

        HBox classTables = new HBox();
        TableView<RpgClass> classes = new TableView();

        VBox subclassLayout = new VBox();
        Label subclassLabel = new Label("Subclassit");
        ListView<String> subclasses = createEmptyList();
        subclasses.setEditable(false);
        subclassLayout.getChildren().addAll(subclassLabel, subclasses);

        TableColumn<RpgClass, String> classNameColumn = new TableColumn<>("Nimi");
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        classNameColumn.prefWidthProperty().bind(classes.widthProperty());

        classes.setOnMouseClicked((event) -> {
            RpgClass rpgclass = classes.getSelectionModel().getSelectedItem();
            if (!(rpgclass == null)) {
                refreshSubclasses(rpgclass, subclasses);
            }
        });

        classes.getColumns().setAll(classNameColumn);

        classTables.getChildren().addAll(classes, subclassLayout);

        Button addClass = new Button("Lisää uusi");
        Button modifyExistingClass = new Button("Muokkaa");
        Button deleteClass = new Button("Poista");
        Button backFromClass = new Button("Takaisin");
        HBox classDbButtons = new HBox();

        Label classDatabaseErrorText = new Label("");
        classDatabaseErrorText.setTextFill(Color.RED);
        classDbButtons.getChildren().addAll(addClass, modifyExistingClass,
                deleteClass, backFromClass);

        classDatabaseLayout.getChildren().addAll(classTables, classDbButtons,
                classDatabaseErrorText);
        this.classDatabaseScene = new Scene(classDatabaseLayout);
        
        HBox classAddLayout = new HBox();

        VBox classAddLeftLayout = new VBox();
        VBox classAddCenterLeftLayout = new VBox();
        VBox classAddCenterRightLayout = new VBox();
        VBox classAddRightLayout = new VBox();

        VBox classAddNameLayout = new VBox();
        Label addClassNameLabel = new Label("Nimi: ");
        TextField addClassNameText = new TextField("");
        Label addClassNameError = new Label("");
        addClassNameError.setTextFill(Color.RED);
        classAddNameLayout.getChildren().addAll(addClassNameLabel, addClassNameText);

        Label addClassRandomProfsLabel = new Label("Class antaa epävarmoja proficiencyjä:");
        TextField addClassRandomProfs = new TextField("0");
        Label addClassRandomProfsError = new Label("");
        Label addClassRandomLangsLabel = new Label("Class antaa kieliä:");
        TextField addClassRandomLangs = new TextField("0");
        Label addClassRandomLangsError = new Label("");
        Label addClassExtraProfsLabel = new Label("Class antaa extra-proficiencyjä:");
        TextField addClassExtraProfs = new TextField("0");
        Label addClassExtraProfsError = new Label("");

        addClassRandomProfsError.setTextFill(Color.RED);
        addClassRandomLangsError.setTextFill(Color.RED);
        addClassExtraProfsError.setTextFill(Color.RED);

        Label addClassExtraProfTypeLabel = new Label("Mitä tyyppiä extra-proficiencyt ovat?");

        ToggleGroup addClassExtraProfGroup = new ToggleGroup();
        RadioButton addClassExtraSkill = new RadioButton("Skill");
        addClassExtraSkill.setSelected(true);
        RadioButton addClassExtraTool = new RadioButton("Tool");
        RadioButton addClassExtraArtisan = new RadioButton("Artisan");
        RadioButton addClassExtraGamingSet = new RadioButton("Gaming Set");
        RadioButton addClassExtraInstrument = new RadioButton("Instrument");
        RadioButton addClassExtraSkillTool = new RadioButton("Skill/Tool");
        RadioButton addClassExtraArtisanInstrument = 
                new RadioButton("Artisan/Instrument");
        RadioButton addClassExtraArtisanGamingSet = 
                new RadioButton("Artisan/Gaming Set");
        RadioButton addClassExtraGamingSetInstrument = 
                new RadioButton("Gaming Set/Instrument");
        addClassExtraSkill.setToggleGroup(addClassExtraProfGroup);
        addClassExtraTool.setToggleGroup(addClassExtraProfGroup);
        addClassExtraArtisan.setToggleGroup(addClassExtraProfGroup);
        addClassExtraGamingSet.setToggleGroup(addClassExtraProfGroup);
        addClassExtraInstrument.setToggleGroup(addClassExtraProfGroup);
        addClassExtraSkillTool.setToggleGroup(addClassExtraProfGroup);
        addClassExtraArtisanInstrument.setToggleGroup(addClassExtraProfGroup);
        addClassExtraArtisanGamingSet.setToggleGroup(addClassExtraProfGroup);
        addClassExtraGamingSetInstrument.setToggleGroup(addClassExtraProfGroup);

        Button addNewClass = new Button("Lisää");
        Button backFromAddingClass = new Button("Takaisin");
        HBox classAddingButtons = new HBox();
        classAddingButtons.getChildren().addAll(addNewClass, backFromAddingClass);

        classAddLeftLayout.getChildren().addAll(classAddNameLayout, addClassNameError,
                addClassRandomProfsLabel, addClassRandomProfs, addClassRandomProfsError, 
                addClassRandomLangsLabel, addClassRandomLangs,
                addClassRandomLangsError, addClassExtraProfsLabel, 
                addClassExtraProfs, addClassExtraProfsError,
                addClassExtraProfTypeLabel, addClassExtraSkill, 
                addClassExtraTool, addClassExtraArtisan, addClassExtraGamingSet, 
                addClassExtraInstrument, addClassExtraSkillTool, 
                addClassExtraArtisanInstrument, addClassExtraArtisanGamingSet, 
                addClassExtraGamingSetInstrument, classAddingButtons);

        Label addClassCertainProfLabel = new Label("Valitse varmat proficiencyt");
        TableView<Proficiency> addClassCertainProfTable = createProfTable();
        Label addClassUncertainProfLabel = new Label("Valitse epävarmat proficiencyt");
        TableView<Proficiency> addClassUncertainProfTable = createProfTable();

        classAddCenterRightLayout.getChildren().addAll(addClassCertainProfLabel,
                addClassCertainProfTable);
        classAddRightLayout.getChildren().addAll(addClassUncertainProfLabel,
                addClassUncertainProfTable);

        Label addSubclassLabel = new Label("Lisää subclassit");
        ListView<String> addSubclassList = createEmptyList();

        classAddCenterLeftLayout.getChildren().addAll(addSubclassLabel, addSubclassList);

        classAddLayout.getChildren().addAll(classAddLeftLayout, classAddCenterLeftLayout, 
                classAddCenterRightLayout, classAddRightLayout);

        HBox classModifyLayout = new HBox();

        VBox classModLeftLayout = new VBox();
        VBox classModCenterLeftLayout = new VBox();
        VBox classModCenterRightLayout = new VBox();
        VBox classModRightLayout = new VBox();

        VBox classModNameLayout = new VBox();
        Label modClassNameLabel = new Label("Nimi: ");
        TextField modClassNameText = new TextField("");
        Label modClassNameError = new Label("");
        modClassNameError.setTextFill(Color.RED);
        classModNameLayout.getChildren().addAll(modClassNameLabel, modClassNameText);

        Label modClassRandomProfsLabel = new Label("Class antaa epävarmoja proficiencyjä:");
        TextField modClassRandomProfs = new TextField("0");
        Label modClassRandomProfsError = new Label("");
        Label modClassRandomLangsLabel = new Label("Class antaa kieliä:");
        TextField modClassRandomLangs = new TextField("0");
        Label modClassRandomLangsError = new Label("");
        Label modClassExtraProfsLabel = new Label("Class antaa extra-proficiencyjä:");
        TextField modClassExtraProfs = new TextField("0");
        Label modClassExtraProfsError = new Label("");

        modClassRandomProfsError.setTextFill(Color.RED);
        modClassRandomLangsError.setTextFill(Color.RED);
        modClassExtraProfsError.setTextFill(Color.RED);

        Label modClassExtraProfTypeLabel = new Label("Mitä tyyppiä extra-proficiencyt ovat?");

        ToggleGroup modClassExtraProfGroup = new ToggleGroup();
        RadioButton modClassExtraSkill = new RadioButton("Skill");
        modClassExtraSkill.setSelected(true);
        RadioButton modClassExtraTool = new RadioButton("Tool");
        RadioButton modClassExtraArtisan = new RadioButton("Artisan");
        RadioButton modClassExtraGamingSet = new RadioButton("Gaming Set");
        RadioButton modClassExtraInstrument = new RadioButton("Instrument");
        RadioButton modClassExtraSkillTool = new RadioButton("Skill/Tool");
        RadioButton modClassExtraArtisanInstrument = 
                new RadioButton("Artisan/Instrument");
        RadioButton modClassExtraArtisanGamingSet = 
                new RadioButton("Artisan/Gaming Set");
        RadioButton modClassExtraGamingSetInstrument = 
                new RadioButton("Gaming Set/Instrument");
        modClassExtraSkill.setToggleGroup(modClassExtraProfGroup);
        modClassExtraTool.setToggleGroup(modClassExtraProfGroup);
        modClassExtraArtisan.setToggleGroup(modClassExtraProfGroup);
        modClassExtraGamingSet.setToggleGroup(modClassExtraProfGroup);
        modClassExtraInstrument.setToggleGroup(modClassExtraProfGroup);
        modClassExtraSkillTool.setToggleGroup(modClassExtraProfGroup);
        modClassExtraArtisanInstrument.setToggleGroup(modClassExtraProfGroup);
        modClassExtraArtisanGamingSet.setToggleGroup(modClassExtraProfGroup);
        modClassExtraGamingSetInstrument.setToggleGroup(modClassExtraProfGroup);

        Button modifyThisClass = new Button("Päivitä");
        Button backFromModifyingClass = new Button("Takaisin");
        HBox classModifyingButtons = new HBox();
        classModifyingButtons.getChildren().addAll(modifyThisClass, backFromModifyingClass);

        classModLeftLayout.getChildren().addAll(classModNameLayout, modClassNameError,
                modClassRandomProfsLabel, modClassRandomProfs, modClassRandomProfsError, 
                modClassRandomLangsLabel, modClassRandomLangs,
                modClassRandomLangsError, modClassExtraProfsLabel, 
                modClassExtraProfs, modClassExtraProfsError,
                modClassExtraProfTypeLabel, modClassExtraSkill, 
                modClassExtraTool, modClassExtraArtisan, modClassExtraGamingSet, 
                modClassExtraInstrument, modClassExtraSkillTool, 
                modClassExtraArtisanInstrument, modClassExtraArtisanGamingSet, 
                modClassExtraGamingSetInstrument, classModifyingButtons);

        Label modClassCertainProfLabel = new Label("Valitse varmat proficiencyt");
        TableView<Proficiency> modClassCertainProfTable = createProfTable();
        Label modClassUncertainProfLabel = new Label("Valitse epävarmat proficiencyt");
        TableView<Proficiency> modClassUncertainProfTable = createProfTable();

        classModCenterRightLayout.getChildren().addAll(modClassCertainProfLabel,
                modClassCertainProfTable);
        classModRightLayout.getChildren().addAll(modClassUncertainProfLabel,
                modClassUncertainProfTable);

        Label modSubclassLabel = new Label("Muokkaa subclassit");
        ListView<String> modSubclassList = createEmptyList();

        classModCenterLeftLayout.getChildren().addAll(modSubclassLabel, modSubclassList);

        classModifyLayout.getChildren().addAll(classModLeftLayout, classModCenterLeftLayout, 
                classModCenterRightLayout, classModRightLayout);

        this.classAddScene = new Scene(classAddLayout);
        this.classModScene = new Scene(classModifyLayout);

        modifyClass.setOnAction((event) -> {
            refreshClasses(classes);
            this.databaseWindow.setScene(this.classDatabaseScene);
            this.databaseWindow.show();
        });

        backFromClass.setOnAction((event) -> {
            classDatabaseErrorText.setText("");
            this.databaseWindow.close();
        });

        addClass.setOnAction((event) -> {
            refreshProfs(addClassCertainProfTable);
            refreshProfs(addClassUncertainProfTable);
            emptyListView(addSubclassList);
            classDatabaseErrorText.setText("");
            addClassNameText.setText("");
            addClassNameError.setText("");
            addClassRandomProfs.setText("0");
            addClassRandomProfsError.setText("");
            addClassRandomLangs.setText("0");
            addClassRandomLangsError.setText("");
            addClassExtraProfs.setText("0");
            addClassExtraProfsError.setText("");
            addClassExtraSkill.setSelected(true);
            this.modifyWindow.setTitle("Lisää Class");
            this.modifyWindow.setScene(this.classAddScene);
            this.modifyWindow.show();
        });

        modifyExistingClass.setOnAction((event) -> {
            refreshProfs(modClassCertainProfTable);
            refreshProfs(modClassUncertainProfTable);
            modClassNameText.setText("");
            RpgClass classToBeModified = classes.getSelectionModel().getSelectedItem();
            if (!(classToBeModified == null)) {
                classDatabaseErrorText.setText("");
                this.modifyWindow.setTitle("Muokkaa Classia");
                this.modifyWindow.setScene(this.classModScene);
                this.modifyWindow.show();
                modClassNameText.setText(classToBeModified.getName());
                String oldRandomProfs = "" + classToBeModified.getRandomProfs();
                String oldRandomLangs = "" + classToBeModified.getRandomLangs();
                String oldExtraProfs = "" + classToBeModified.getExtraProfs();
                String oldExtraProfType = classToBeModified.getExtraProfType();
                modClassRandomProfs.setText(oldRandomProfs);
                modClassRandomLangs.setText(oldRandomLangs);
                modClassExtraProfs.setText(oldExtraProfs);
                switch (oldExtraProfType) {
                    case "Skill":
                        modClassExtraSkill.setSelected(true);
                        break;
                    case "Tool":
                        modClassExtraTool.setSelected(true);
                        break;
                    case "Artisan":
                        modClassExtraArtisan.setSelected(true);
                        break;
                    case "Gaming Set":
                        modClassExtraGamingSet.setSelected(true);
                        break;
                    case "Instrument":
                        modClassExtraInstrument.setSelected(true);
                        break;
                    case "Skill/Tool":
                        modClassExtraSkillTool.setSelected(true);
                        break;
                    case "Artisan/Instrument":
                        modClassExtraArtisanInstrument.setSelected(true);
                        break;
                    case "Artisan/Gaming Set":
                        modClassExtraArtisanGamingSet.setSelected(true);
                        break;
                    default:
                        modClassExtraGamingSetInstrument.setSelected(true);
                        break;
                }
                refreshSubclasses(classToBeModified, modSubclassList);

                for (Proficiency certainProf : classToBeModified.getCertainProfs()) {
                    modClassCertainProfTable.getSelectionModel().select(certainProf);
                }
                for (Proficiency uncertainProf : classToBeModified.getUncertainProfs()) {
                    modClassUncertainProfTable.getSelectionModel().select(uncertainProf);
                }
            } else {
                classDatabaseErrorText.setText("Valitse muokattava class!");
            }
        });

        addNewClass.setOnAction((event) -> {
            String className = addClassNameText.getText();

            if (!className.isEmpty() && isInteger(addClassRandomProfs.getText())
                    && isInteger(addClassRandomLangs.getText())
                    && isInteger(addClassExtraProfs.getText())) {
                int randomProfs = Integer.parseInt(addClassRandomProfs.getText());
                int randomLangs = Integer.parseInt(addClassRandomLangs.getText());
                int extraProfs = Integer.parseInt(addClassExtraProfs.getText());
                String extraProfType = "";
                if (extraProfs != 0) {
                    if (addClassExtraSkill.isSelected()) {
                        extraProfType = "Skill";
                    } else if (addClassExtraTool.isSelected()) {
                        extraProfType = "Tool";
                    } else if (addClassExtraArtisan.isSelected()) {
                        extraProfType = "Artisan";
                    } else if (addClassExtraGamingSet.isSelected()) {
                        extraProfType = "Gaming Set";
                    } else if (addClassExtraInstrument.isSelected()) {
                        extraProfType = "Instrument";
                    } else if (addClassExtraSkillTool.isSelected()) {
                        extraProfType = "Skill/Tool";
                    } else if (addClassExtraArtisanInstrument.isSelected()) {
                        extraProfType = "Artisan/Instrument";
                    } else if (addClassExtraArtisanGamingSet.isSelected()) {
                        extraProfType = "Artisan/Gaming Set";
                    } else {
                        extraProfType = "Gaming Set/Instrument";
                    }
                }
                RpgClass classToAdd = new RpgClass(className, randomProfs, randomLangs, 
                        extraProfs, extraProfType);

                ObservableList<Proficiency> certainProfsToAdd = addClassCertainProfTable.
                        getSelectionModel().getSelectedItems();

                ObservableList<Proficiency> uncertainProfsToAdd = addClassUncertainProfTable.
                        getSelectionModel().getSelectedItems();

                ObservableList<String> subclassesToAdd = addSubclassList.getItems();

                for (String subclass : subclassesToAdd) {
                    if (!(subclass.isEmpty())) {
                        classToAdd.addSubclass(subclass.trim());
                    }
                }               
                for (Proficiency prof : certainProfsToAdd) {
                    classToAdd.addCertainProf(prof);
                }
                for (Proficiency prof : uncertainProfsToAdd) {
                    classToAdd.addUncertainProf(prof);
                }
                
                try {
                    this.generator.addNewClassToDb(classToAdd);
                } catch (SQLException ex) {
                    addClassNameError.setText(ex.getMessage());
                }
                classDatabaseErrorText.setText("");
                addClassNameText.setText("");
                addClassNameError.setText("");
                addClassRandomProfs.setText("0");
                addClassRandomProfsError.setText("");
                addClassRandomLangs.setText("0");
                addClassRandomLangsError.setText("");
                addClassExtraProfs.setText("0");
                addClassExtraProfsError.setText("");
                addClassExtraSkill.setSelected(true);
                this.modifyWindow.close();
                refreshClasses(classes);
            } else {
                if (className.isEmpty()) {
                    addClassNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    addClassNameError.setText("");
                }
                if (!isInteger(addClassRandomProfs.getText())) {
                    addClassRandomProfsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    addClassRandomProfsError.setText("");
                }
                if (!isInteger(addClassRandomLangs.getText())) {
                    addClassRandomLangsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    addClassRandomLangsError.setText("");
                }
                if (!isInteger(addClassExtraProfs.getText())) {
                    addClassExtraProfsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    addClassExtraProfsError.setText("");
                }
            }
        });

        backFromAddingClass.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        modifyThisClass.setOnAction((event) -> {
            String className = modClassNameText.getText();

            if (!className.isEmpty() && isInteger(modClassRandomProfs.getText())
                    && isInteger(modClassRandomLangs.getText())
                    && isInteger(modClassExtraProfs.getText())) {
                int randomProfs = Integer.parseInt(modClassRandomProfs.getText());
                int randomLangs = Integer.parseInt(modClassRandomLangs.getText());
                int extraProfs = Integer.parseInt(modClassExtraProfs.getText());
                int id = classes.getSelectionModel().getSelectedItem().getId();
                String extraProfType = "";
                if (extraProfs != 0) {
                    if (modClassExtraSkill.isSelected()) {
                        extraProfType = "Skill";
                    } else if (modClassExtraTool.isSelected()) {
                        extraProfType = "Tool";
                    } else if (modClassExtraArtisan.isSelected()) {
                        extraProfType = "Artisan";
                    } else if (modClassExtraGamingSet.isSelected()) {
                        extraProfType = "Gaming Set";
                    } else if (modClassExtraInstrument.isSelected()) {
                        extraProfType = "Instrument";
                    } else if (modClassExtraSkillTool.isSelected()) {
                        extraProfType = "Skill/Tool";
                    } else if (modClassExtraArtisanInstrument.isSelected()) {
                        extraProfType = "Artisan/Instrument";
                    } else if (modClassExtraArtisanGamingSet.isSelected()) {
                        extraProfType = "Artisan/Gaming Set";
                    } else {
                        extraProfType = "Gaming Set/Instrument";
                    }
                }
                RpgClass classToMod = new RpgClass(id, className, randomProfs, 
                        randomLangs, extraProfs, extraProfType);

                ObservableList<Proficiency> certainProfsToMod = modClassCertainProfTable.
                        getSelectionModel().getSelectedItems();
                
                ObservableList<Proficiency> uncertainProfsToMod = 
                        modClassUncertainProfTable.getSelectionModel().
                        getSelectedItems();
                
                ObservableList<String> subclassesToMod = modSubclassList.getItems();

                ArrayList<String> newSubclasses = new ArrayList<>();
                for (String subclass : subclassesToMod) {
                    if (!(subclass.isEmpty())) {
                        newSubclasses.add(subclass.trim());
                    }
                }
                
                classToMod.setSubclasses(newSubclasses);
                
                for (Proficiency prof : certainProfsToMod) {
                    classToMod.addCertainProf(prof);
                }              
                for (Proficiency prof : uncertainProfsToMod) {
                    classToMod.addUncertainProf(prof);
                }
                
                try {
                    this.generator.updateClassToDb(classToMod);
                } catch (SQLException ex) {
                    modClassNameError.setText(ex.getMessage());
                }
                classDatabaseErrorText.setText("");
                modClassNameText.setText("");
                modClassNameError.setText("");
                modClassRandomProfs.setText("0");
                modClassRandomProfsError.setText("");
                modClassRandomLangs.setText("0");
                modClassRandomLangsError.setText("");
                modClassExtraProfs.setText("0");
                modClassExtraProfsError.setText("");
                modClassExtraSkill.setSelected(true);
                this.modifyWindow.close();
                refreshClasses(classes);
            } else {
                if (className.isEmpty()) {
                    modClassNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    modClassNameError.setText("");
                }
                if (!isInteger(modClassRandomProfs.getText())) {
                    modClassRandomProfsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    modClassRandomProfsError.setText("");
                }
                if (!isInteger(modClassRandomLangs.getText())) {
                    modClassRandomLangsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    modClassRandomLangsError.setText("");
                }
                if (!isInteger(modClassExtraProfs.getText())) {
                    modClassExtraProfsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    modClassExtraProfsError.setText("");
                }
            }
        });

        backFromModifyingClass.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        deleteClass.setOnAction((event) -> {
            deleteClass(classes, subclasses, classDatabaseErrorText);
        });

        VBox bgDatabaseLayout = new VBox();

        TableView<Background> bgs = new TableView();

        TableColumn<Background, String> bgNameColumn = new TableColumn<>("Nimi");
        bgNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bgNameColumn.prefWidthProperty().bind(bgs.widthProperty());

        bgs.getColumns().setAll(bgNameColumn);

        Button addBg = new Button("Lisää uusi");
        Button modifyExistingBg = new Button("Muokkaa");
        Button deleteBg = new Button("Poista");
        Button backFromBg = new Button("Takaisin");
        HBox bgDbButtons = new HBox();

        Label bgDatabaseErrorText = new Label("");
        bgDatabaseErrorText.setTextFill(Color.RED);
        bgDbButtons.getChildren().addAll(addBg, modifyExistingBg,
                deleteBg, backFromBg);

        bgDatabaseLayout.getChildren().addAll(bgs, bgDbButtons,
                bgDatabaseErrorText);
        this.bgDatabaseScene = new Scene(bgDatabaseLayout);

        HBox bgAddLayout = new HBox();

        VBox bgAddLeftLayout = new VBox();
        HBox bgAddRightLayout = new HBox();

        VBox bgAddNameLayout = new VBox();
        Label bgAddNameLabel = new Label("Nimi: ");
        TextField bgAddNameText = new TextField("");
        Label bgAddNameError = new Label("");
        bgAddNameError.setTextFill(Color.RED);
        bgAddNameLayout.getChildren().addAll(bgAddNameLabel, bgAddNameText);

        Button addNewBg = new Button("Lisää");
        Button backFromAddingBg = new Button("Takaisin");
        HBox bgAddingButtons = new HBox();
        bgAddingButtons.getChildren().addAll(addNewBg, backFromAddingBg);

        bgAddLeftLayout.getChildren().addAll(bgAddNameLayout, bgAddNameError,
                bgAddingButtons);

        TableView<Proficiency> addBgProfTable = createProfTable();

        bgAddRightLayout.getChildren().addAll(addBgProfTable);

        bgAddLayout.getChildren().addAll(bgAddLeftLayout, bgAddRightLayout);

        HBox bgModifyLayout = new HBox();

        VBox bgModLeftLayout = new VBox();
        VBox bgModRightLayout = new VBox();

        VBox bgModNameLayout = new VBox();
        Label bgModNameLabel = new Label("Nimi: ");
        TextField bgModNameText = new TextField("");
        Label bgModNameError = new Label("");
        bgModNameError.setTextFill(Color.RED);
        bgModNameLayout.getChildren().addAll(bgModNameLabel, bgModNameText);

        Button modifyThisBg = new Button("Päivitä");
        Button backFromModifyingBg = new Button("Takaisin");
        HBox bgModifyingButtons = new HBox();
        bgModifyingButtons.getChildren().addAll(modifyThisBg, backFromModifyingBg);

        bgModLeftLayout.getChildren().addAll(bgModNameLayout, bgModNameError,
                bgModifyingButtons);

        TableView<Proficiency> modBgProfTable = createProfTable();

        bgModRightLayout.getChildren().addAll(modBgProfTable);

        bgModifyLayout.getChildren().addAll(bgModLeftLayout, bgModRightLayout);

        this.bgAddScene = new Scene(bgAddLayout);
        this.bgModScene = new Scene(bgModifyLayout);
        
        modifyBg.setOnAction((event) -> {
            refreshBackgrounds(bgs);
            this.databaseWindow.setScene(this.bgDatabaseScene);
            this.databaseWindow.show();
        });

        backFromBg.setOnAction((event) -> {
            bgDatabaseErrorText.setText("");
            this.databaseWindow.close();
        });

        addBg.setOnAction((event) -> {
            /*refreshProfs(addBgProfTable);
            bgDatabaseErrorText.setText("");
            bgAddNameText.setText("");
            bgAddNameError.setText("");
            this.modifyWindow.setTitle("Lisää Background");
            this.modifyWindow.setScene(this.bgAddScene);
            this.modifyWindow.show();*/
        });

        modifyExistingBg.setOnAction((event) -> {
            /*refreshProfs(modBgProfTable);
            bgModNameText.setText("");
            Background bgToBeModified = bgs.getSelectionModel().getSelectedItem();
            if (!(bgToBeModified == null)) {
                bgDatabaseErrorText.setText("");
                this.modifyWindow.setTitle("Muokkaa Backgroundia");
                this.modifyWindow.setScene(this.bgModScene);
                this.modifyWindow.show();
                bgModNameText.setText(bgToBeModified.getName());

                for (Proficiency bgProf : bgToBeModified.getBackgroundProfs()) {
                    modBgProfTable.getSelectionModel().select(bgProf);
                }
            } else {
                bgDatabaseErrorText.setText("Valitse muokattava background!");
            }*/
        });

        addNewBg.setOnAction((event) -> {
            /*String bgName = bgAddNameText.getText();

            if (!bgName.isEmpty()) {
                Background bgToAdd = new Background(bgName);

                ObservableList<Proficiency> bgProfsToAdd = addBgProfTable.
                        getSelectionModel().getSelectedItems();

                for (Proficiency prof : bgProfsToAdd) {
                    bgToAdd.addBackgroundProf(prof);
                }
                try {
                    this.generator.addNewBgToDb(bgToAdd);
                } catch (SQLException ex) {
                    bgAddNameError.setText(ex.getMessage());
                }
                bgAddNameText.setText("");
                bgAddNameError.setText("");
                this.modifyWindow.close();
                refreshBackgrounds(bgs);
            } else {
                if (bgName.isEmpty()) {
                    bgAddNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    bgAddNameError.setText("");
                }
            }*/
        });

        backFromAddingBg.setOnAction((event) -> {
            /*this.modifyWindow.close();*/
        });

        modifyThisBg.setOnAction((event) -> {
            /*String bgName = bgModNameText.getText();

            if (!bgName.isEmpty()) {
                int id = bgs.getSelectionModel().getSelectedItem().getId();

                Background bgToMod = new Background(id, bgName);

                ObservableList<Proficiency> bgProfsToMod = modBgProfTable.
                        getSelectionModel().getSelectedItems();

                for (Proficiency prof : bgProfsToMod) {
                    bgToMod.addBackgroundProf(prof);
                }
                try {
                    this.generator.updateBgToDb(bgToMod);
                } catch (SQLException ex) {
                    bgModNameError.setText(ex.getMessage());
                }
                bgModNameText.setText("");
                bgModNameError.setText("");
                this.modifyWindow.close();
                refreshBackgrounds(bgs);
            } else {
                if (bgName.isEmpty()) {
                    bgModNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    bgModNameError.setText("");
                }
            }*/
        });

        backFromModifyingBg.setOnAction((event) -> {
            /*this.modifyWindow.close();*/
        });

        deleteBg.setOnAction((event) -> {
            /*deleteBg(bgs, bgDatabaseErrorText);*/
        });

        VBox featDatabaseLayout = new VBox();
        TableView<Feat> feats = new TableView();

        TableColumn<Feat, String> featNameColumn = new TableColumn<>("Nimi");
        featNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        featNameColumn.prefWidthProperty().bind(feats.widthProperty().multiply(0.5));

        TableColumn<Feat, String> featStatColumn = new TableColumn<>("Stats");
        featStatColumn.setCellValueFactory(new PropertyValueFactory<>("stats"));
        featStatColumn.prefWidthProperty().bind(feats.widthProperty().multiply(0.5));

        feats.getColumns().setAll(featNameColumn, featStatColumn);

        Button addFeat = new Button("Lisää uusi");
        Button modifyExistingFeat = new Button("Muokkaa");
        Button deleteFeat = new Button("Poista");
        Button backFromFeat = new Button("Takaisin");
        HBox featDbButtons = new HBox();

        Label featDatabaseErrorText = new Label("");
        featDatabaseErrorText.setTextFill(Color.RED);
        featDbButtons.getChildren().addAll(addFeat, modifyExistingFeat, deleteFeat,
                backFromFeat);

        featDatabaseLayout.getChildren().addAll(feats, featDbButtons, featDatabaseErrorText);
        this.featDatabaseScene = new Scene(featDatabaseLayout);

        HBox featAddLayout = new HBox();

        VBox featAddLeftLayout = new VBox();
        HBox featAddRightLayout = new HBox();

        VBox featAddNameLayout = new VBox();
        Label featAddNameLabel = new Label("Nimi: ");
        TextField featAddNameText = new TextField("");
        Label featAddNameError = new Label("");
        featAddNameError.setTextFill(Color.RED);
        featAddNameLayout.getChildren().addAll(featAddNameLabel, featAddNameText);

        VBox addFeatStats = new VBox();
        Label addFeatStatsLabel = new Label("Mitä statteja Feat voi antaa?");

        HBox addFeatStatsBoxes = new HBox();
        CheckBox addFeatStatStr = new CheckBox("STR");
        CheckBox addFeatStatDex = new CheckBox("DEX");
        CheckBox addFeatStatCon = new CheckBox("CON");
        CheckBox addFeatStatInt = new CheckBox("INT");
        CheckBox addFeatStatWis = new CheckBox("WIS");
        CheckBox addFeatStatCha = new CheckBox("CHA");
        addFeatStatsBoxes.getChildren().addAll(addFeatStatStr, addFeatStatDex,
                addFeatStatCon, addFeatStatInt, addFeatStatWis, addFeatStatCha);

        addFeatStats.getChildren().addAll(addFeatStatsLabel, addFeatStatsBoxes);

        Button addNewFeat = new Button("Lisää");
        Button backFromAddingFeat = new Button("Takaisin");
        HBox featAddingButtons = new HBox();
        featAddingButtons.getChildren().addAll(addNewFeat, backFromAddingFeat);

        featAddLeftLayout.getChildren().addAll(featAddNameLayout, featAddNameError,
                addFeatStats, featAddingButtons);

        TableView<Proficiency> addFeatProfTable = createProfTable();

        featAddRightLayout.getChildren().addAll(addFeatProfTable);

        featAddLayout.getChildren().addAll(featAddLeftLayout, featAddRightLayout);

        HBox featModifyLayout = new HBox();

        VBox featModLeftLayout = new VBox();
        VBox featModRightLayout = new VBox();

        VBox featModNameLayout = new VBox();
        Label featModNameLabel = new Label("Nimi: ");
        TextField featModNameText = new TextField("");
        Label featModNameError = new Label("");
        featModNameError.setTextFill(Color.RED);
        featModNameLayout.getChildren().addAll(featModNameLabel, featModNameText);

        VBox modFeatStats = new VBox();
        Label modFeatStatsLabel = new Label("Mitä statteja Feat voi antaa?");

        HBox modFeatStatsBoxes = new HBox();
        CheckBox modFeatStatStr = new CheckBox("STR");
        CheckBox modFeatStatDex = new CheckBox("DEX");
        CheckBox modFeatStatCon = new CheckBox("CON");
        CheckBox modFeatStatInt = new CheckBox("INT");
        CheckBox modFeatStatWis = new CheckBox("WIS");
        CheckBox modFeatStatCha = new CheckBox("CHA");
        modFeatStatsBoxes.getChildren().addAll(modFeatStatStr, modFeatStatDex,
                modFeatStatCon, modFeatStatInt, modFeatStatWis, modFeatStatCha);

        modFeatStats.getChildren().addAll(modFeatStatsLabel, modFeatStatsBoxes);

        Button modifyThisFeat = new Button("Päivitä");
        Button backFromModifyingFeat = new Button("Takaisin");
        HBox featModifyingButtons = new HBox();
        featModifyingButtons.getChildren().addAll(modifyThisFeat, backFromModifyingFeat);

        featModLeftLayout.getChildren().addAll(featModNameLayout, featModNameError,
                modFeatStats, featModifyingButtons);

        TableView<Proficiency> modFeatProfTable = createProfTable();

        featModRightLayout.getChildren().addAll(modFeatProfTable);

        featModifyLayout.getChildren().addAll(featModLeftLayout, featModRightLayout);

        this.featAddScene = new Scene(featAddLayout);
        this.featModScene = new Scene(featModifyLayout);
        
        modifyFeat.setOnAction((event) -> {
            refreshFeats(feats);
            this.databaseWindow.setScene(this.featDatabaseScene);
            this.databaseWindow.show();
        });

        backFromFeat.setOnAction((event) -> {
            featDatabaseErrorText.setText("");
            this.databaseWindow.close();
        });

        addFeat.setOnAction((event) -> {
            /*refreshProfs(addFeatProfTable);
            featDatabaseErrorText.setText("");
            featAddNameText.setText("");
            featAddNameError.setText("");
            addFeatStatStr.setSelected(false);
            addFeatStatDex.setSelected(false);
            addFeatStatCon.setSelected(false);
            addFeatStatInt.setSelected(false);
            addFeatStatWis.setSelected(false);
            addFeatStatCha.setSelected(false);
            this.modifyWindow.setTitle("Lisää Feat");
            this.modifyWindow.setScene(this.featAddScene);
            this.modifyWindow.show();*/
        });

        modifyExistingFeat.setOnAction((event) -> {
            /*refreshProfs(modFeatProfTable);
            featModNameText.setText("");
            modFeatStatStr.setSelected(false);
            modFeatStatDex.setSelected(false);
            modFeatStatCon.setSelected(false);
            modFeatStatInt.setSelected(false);
            modFeatStatWis.setSelected(false);
            modFeatStatCha.setSelected(false);
            Feat featToBeModified = feats.getSelectionModel().getSelectedItem();
            if (!(featToBeModified == null)) {
                featDatabaseErrorText.setText("");
                this.modifyWindow.setTitle("Muokkaa Feattia");
                this.modifyWindow.setScene(this.featModScene);
                this.modifyWindow.show();
                featModNameText.setText(featToBeModified.getName());
                String oldStats = featToBeModified.getStats();

                String[] parts = oldStats.split("/");
                for (int i = 0; i < parts.length; i++) {
                    switch (parts[i]) {
                        case "STR":
                            modFeatStatStr.setSelected(true);
                            break;
                        case "DEX":
                            modFeatStatDex.setSelected(true);
                            break;
                        case "CON":
                            modFeatStatCon.setSelected(true);
                            break;
                        case "INT":
                            modFeatStatInt.setSelected(true);
                            break;
                        case "WIS":
                            modFeatStatWis.setSelected(true);
                            break;
                        case "CHA":
                            modFeatStatCha.setSelected(true);
                            break;
                    }
                }

                for (Proficiency featProf : featToBeModified.getFeatProfs()) {
                    modFeatProfTable.getSelectionModel().select(featProf);
                }
            } else {
                racialDatabaseErrorText.setText("Valitse muokattava feat!");
            }*/
        });

        addNewFeat.setOnAction((event) -> {
            /*String featName = featAddNameText.getText();

            if (!featName.isEmpty()) {
                String stats = "";
                if (addFeatStatStr.isSelected()) {
                    stats += "STR/";
                }
                if (addFeatStatDex.isSelected()) {
                    stats += "DEX/";
                }
                if (addFeatStatCon.isSelected()) {
                    stats += "CON/";
                }
                if (addFeatStatInt.isSelected()) {
                    stats += "INT/";
                }
                if (addFeatStatWis.isSelected()) {
                    stats += "WIS/";
                }
                if (addFeatStatCha.isSelected()) {
                    stats += "CHA/";
                }
                if (!stats.isEmpty()) {
                    stats = stats.substring(0, stats.length() - 1);
                }

                Feat featToAdd = new Feat(featName);
                featToAdd.setStats(stats);

                ObservableList<Proficiency> featProfsToAdd = addFeatProfTable.
                        getSelectionModel().getSelectedItems();

                for (Proficiency prof : featProfsToAdd) {
                    featToAdd.addFeatProf(prof);
                }
                try {
                    this.generator.addNewFeatToDb(featToAdd);
                } catch (SQLException ex) {
                    featAddNameError.setText(ex.getMessage());
                }
                featAddNameText.setText("");
                featAddNameError.setText("");
                addFeatStatStr.setSelected(false);
                addFeatStatDex.setSelected(false);
                addFeatStatCon.setSelected(false);
                addFeatStatInt.setSelected(false);
                addFeatStatWis.setSelected(false);
                addFeatStatCha.setSelected(false);
                this.modifyWindow.close();
                refreshFeats(feats);
            } else {
                if (featName.isEmpty()) {
                    featAddNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    featAddNameError.setText("");
                }
            }*/
        });

        backFromAddingFeat.setOnAction((event) -> {
            /*this.modifyWindow.close();*/
        });

        modifyThisFeat.setOnAction((event) -> {
            /*String featName = featModNameText.getText();

            if (!featName.isEmpty()) {
                int id = feats.getSelectionModel().getSelectedItem().getId();

                Feat featToMod = new Feat(id, featName);

                String stats = "";
                if (modFeatStatStr.isSelected()) {
                    stats += "STR/";
                }
                if (modFeatStatDex.isSelected()) {
                    stats += "DEX/";
                }
                if (modFeatStatCon.isSelected()) {
                    stats += "CON/";
                }
                if (modFeatStatInt.isSelected()) {
                    stats += "INT/";
                }
                if (modFeatStatWis.isSelected()) {
                    stats += "WIS/";
                }
                if (modFeatStatCha.isSelected()) {
                    stats += "CHA/";
                }
                if (!stats.isEmpty()) {
                    stats = stats.substring(0, stats.length() - 1);
                }

                featToMod.setStats(stats);

                ObservableList<Proficiency> featProfsToMod = modFeatProfTable.
                        getSelectionModel().getSelectedItems();

                for (Proficiency prof : featProfsToMod) {
                    featToMod.addFeatProf(prof);
                }
                try {
                    this.generator.updateFeatToDb(featToMod);
                } catch (SQLException ex) {
                    featModNameError.setText(ex.getMessage());
                }
                featModNameText.setText("");
                featModNameError.setText("");
                modFeatStatStr.setSelected(false);
                modFeatStatDex.setSelected(false);
                modFeatStatCon.setSelected(false);
                modFeatStatInt.setSelected(false);
                modFeatStatWis.setSelected(false);
                modFeatStatCha.setSelected(false);
                this.modifyWindow.close();
                refreshFeats(feats);
            } else {
                if (featName.isEmpty()) {
                    featModNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    featModNameError.setText("");
                }
            }*/
        });

        backFromModifyingFeat.setOnAction((event) -> {
            /*this.modifyWindow.close();*/
        });

        deleteFeat.setOnAction((event) -> {
            /*deleteFeat(feats, featDatabaseErrorText);*/
        });

        Label stats = new Label("");
        Label proficiencies = new Label("");
        HBox characterAttributes = new HBox();

        characterAttributes.getChildren()
                .addAll(stats, proficiencies);
        layout.setCenter(characterAttributes);

        generate.setOnAction((event) -> {
            this.generator.generate();
            stats.setText(this.generator.generateStatList());
        });

        this.primaryWindow.setTitle("Hahmogeneraattori");
        this.primaryWindow.setScene(this.startScene);

        this.databaseWindow.initOwner(this.primaryWindow);
        this.modifyWindow.initOwner(this.databaseWindow);

        this.databaseWindow.initModality(Modality.WINDOW_MODAL);
        this.modifyWindow.initModality(Modality.WINDOW_MODAL);

        this.primaryWindow.show();
        //alkunäkymä
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        this.settings.update();
    }
    
    public void deleteRace(TableView<Race> races, Label error) {
        try {
            Race raceToBeDeleted = races.getSelectionModel().getSelectedItem();
            this.generator.deleteRaceFromDb(raceToBeDeleted);
            error.setText("");
        } catch (Exception e) {
            String errorText = "Valitse poistettava race!";
            if (!(e.getMessage() == null)) {
                errorText += e.getMessage();
            }
            error.setText(errorText);
        }
        refreshRaces(races);
    }

    public void deleteProficiency(TableView<Proficiency> profs, Label error) {
        try {
            Proficiency profToBeDeleted = profs.getSelectionModel().getSelectedItem();
            this.generator.deleteProfFromDb(profToBeDeleted);
            error.setText("");
        } catch (Exception e) {
            String errorText = "Valitse poistettava proficiency!";
            if (!(e.getMessage() == null)) {
                errorText += e.getMessage();
            }
            error.setText(errorText);
        }
        refreshProfs(profs);
    }

    public void deleteRacial(TableView<Racial> racials, Label error) {
        try {
            Racial racialToBeDeleted = racials.getSelectionModel().getSelectedItem();
            this.generator.deleteRacialFromDb(racialToBeDeleted);
            error.setText("");
        } catch (Exception e) {
            String errorText = "Valitse poistettava racial!";
            if (!(e.getMessage() == null)) {
                errorText += e.getMessage();
            }
            error.setText(errorText);
        }
        refreshRacials(racials);
    }

    public void deleteClass(TableView<RpgClass> classes, ListView<String> subclasses, Label error) {
        try {
            RpgClass classToBeDeleted = classes.getSelectionModel()
                    .getSelectedItem();
            this.generator.deleteClassFromDb(classToBeDeleted);
            error.setText("");
            emptyListView(subclasses);
        } catch (Exception e) {
            String errorText = "Valitse poistettava class!";
            if (!(e.getMessage() == null)) {
                errorText += e.getMessage();
            }
            error.setText(errorText);
        }
        refreshClasses(classes);
    }

    public void deleteBg(TableView<Background> bgs, Label error) {
        try {
            Background bgToBeDeleted = bgs.getSelectionModel().getSelectedItem();
            this.generator.deleteBgFromDb(bgToBeDeleted);
            error.setText("");
        } catch (Exception e) {
            String errorText = "Valitse poistettava background!";
            if (!(e.getMessage() == null)) {
                errorText += e.getMessage();
            }
            error.setText(errorText);
        }
        refreshBackgrounds(bgs);
    }

    public void deleteFeat(TableView<Feat> feats, Label error) {
        try {
            Feat featToBeDeleted = feats.getSelectionModel().getSelectedItem();
            this.generator.deleteFeatFromDb(featToBeDeleted);
            error.setText("");
        } catch (Exception e) {
            String errorText = "Valitse poistettava background!";
            if (!(e.getMessage() == null)) {
                errorText += e.getMessage();
            }
            error.setText(errorText);
        }
        refreshFeats(feats);
    }

    public TableView<Proficiency> createProfTable() {
        TableView<Proficiency> profTable = new TableView();

        TableColumn<Proficiency, String> profNameColumn = new TableColumn<>(""
                + "Valitse proficiencyt");
        profNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        profNameColumn.prefWidthProperty().bind(profTable.widthProperty());

        profTable.getColumns().setAll(profNameColumn);
        profTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        profTable.getSortOrder().add(profNameColumn);

        return profTable;
    }
    
    public void refreshRaces(TableView raceView) {
        raceView.getItems().clear();
        raceView.getItems().addAll(this.generator.listAllRaces());
        raceView.sort();
    }

    public void refreshProfs(TableView profView) {
        profView.getItems().clear();
        profView.getItems().addAll(this.generator.listAllProfs());
        profView.sort();
    }

    public void refreshRacials(TableView racialView) {
        racialView.getItems().clear();
        racialView.getItems().addAll(this.generator.listAllRacials());
        racialView.sort();
    }

    public void refreshClasses(TableView classView) {
        classView.getItems().clear();
        classView.getItems().addAll(this.generator.listAllClasses());
        classView.sort();
    }

    public void refreshSubclasses(RpgClass rpgclass, ListView subclassView) {
        emptyListView(subclassView);
        int index = 0;
        for (String subclass : rpgclass.getSubclasses()) {
            subclassView.getItems().set(index, subclass);
            index++;
        }
    }

    public void refreshBackgrounds(TableView bgView) {
        bgView.getItems().clear();
        bgView.getItems().addAll(this.generator.listAllBackgrounds());
        bgView.sort();
    }

    public void refreshFeats(TableView featView) {
        featView.getItems().clear();
        featView.getItems().addAll(this.generator.listAllFeats());
        featView.sort();
    }

    public void emptyListView(ListView listToEmpty) {
        for (int i = 0; i < 12; i++) {
            listToEmpty.getItems().set(i, "");
        }
    }

    public ListView<String> createEmptyList() {
        ListView<String> emptyList = new ListView();

        emptyList.setEditable(true);
        emptyList.setCellFactory(TextFieldListCell.forListView());
        String tyhja = "";
        ArrayList<String> empties = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            empties.add(tyhja);
        }
        emptyList.getItems().addAll(empties);

        return emptyList;
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
