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
    private Scene profDatabaseScene;
    private Scene racialDatabaseScene;
    private Scene classDatabaseScene;
    private Scene bgDatabaseScene;
    private Scene featDatabaseScene;
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

        MenuItem modifyProf = new MenuItem("Proficiency");
        MenuItem modifyRace = new MenuItem("Race");
        MenuItem modifyRacial = new MenuItem("Racial");
        MenuItem modifyClass = new MenuItem("Class");
        MenuItem modifyBg = new MenuItem("Background");
        MenuItem modifyFeat = new MenuItem("Feat");
        database.getItems().addAll(modifyRace, modifyRacial, modifyClass,
                modifyBg, modifyProf, modifyFeat);

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
        profAddTypeLayout.getChildren().addAll(addTypeSkill, addTypeArmor, addTypeWeapon,
                addTypeTool, addTypeLanguage);

        HBox profAddSubtypeToolLayout = new HBox();
        ToggleGroup profAddSubtypeToolGroup = new ToggleGroup();
        RadioButton addSubtypeArtisan = new RadioButton("Artisan");
        addSubtypeArtisan.setSelected(true);
        addSubtypeArtisan.setToggleGroup(profAddSubtypeToolGroup);
        RadioButton addSubtypeGamingSet = new RadioButton("Gaming Set");
        addSubtypeGamingSet.setToggleGroup(profAddSubtypeToolGroup);
        RadioButton addSubtypeInstrument = new RadioButton("Instrument");
        addSubtypeInstrument.setToggleGroup(profAddSubtypeToolGroup);
        profAddSubtypeToolLayout.getChildren().addAll(addSubtypeArtisan,
                addSubtypeGamingSet, addSubtypeInstrument);

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
        profModTypeLayout.getChildren().addAll(modTypeSkill, modTypeArmor, modTypeWeapon,
                modTypeTool, modTypeLanguage);

        HBox profModSubtypeToolLayout = new HBox();
        ToggleGroup profModSubtypeToolGroup = new ToggleGroup();
        RadioButton modSubtypeArtisan = new RadioButton("Artisan");
        modSubtypeArtisan.setSelected(true);
        modSubtypeArtisan.setToggleGroup(profModSubtypeToolGroup);
        RadioButton modSubtypeGamingSet = new RadioButton("Gaming Set");
        modSubtypeGamingSet.setToggleGroup(profModSubtypeToolGroup);
        RadioButton modSubtypeInstrument = new RadioButton("Instrument");
        modSubtypeInstrument.setToggleGroup(profModSubtypeToolGroup);
        profModSubtypeToolLayout.getChildren().addAll(modSubtypeArtisan,
                modSubtypeGamingSet, modSubtypeInstrument);

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

        Button addNewProf = new Button("Lisää");
        Button backFromAddingProf = new Button("Takaisin");

        Button modifyThisProf = new Button("Päivitä");
        Button backFromModifyingProf = new Button("Takaisin");

        HBox profAddingButtons = new HBox();
        HBox profModifyingButtons = new HBox();
        profAddingButtons.getChildren().addAll(addNewProf, backFromAddingProf);
        profModifyingButtons.getChildren().addAll(modifyThisProf, backFromModifyingProf);

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

        Label profDatabaseErrorText = new Label("");
        profDatabaseErrorText.setTextFill(Color.RED);
        profDbButtons.getChildren().addAll(addProf, modifyExistingProf, deleteProf,
                backFromProf);

        profDatabaseLayout.getChildren().addAll(profs, profDbButtons, profDatabaseErrorText);
        this.profDatabaseScene = new Scene(profDatabaseLayout);

        modifyProf.setOnAction((event) -> {
            refreshProfs(profs);
            this.databaseWindow.setScene(this.profDatabaseScene);
            this.databaseWindow.show();
        });

        backFromProf.setOnAction((event) -> {
            profDatabaseErrorText.setText("");
            this.databaseWindow.close();
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

        modifyRacial.setOnAction((event) -> {
            refreshRacials(racials);
            this.databaseWindow.setScene(this.racialDatabaseScene);
            this.databaseWindow.show();
        });

        backFromRacial.setOnAction((event) -> {
            racialDatabaseErrorText.setText("");
            this.databaseWindow.close();
        });

        VBox classDatabaseLayout = new VBox();

        HBox classTables = new HBox();
        TableView<RpgClass> classes = new TableView();

        VBox subclassLayout = new VBox();
        Label subclassLabel = new Label("Subclassit");
        ListView<String> subclasses = createEmptyList();
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

        modifyClass.setOnAction((event) -> {
            refreshClasses(classes);
            this.databaseWindow.setScene(this.classDatabaseScene);
            this.databaseWindow.show();
        });

        backFromClass.setOnAction((event) -> {
            classDatabaseErrorText.setText("");
            this.databaseWindow.close();
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

        modifyBg.setOnAction((event) -> {
            refreshBackgrounds(bgs);
            this.databaseWindow.setScene(this.bgDatabaseScene);
            this.databaseWindow.show();
        });

        backFromBg.setOnAction((event) -> {
            bgDatabaseErrorText.setText("");
            this.databaseWindow.close();
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

        modifyFeat.setOnAction((event) -> {
            refreshFeats(feats);
            this.databaseWindow.setScene(this.featDatabaseScene);
            this.databaseWindow.show();
        });

        backFromFeat.setOnAction((event) -> {
            featDatabaseErrorText.setText("");
            this.databaseWindow.close();
        });

        this.modifyWindow = new Stage();
        this.profAddScene = new Scene(profAddLayout);
        this.profModScene = new Scene(profModifyLayout);

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
                        }
                        break;
                    default:
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
                } else {
                    profSubtype = "Instrument";
                }
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
                } else {
                    profSubtype = "Instrument";
                }
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

        HBox racialAddLayout = new HBox();

        VBox racialAddLeftLayout = new VBox();
        VBox racialAddCenterLayout = new VBox();
        VBox racialAddRightLayout = new VBox();

        VBox racialAddNameLayout = new VBox();
        Label racialAddNameLabel = new Label("Nimi: ");
        TextField racialAddNameText = new TextField("");
        Label racialAddNameError = new Label("");
        racialAddNameError.setTextFill(Color.RED);
        racialAddNameLayout.getChildren().addAll(racialAddNameLabel, racialAddNameText);

        VBox addRacialStats = new VBox();
        Label addRacialStatsLabel = new Label("Racial antaa statteja: ");
        TextField addRacialStatsText = new TextField("0");
        Label addRacialStatsError = new Label("");
        Label addRacialRandomProfsLabel = new Label("Racial antaa epävarmoja proficiencyjä:");
        TextField addRacialRandomProfs = new TextField("0");
        Label racialAddRandomProfsError = new Label("");
        Label addRacialRandomLangsLabel = new Label("Racial antaa kieliä:");
        TextField addRacialRandomLangs = new TextField("0");
        Label racialAddRandomLangsError = new Label("");
        Label addRacialExtraProfsLabel = new Label("Racial antaa extra-proficiencyjä:");
        TextField addRacialExtraProfs = new TextField("0");
        Label racialAddExtraProfsError = new Label("");

        addRacialStatsError.setTextFill(Color.RED);
        addRacialStats.getChildren().addAll(addRacialStatsLabel, addRacialStatsText,
                addRacialStatsError);

        CheckBox addRacialFeat = new CheckBox("Racial antaa featin");

        Label addRacialExtraProfTypeLabel = new Label("Mitä tyyppiä extra-proficiencyt ovat?");

        ToggleGroup addRacialExtraProfGroup = new ToggleGroup();
        RadioButton addExtraSkill = new RadioButton("Skill");
        addExtraSkill.setSelected(true);
        RadioButton addExtraTool = new RadioButton("Tool");
        RadioButton addExtraArtisan = new RadioButton("Artisan");
        RadioButton addExtraGamingSet = new RadioButton("Gaming Set");
        RadioButton addExtraInstrument = new RadioButton("Instrument");
        RadioButton addExtraSkillTool = new RadioButton("Skill/Tool");
        RadioButton addExtraArtisanInstrument = new RadioButton("Artisan/Instrument");
        RadioButton addExtraArtisanGamingSet = new RadioButton("Artisan/Gaming Set");
        RadioButton addExtraGamingSetInstrument = new RadioButton("Gaming Set/Instrument");
        addExtraSkill.setToggleGroup(addRacialExtraProfGroup);
        addExtraTool.setToggleGroup(addRacialExtraProfGroup);
        addExtraArtisan.setToggleGroup(addRacialExtraProfGroup);
        addExtraGamingSet.setToggleGroup(addRacialExtraProfGroup);
        addExtraInstrument.setToggleGroup(addRacialExtraProfGroup);
        addExtraSkillTool.setToggleGroup(addRacialExtraProfGroup);
        addExtraArtisanInstrument.setToggleGroup(addRacialExtraProfGroup);
        addExtraArtisanGamingSet.setToggleGroup(addRacialExtraProfGroup);
        addExtraGamingSetInstrument.setToggleGroup(addRacialExtraProfGroup);

        Button addNewRacial = new Button("Lisää");
        Button backFromAddingRacial = new Button("Takaisin");
        HBox racialAddingButtons = new HBox();
        racialAddingButtons.getChildren().addAll(addNewRacial, backFromAddingRacial);

        racialAddLeftLayout.getChildren().addAll(racialAddNameLayout, racialAddNameError,
                addRacialStats, addRacialStatsError, addRacialRandomProfsLabel, 
                addRacialRandomProfs, racialAddRandomProfsError, 
                addRacialRandomLangsLabel, addRacialRandomLangs,
                racialAddRandomLangsError, addRacialExtraProfsLabel, 
                addRacialExtraProfs, racialAddExtraProfsError,
                addRacialFeat, addRacialExtraProfTypeLabel, addExtraSkill, addExtraTool,
                addExtraArtisan, addExtraGamingSet, addExtraInstrument,
                addExtraSkillTool, addExtraArtisanInstrument,
                addExtraArtisanGamingSet, addExtraGamingSetInstrument,
                racialAddingButtons);

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
        Label racialModNameLabel = new Label("Nimi: ");
        TextField racialModNameText = new TextField("");
        Label racialModNameError = new Label("");
        racialModNameError.setTextFill(Color.RED);
        racialModNameLayout.getChildren().addAll(racialModNameLabel, racialModNameText);

        VBox modRacialStats = new VBox();
        Label modRacialStatsLabel = new Label("Racial antaa statteja: ");
        TextField modRacialStatsText = new TextField("0");
        Label modRacialStatsError = new Label("");
        Label modRacialRandomProfsLabel = new Label("Racial antaa epävarmoja proficiencyjä:");
        TextField modRacialRandomProfs = new TextField("0");
        Label racialModRandomProfsError = new Label("");
        Label modRacialRandomLangsLabel = new Label("Racial antaa kieliä:");
        TextField modRacialRandomLangs = new TextField("0");
        Label racialModRandomLangsError = new Label("");
        Label modRacialExtraProfsLabel = new Label("Racial antaa extra-proficiencyjä:");
        TextField modRacialExtraProfs = new TextField("0");
        Label racialModExtraProfsError = new Label("");

        modRacialStatsError.setTextFill(Color.RED);
        modRacialStats.getChildren().addAll(modRacialStatsLabel, modRacialStatsText,
                modRacialStatsError);

        CheckBox modRacialFeat = new CheckBox("Racial antaa featin");

        Label modRacialExtraProfTypeLabel = new Label("Mitä tyyppiä extra-proficiencyt ovat?");

        ToggleGroup modRacialExtraProfGroup = new ToggleGroup();
        RadioButton modExtraSkill = new RadioButton("Skill");
        modExtraSkill.setSelected(true);
        RadioButton modExtraTool = new RadioButton("Tool");
        RadioButton modExtraArtisan = new RadioButton("Artisan");
        RadioButton modExtraGamingSet = new RadioButton("Gaming Set");
        RadioButton modExtraInstrument = new RadioButton("Instrument");
        RadioButton modExtraSkillTool = new RadioButton("Skill/Tool");
        RadioButton modExtraArtisanInstrument = new RadioButton("Artisan/Instrument");
        RadioButton modExtraArtisanGamingSet = new RadioButton("Artisan/Gaming Set");
        RadioButton modExtraGamingSetInstrument = new RadioButton("Gaming Set/Instrument");
        modExtraSkill.setToggleGroup(modRacialExtraProfGroup);
        modExtraTool.setToggleGroup(modRacialExtraProfGroup);
        modExtraArtisan.setToggleGroup(modRacialExtraProfGroup);
        modExtraGamingSet.setToggleGroup(modRacialExtraProfGroup);
        modExtraInstrument.setToggleGroup(modRacialExtraProfGroup);
        modExtraSkillTool.setToggleGroup(modRacialExtraProfGroup);
        modExtraArtisanInstrument.setToggleGroup(modRacialExtraProfGroup);
        modExtraArtisanGamingSet.setToggleGroup(modRacialExtraProfGroup);
        modExtraGamingSetInstrument.setToggleGroup(modRacialExtraProfGroup);

        Button modifyThisRacial = new Button("Lisää");
        Button backFromModifyingRacial = new Button("Takaisin");
        HBox racialModifyingButtons = new HBox();
        racialModifyingButtons.getChildren().addAll(modifyThisRacial,
                backFromModifyingRacial);

        racialModLeftLayout.getChildren().addAll(racialModNameLayout, racialModNameError,
                modRacialStats, modRacialStatsError, modRacialRandomProfsLabel, 
                modRacialRandomProfs, racialModRandomProfsError, 
                modRacialRandomLangsLabel, modRacialRandomLangs,
                racialModRandomLangsError, modRacialExtraProfsLabel, modRacialExtraProfs, 
                racialModExtraProfsError, modRacialFeat, 
                modRacialExtraProfTypeLabel, modExtraSkill, modExtraTool,
                modExtraArtisan, modExtraGamingSet, modExtraInstrument,
                modExtraSkillTool, modExtraArtisanInstrument,
                modExtraArtisanGamingSet, modExtraGamingSetInstrument,
                racialModifyingButtons);

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

        addRacial.setOnAction((event) -> {
            refreshProfs(addRacialCertainProfTable);
            refreshProfs(addRacialUncertainProfTable);
            racialDatabaseErrorText.setText("");
            racialAddNameText.setText("");
            racialAddNameError.setText("");
            addRacialStatsText.setText("0");
            addRacialStatsError.setText("");
            addRacialRandomProfs.setText("0");
            racialAddRandomProfsError.setText("");
            addRacialRandomLangs.setText("0");
            racialAddRandomLangsError.setText("");
            addRacialExtraProfs.setText("0");
            racialAddExtraProfsError.setText("");
            addRacialFeat.setSelected(false);
            addExtraSkill.setSelected(true);
            this.modifyWindow.setTitle("Lisää Racial");
            this.modifyWindow.setScene(this.racialAddScene);
            this.modifyWindow.show();
        });

        modifyExistingRacial.setOnAction((event) -> {
            refreshProfs(modRacialCertainProfTable);
            refreshProfs(modRacialUncertainProfTable);
            racialModNameText.setText("");
            Racial racialToBeModified = racials.getSelectionModel().getSelectedItem();
            if (!(racialToBeModified == null)) {
                racialDatabaseErrorText.setText("");
                this.modifyWindow.setTitle("Muokkaa Racialia");
                this.modifyWindow.setScene(this.racialModScene);
                this.modifyWindow.show();
                racialModNameText.setText(racialToBeModified.getName());
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
                        modExtraSkill.setSelected(true);
                        break;
                    case "Tool":
                        modExtraTool.setSelected(true);
                        break;
                    case "Artisan":
                        modExtraArtisan.setSelected(true);
                        break;
                    case "Gaming Set":
                        modExtraGamingSet.setSelected(true);
                        break;
                    case "Instrument":
                        modExtraInstrument.setSelected(true);
                        break;
                    case "Skill/Tool":
                        modExtraSkillTool.setSelected(true);
                        break;
                    case "Artisan/Instrument":
                        modExtraArtisanInstrument.setSelected(true);
                        break;
                    case "Artisan/Gaming Set":
                        modExtraArtisanGamingSet.setSelected(true);
                        break;
                    default:
                        modExtraGamingSetInstrument.setSelected(true);
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
            /*
            String racialName = racialAddNameText.getText();

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
                    if (addExtraSkill.isSelected()) {
                        extraProfType = "Skill";
                    } else if (addExtraTool.isSelected()) {
                        extraProfType = "Tool";
                    } else if (addExtraArtisan.isSelected()) {
                        extraProfType = "Artisan";
                    } else if (addExtraGamingSet.isSelected()) {
                        extraProfType = "Gaming Set";
                    } else if (addExtraInstrument.isSelected()) {
                        extraProfType = "Instrument";
                    } else if (addExtraSkillTool.isSelected()) {
                        extraProfType = "Skill/Tool";
                    } else if (addExtraArtisanInstrument.isSelected()) {
                        extraProfType = "Artisan/Instrument";
                    } else if (addExtraArtisanGamingSet.isSelected()) {
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
                    racialAddNameError.setText(ex.getMessage());
                }
                racialDatabaseErrorText.setText("");
                racialAddNameText.setText("");
                racialAddNameError.setText("");
                addRacialStatsText.setText("0");
                addRacialStatsError.setText("");
                addRacialRandomProfs.setText("0");
                racialAddRandomProfsError.setText("");
                addRacialRandomLangs.setText("0");
                racialAddRandomLangsError.setText("");
                addRacialExtraProfs.setText("0");
                racialAddExtraProfsError.setText("");
                addRacialFeat.setSelected(false);
                addExtraSkill.setSelected(true);
                this.modifyWindow.close();
                refreshRacials(racials);
            } else {
                if (racialName.isEmpty()) {
                    racialAddNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    racialAddNameError.setText("");
                }
                if (!isInteger(addRacialStatsText.getText())) {
                    addRacialStatsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    addRacialStatsError.setText("");
                }
                if (!isInteger(addRacialRandomProfs.getText())) {
                    racialAddRandomProfsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    racialAddRandomProfsError.setText("");
                }
                if (!isInteger(addRacialRandomLangs.getText())) {
                    racialAddRandomLangsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    racialAddRandomLangsError.setText("");
                }
                if (!isInteger(addRacialExtraProfs.getText())) {
                    racialAddExtraProfsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    racialAddExtraProfsError.setText("");
                }
            }*/
        });

        backFromAddingRacial.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        modifyThisRacial.setOnAction((event) -> {
            /*String racialName = racialModNameText.getText();

            if (!racialName.isEmpty() && isInteger(modRacialStatsText.getText())) {
                int racialStats = Integer.parseInt(modRacialStatsText.getText());
                boolean racialFeat = modRacialFeat.isSelected();
                int id = racials.getSelectionModel().getSelectedItem().getId();

                Racial racialToMod = new Racial(id, racialName, racialStats, racialFeat);

                ObservableList<Proficiency> racialProfsToMod = modRacialProfTable.
                        getSelectionModel().getSelectedItems();

                for (Proficiency prof : racialProfsToMod) {
                    racialToMod.addRacialProf(prof);
                }
                try {
                    this.generator.updateRacialToDb(racialToMod);
                } catch (SQLException ex) {
                    racialModNameError.setText(ex.getMessage());
                }
                racialModNameText.setText("");
                racialModNameError.setText("");
                modRacialStatsError.setText("");
                modRacialStatsText.setText("0");
                modRacialFeat.setSelected(false);
                this.modifyWindow.close();
                refreshRacials(racials);
            } else {
                if (racialName.isEmpty()) {
                    racialModNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    racialModNameError.setText("");
                }
                if (!isInteger(modRacialStatsText.getText())) {
                    modRacialStatsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    modRacialStatsError.setText("");
                }
            }*/
        });

        backFromModifyingRacial.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        deleteRacial.setOnAction((event) -> {
            deleteRacial(racials, racialDatabaseErrorText);
        });

        HBox classAddLayout = new HBox();

        VBox classAddLeftLayout = new VBox();
        HBox classAddRightLayout = new HBox();

        VBox classAddNameLayout = new VBox();
        Label classAddNameLabel = new Label("Nimi: ");
        TextField classAddNameText = new TextField("");
        Label classAddNameError = new Label("");
        classAddNameError.setTextFill(Color.RED);
        classAddNameLayout.getChildren().addAll(classAddNameLabel, classAddNameText);

        Button addNewClass = new Button("Lisää");
        Button backFromAddingClass = new Button("Takaisin");
        HBox classAddingButtons = new HBox();
        classAddingButtons.getChildren().addAll(addNewClass, backFromAddingClass);

        classAddLeftLayout.getChildren().addAll(classAddNameLayout, classAddNameError,
                classAddingButtons);

        VBox addSubclassTable = new VBox();
        Label addSubclassLabel = new Label("Lisää subclassit");
        ListView<String> addSubclassList = createEmptyList();

        addSubclassTable.getChildren().addAll(addSubclassLabel, addSubclassList);

        TableView<Proficiency> addClassProfTable = createProfTable();

        classAddRightLayout.getChildren().addAll(addSubclassTable, addClassProfTable);

        classAddLayout.getChildren().addAll(classAddLeftLayout, classAddRightLayout);

        HBox classModifyLayout = new HBox();

        VBox classModLeftLayout = new VBox();
        HBox classModRightLayout = new HBox();

        VBox classModNameLayout = new VBox();
        Label classModNameLabel = new Label("Nimi: ");
        TextField classModNameText = new TextField("");
        Label classModNameError = new Label("");
        classModNameError.setTextFill(Color.RED);
        classModNameLayout.getChildren().addAll(classModNameLabel, classModNameText);

        Button modifyThisClass = new Button("Päivitä");
        Button backFromModifyingClass = new Button("Takaisin");
        HBox classModifyingButtons = new HBox();
        classModifyingButtons.getChildren().addAll(modifyThisClass, backFromModifyingClass);

        classModLeftLayout.getChildren().addAll(classModNameLayout, classModNameError,
                classModifyingButtons);

        VBox modSubclassTable = new VBox();
        Label modSubclassLabel = new Label("Lisää subclassit");
        ListView<String> modSubclassList = createEmptyList();
        modSubclassTable.getChildren().addAll(modSubclassLabel, modSubclassList);

        TableView<Proficiency> modClassProfTable = createProfTable();

        classModRightLayout.getChildren().addAll(modSubclassTable, modClassProfTable);

        classModifyLayout.getChildren().addAll(classModLeftLayout, classModRightLayout);

        this.classAddScene = new Scene(classAddLayout);
        this.classModScene = new Scene(classModifyLayout);

        addClass.setOnAction((event) -> {
            /*refreshProfs(addClassProfTable);
            emptyListView(addSubclassList);
            classDatabaseErrorText.setText("");
            classAddNameText.setText("");
            classAddNameError.setText("");
            this.modifyWindow.setTitle("Lisää Class");
            this.modifyWindow.setScene(this.classAddScene);
            this.modifyWindow.show();*/
        });

        modifyExistingClass.setOnAction((event) -> {
            /*refreshProfs(modClassProfTable);
            classDatabaseErrorText.setText("");
            classModNameText.setText("");
            classModNameError.setText("");

            RpgClass classToBeModified = classes.getSelectionModel().getSelectedItem();
            if (!(classToBeModified == null)) {
                racialDatabaseErrorText.setText("");
                this.modifyWindow.setTitle("Muokkaa Classia");
                this.modifyWindow.setScene(this.classModScene);
                this.modifyWindow.show();
                classModNameText.setText(classToBeModified.getName());

                refreshSubclasses(classToBeModified, modSubclassList);

                for (Proficiency classProf : classToBeModified.getClassProfs()) {
                    modClassProfTable.getSelectionModel().select(classProf);
                }
            } else {
                classDatabaseErrorText.setText("Valitse muokattava class!");
            }*/
        });

        addNewClass.setOnAction((event) -> {
            /*String className = classAddNameText.getText();

            if (!className.isEmpty()) {
                RpgClass classToAdd = new RpgClass(className);

                ObservableList<Proficiency> classProfsToAdd = addClassProfTable.
                        getSelectionModel().getSelectedItems();

                ObservableList<String> subclassesToAdd = addSubclassList.getItems();

                for (Proficiency prof : classProfsToAdd) {
                    classToAdd.addClassProf(prof);
                }

                for (String subclass : subclassesToAdd) {
                    if (!(subclass == "")) {
                        classToAdd.addSubclass(subclass.trim());
                    }
                }

                try {
                    this.generator.addNewClassToDb(classToAdd);
                } catch (SQLException ex) {
                    classAddNameError.setText(ex.getMessage());
                }
                classAddNameText.setText("");
                classAddNameError.setText("");
                this.modifyWindow.close();
                refreshClasses(classes);
            } else {
                if (className.isEmpty()) {
                    classAddNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    classAddNameError.setText("");
                }
            }*/
        });

        backFromAddingClass.setOnAction((event) -> {
            /*this.modifyWindow.close();*/
        });

        modifyThisClass.setOnAction((event) -> {
            /*String className = classModNameText.getText();

            if (!className.isEmpty()) {
                int id = classes.getSelectionModel().getSelectedItem().getId();

                RpgClass classToMod = new RpgClass(id, className);

                ObservableList<Proficiency> classProfsToMod = modClassProfTable.
                        getSelectionModel().getSelectedItems();

                ObservableList<String> subclassesToMod = modSubclassList.getItems();

                for (Proficiency prof : classProfsToMod) {
                    classToMod.addClassProf(prof);
                }

                ArrayList<String> newSubclasses = new ArrayList<>();
                for (String subclass : subclassesToMod) {
                    if (!(subclass.trim() == "")) {
                        newSubclasses.add(subclass.trim());
                    }
                }
                classToMod.setSubclasses(newSubclasses);

                try {
                    this.generator.updateClassToDb(classToMod);
                    emptyListView(subclasses);
                } catch (SQLException ex) {
                    classModNameError.setText(ex.getMessage());
                }
                classModNameText.setText("");
                classModNameError.setText("");
                this.modifyWindow.close();
                refreshClasses(classes);
            } else {
                if (className.isEmpty()) {
                    classModNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    classModNameError.setText("");
                }
            }*/
        });

        backFromModifyingClass.setOnAction((event) -> {
            /*this.modifyWindow.close();*/
        });

        deleteClass.setOnAction((event) -> {
            /*deleteClass(classes, subclasses, classDatabaseErrorText);*/
        });

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
