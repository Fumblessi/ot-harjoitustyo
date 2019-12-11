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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import java.util.Properties;
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
        //luodaan alkunäkymä

        HBox buttons = new HBox();
        Button generate = new Button("Generoi");
        Button settingsButton = new Button("Asetukset");
        buttons.getChildren().addAll(generate, settingsButton);
        layout.setTop(buttons);
        //luodaan alkunäkymän painikkeet

        VBox settingsLayout = new VBox();
        settingsLayout.setPrefSize(600, 400);
        this.settingsScene = new Scene(settingsLayout);
        //luodaan asetusnäkymä

        Button back = new Button("Tallenna ja palaa");
        Button setDefault = new Button("Palauta alkuperäiset");
        MenuBar databaseBar = new MenuBar();
        Menu database = new Menu("Tietokanta");
        databaseBar.getMenus().add(database);

        MenuItem modifyProf = new MenuItem("Proficiency");
        MenuItem modifyRacial = new MenuItem("Racial");
        MenuItem modifyClass = new MenuItem("Class");
        MenuItem modifyBg = new MenuItem("Background");
        MenuItem modifyFeat = new MenuItem("Feat");
        database.getItems().addAll(modifyProf, modifyRacial, modifyClass,
                modifyBg, modifyFeat);

        HBox settingsButtons = new HBox();
        settingsButtons.getChildren().addAll(back, setDefault, databaseBar);
        //luodaan asetusnäkymän painikkeet

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
        //luodaan asetusnäkymän asetusvaihtoehdot

        settingsButton.setOnAction((event) -> {
            settingsLayout.getChildren().addAll(settingsButtons, statPool, statPoolError,
                    statLimits, statLimitError, racialBonus);
            this.primaryWindow.setScene(this.settingsScene);
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
        //asetukset-ikkunasta paluu, uusien asetusten tarkistaminen ja tallennus

        setDefault.setOnAction((event) -> {
            statPoolAmount.setText("70");
            statPoolVarAmount.setText("5");
            statMinAmount.setText("8");
            statMaxAmount.setText("18");
            racialBonus.setSelected(true);
        });
        //oletus-asetusten palautus

        this.databaseWindow = new Stage();
        this.databaseWindow.setTitle("Tietokanta");

        VBox profAddLayout = new VBox();

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

        VBox profModifyLayout = new VBox();

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
        profModifyLayout.getChildren().addAll(profModNameLayout, profModNameError,
                profModTypeLayout, profModifyingButtons);

        VBox profDatabaseLayout = new VBox();
        TableView<Proficiency> profs = new TableView();

        TableColumn<Proficiency, String> profNameColumn = new TableColumn<>("Nimi");
        profNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        profNameColumn.prefWidthProperty().bind(profs.widthProperty().multiply(0.5));
        
        TableColumn<Proficiency, String> profTypeColumn = new TableColumn<>("Tyyppi");
        profTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        profTypeColumn.prefWidthProperty().bind(profs.widthProperty().multiply(0.5));

        profs.getSortOrder().add(profTypeColumn);
        profs.getColumns().setAll(profNameColumn, profTypeColumn);

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
        TableView<String> subclasses = new TableView();

        TableColumn<RpgClass, String> classNameColumn = new TableColumn<>("Nimi");
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<String, String> subClassNameColumn = new TableColumn<>("Subclass");

        classes.getColumns().setAll(classNameColumn);
        subclasses.getColumns().setAll(subClassNameColumn);
        
        classTables.getChildren().addAll(classes, subclasses);

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

        TableColumn<Feat, String> featTypeColumn = new TableColumn<>("Stats");
        featTypeColumn.setCellValueFactory(new PropertyValueFactory<>("stats"));

        feats.getColumns().setAll(featNameColumn, featTypeColumn);

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
            profAddNameText.setText("");
            profAddNameError.setText("");
            addTypeSkill.setSelected(true);
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
                        break;
                    case "Armor":
                        modTypeArmor.setSelected(true);
                        break;
                    case "Weapon":
                        modTypeWeapon.setSelected(true);
                        break;
                    case "Tool":
                        modTypeTool.setSelected(true);
                        break;
                    default:
                        modTypeLanguage.setSelected(true);
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
            if (addTypeLanguage.isSelected()) {
                profType = "Language";
            } else if (addTypeArmor.isSelected()) {
                profType = "Armor";
            } else if (addTypeWeapon.isSelected()) {
                profType = "Weapon";
            } else if (addTypeTool.isSelected()) {
                profType = "Tool";
            } else {
                profType = "Skill";
            }
            if (!profName.isEmpty()) {
                try {
                    this.generator.addNewProfToDb(new Proficiency(profName, profType));
                } catch (SQLException ex) {
                    profAddNameError.setText(ex.getMessage());
                }
                profAddNameError.setText("");
                addTypeSkill.setSelected(true);
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
            if (modTypeLanguage.isSelected()) {
                profType = "Language";
            } else if (modTypeArmor.isSelected()) {
                profType = "Armor";
            } else if (modTypeWeapon.isSelected()) {
                profType = "Weapon";
            } else if (modTypeTool.isSelected()) {
                profType = "Tool";
            } else {
                profType = "Skill";
            }
            int id = profs.getSelectionModel().getSelectedItem().getId();
            Proficiency newProf = new Proficiency(id, profName, profType);
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
            try {
                Proficiency profToBeDeleted = profs.getSelectionModel().getSelectedItem();
                this.generator.deleteProfFromDb(profToBeDeleted);
                profDatabaseErrorText.setText("");
            } catch (Exception e) {
                String errorText = "Valitse poistettava proficiency!";
                if (!(e.getMessage() == null)) {
                    errorText += e.getMessage();
                }
                profDatabaseErrorText.setText(errorText);
            }
            refreshProfs(profs);
        });
        
        HBox racialAddLayout = new HBox();
        
        VBox racialAddLeftLayout = new VBox();
        HBox racialAddRightLayout = new HBox();
        
        VBox racialAddNameLayout = new VBox();
        Label racialAddNameLabel = new Label("Nimi: ");
        TextField racialAddNameText = new TextField("");
        Label racialAddNameError = new Label("");
        racialAddNameError.setTextFill(Color.RED);
        racialAddNameLayout.getChildren().addAll(racialAddNameLabel, racialAddNameText);
        
        VBox addRacialStats = new VBox();
        Label addRacialStatsLabel = new Label("Racial antaa statteja: ");
        TextField addRacialStatsText = new TextField("0");
        Label racialStatsError = new Label("");
        racialStatsError.setTextFill(Color.RED);
        addRacialStats.getChildren().addAll(addRacialStatsLabel, addRacialStatsText, 
                racialStatsError);
        
        CheckBox addRacialFeat = new CheckBox("Racial antaa featin");
        
        Button addNewRacial = new Button("Lisää");
        Button backFromAddingRacial = new Button("Takaisin");
        HBox racialAddingButtons = new HBox();    
        racialAddingButtons.getChildren().addAll(addNewRacial, backFromAddingRacial);
        
        racialAddLeftLayout.getChildren().addAll(racialAddNameLayout, racialAddNameError, 
                addRacialStats, addRacialFeat, racialAddingButtons);
        
        TableView<Proficiency> racialProfTable = new TableView();
        
        TableColumn<Proficiency, String> racialProfNameColumn = new TableColumn<>(""
                + "Valitse proficiencyt");
        racialProfNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        racialProfNameColumn.prefWidthProperty().bind(racialProfTable.widthProperty());
        
        racialProfTable.getColumns().setAll(racialProfNameColumn);
        racialProfTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        racialProfTable.getSortOrder().add(racialProfNameColumn);
        
        racialAddRightLayout.getChildren().addAll(racialProfTable);
        
        racialAddLayout.getChildren().addAll(racialAddLeftLayout, racialAddRightLayout);
        
        HBox racialModifyLayout = new HBox();
        
        VBox racialModLeftLayout = new VBox();
        VBox racialModRightLayout = new VBox();
        
        VBox racialModNameLayout = new VBox();
        Label racialModNameLabel = new Label("Nimi: ");
        TextField racialModNameText = new TextField("");
        Label racialModNameError = new Label("");
        racialModNameError.setTextFill(Color.RED);
        racialModNameLayout.getChildren().addAll(racialModNameLabel, racialModNameText);
        
        CheckBox modRacialFeat = new CheckBox("Racial antaa featin");
        
        VBox modRacialStats = new VBox();
        Label modRacialStatsLabel = new Label("Racial antaa statteja: ");
        TextField modRacialStatsText = new TextField("0");
        Label modRacialStatsError = new Label("");
        modRacialStatsError.setTextFill(Color.RED);
        modRacialStats.getChildren().addAll(modRacialStatsLabel, modRacialStatsText, 
                modRacialStatsError);
        
        Button modifyThisRacial = new Button("Päivitä");
        Button backFromModifyingRacial = new Button("Takaisin");      
        HBox racialModifyingButtons = new HBox();     
        racialModifyingButtons.getChildren().addAll(modifyThisRacial, backFromModifyingRacial);
        
        racialModLeftLayout.getChildren().addAll(racialModNameLayout, racialModNameError, 
                modRacialStats, modRacialFeat, racialModifyingButtons);
        
        TableView<Proficiency> modRacialProfTable = new TableView();
        
        TableColumn<Proficiency, String> modRacialProfNameColumn = new TableColumn<>(""
                + "Valitse proficiencyt");
        modRacialProfNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modRacialProfNameColumn.prefWidthProperty().bind(modRacialProfTable.widthProperty());
        
        modRacialProfTable.getColumns().setAll(modRacialProfNameColumn);
        modRacialProfTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        modRacialProfTable.getSortOrder().add(modRacialProfNameColumn);
        
        racialModRightLayout.getChildren().addAll(modRacialProfTable);
        
        racialModifyLayout.getChildren().addAll(racialModLeftLayout, racialModRightLayout);
        
        this.racialAddScene = new Scene(racialAddLayout);
        this.racialModScene = new Scene(racialModifyLayout);

        addRacial.setOnAction((event) -> {
            refreshProfs(racialProfTable);
            racialDatabaseErrorText.setText("");
            racialAddNameText.setText("");
            racialAddNameError.setText("");
            addRacialStatsText.setText("0");
            addRacialFeat.setSelected(false);
            this.modifyWindow.setTitle("Lisää Racial");
            this.modifyWindow.setScene(this.racialAddScene);
            this.modifyWindow.show();
        });

        modifyExistingRacial.setOnAction((event) -> {
            refreshProfs(modRacialProfTable);
            racialModNameText.setText("");
            Racial racialToBeModified = racials.getSelectionModel().getSelectedItem();
            if (!(racialToBeModified == null)) {
                racialDatabaseErrorText.setText("");
                this.modifyWindow.setTitle("Muokkaa Racialia");
                this.modifyWindow.setScene(this.racialModScene);
                this.modifyWindow.show();
                racialModNameText.setText(racialToBeModified.getName());
                String oldStats = "" + racialToBeModified.getStats();
                modRacialStatsText.setText(oldStats);
                modRacialFeat.setSelected(racialToBeModified.getFeat());

                for (Proficiency racialProf : racialToBeModified.getRacialProfs()) {
                    modRacialProfTable.getSelectionModel().select(racialProf);
                }
            } else {
                racialDatabaseErrorText.setText("Valitse muokattava proficiency!");
            }
        });

        addNewRacial.setOnAction((event) -> {
            String racialName = racialAddNameText.getText();
            
            if (!racialName.isEmpty() && isInteger(addRacialStatsText.getText())) {
                int racialStats = Integer.parseInt(addRacialStatsText.getText());
                boolean racialFeat = addRacialFeat.isSelected();
                Racial racialToAdd = new Racial(racialName, racialStats, racialFeat);
                
                ObservableList<Proficiency> racialProfsToAdd = racialProfTable.
                        getSelectionModel().getSelectedItems();
                
                for (Proficiency prof : racialProfsToAdd) {
                    racialToAdd.addRacialProf(prof);
                }
                try {
                    this.generator.addNewRacialToDb(racialToAdd);
                } catch (SQLException ex) {
                    racialAddNameError.setText(ex.getMessage());
                }
                racialAddNameText.setText("");
                racialAddNameError.setText("");
                racialStatsError.setText("");
                addRacialStatsText.setText("0");
                addRacialFeat.setSelected(false);
                this.modifyWindow.close();
                refreshRacials(racials);
            } else {
                if (racialName.isEmpty()) {
                    racialAddNameError.setText("Syöte ei voi olla tyhjä!");
                } else {
                    racialAddNameError.setText("");
                }
                if (!isInteger(addRacialStatsText.getText())) {
                    racialStatsError.setText("Syötteen täytyy olla kokonaisluku");
                } else {
                    racialStatsError.setText("");
                }
            }
        });

        backFromAddingRacial.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        modifyThisRacial.setOnAction((event) -> {
            String racialName = racialModNameText.getText();
            
            if (!racialName.isEmpty() && isInteger(modRacialStatsText.getText())) {
                int racialStats = Integer.parseInt(modRacialStatsText.getText());
                boolean racialFeat = modRacialFeat.isSelected();
                Racial racialToMod = new Racial(racialName, racialStats, racialFeat);
                
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
            }
        });

        backFromModifyingRacial.setOnAction((event) -> {
            this.modifyWindow.close();
        });

        deleteRacial.setOnAction((event) -> {
            try {
                Racial racialToBeDeleted = racials.getSelectionModel().getSelectedItem();
                this.generator.deleteRacialFromDb(racialToBeDeleted);
                racialDatabaseErrorText.setText("");
            } catch (Exception e) {
                String errorText = "Valitse poistettava racial!";
                if (!(e.getMessage() == null)) {
                    errorText += e.getMessage();
                }
                racialDatabaseErrorText.setText(errorText);
            }
            refreshRacials(racials);
        });
        //HUOM HUOM CLASSIEN MUOKKAUS JA LISÄYS JA POISTO ALKAA
        
        //HUOM HUOM RACIALIEN MUOKKAUS JA LISÄYS ALKAA

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

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
