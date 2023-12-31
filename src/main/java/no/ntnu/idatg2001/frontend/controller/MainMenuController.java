package no.ntnu.idatg2001.frontend.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import no.ntnu.idatg2001.backend.entityinformation.Unit;
import no.ntnu.idatg2001.backend.gameinformation.GameSave;
import no.ntnu.idatg2001.backend.SettingsModel;
import no.ntnu.idatg2001.backend.utility.AlertHelper;
import no.ntnu.idatg2001.dao.GameDAO;
import no.ntnu.idatg2001.dao.GameSaveDAO;
import no.ntnu.idatg2001.dao.UnitDAO;
import no.ntnu.idatg2001.frontend.view.CreateStoryView;
import no.ntnu.idatg2001.frontend.view.dialogs.ExitDialog;
import no.ntnu.idatg2001.frontend.view.GameView;
import no.ntnu.idatg2001.frontend.view.dialogs.LoadGameDialog;
import no.ntnu.idatg2001.frontend.view.MainMenuView;
import no.ntnu.idatg2001.frontend.view.dialogs.NewGameDialog;
import no.ntnu.idatg2001.frontend.view.dialogs.SettingsDialog;
import no.ntnu.idatg2001.frontend.view.StartNewGameView;

public class MainMenuController extends Controller<MainMenuView> {
  private SettingsDialog settingsDialog;
  private LoadGameDialog loadGameDialog;

  public MainMenuController(MainMenuView view) {
    this.view = view;
  }

  public void onStartGameButtonPressed(ActionEvent event) {
    StartNewGameController startNewGameController;
    StartNewGameView startNewGameView;
    startNewGameView = new StartNewGameView();
    startNewGameController = new StartNewGameController(startNewGameView);
    startNewGameView.setController(startNewGameController);
    Scene newScene = view.getScene();
    onCloseSource(event);
    newScene.setRoot(startNewGameView);
  }

  public void onNewGameButtonPressed() {
    NewGameDialog newGameDialog = new NewGameDialog(this);
    newGameDialog.initOwner(view.getScene().getWindow());
    newGameDialog.showAndWait();
  }

  public void onCreateStoryButtonPressed(ActionEvent event) throws IOException {
    CreateStoryView createStoryView = new CreateStoryView();
    CreateStoryController createStoryController = new CreateStoryController(createStoryView);
    createStoryView.setController(createStoryController);
    Scene newScene = view.getScene();
    onCloseSource(event);
    newScene.setRoot(createStoryView);
  }

  @Override
  public void onLoadGameButtonPressed(ActionEvent event) {
    loadGameDialog = new LoadGameDialog(this);
    loadGameDialog.setDeleteButtonDisabledProperty(true);
    configureSavedGamesTableView(event);
    populateSavedGamesTableView(event);
    loadGameDialog.initOwner(view.getScene().getWindow());
    loadGameDialog.showAndWait();
  }

  @Override
  public void populateSavedGamesTableView(ActionEvent event) {
    event.consume();
    loadGameDialog.getSavedGamesTableView().getItems().clear();
    List<GameSave> gameSaves = GameSaveDAO.getInstance().getAll();
    Collections.reverse(gameSaves);
    loadGameDialog.getSavedGamesTableView()
        .setItems(FXCollections.observableArrayList(gameSaves));
  }

  @Override
  public void configureSavedGamesTableView(ActionEvent event) {
    event.consume();
    loadGameDialog.getNameColumn().setCellValueFactory(new PropertyValueFactory<>("storyAndLastPassage"));
    loadGameDialog.getDateTimeColumn().setCellValueFactory(new PropertyValueFactory<>("timeOfSaveFormatted"));
    loadGameDialog.getPlayerColumn().setCellValueFactory(new PropertyValueFactory<>("playerName"));
  }

  public void onSettingsViewButtonPressed() {
    settingsDialog = new SettingsDialog(this);
    settingsDialog.initOwner(view.getScene().getWindow());
    settingsDialog.showAndWait();
  }

  @Override
  public void onSettingSaveButtonPressed(ActionEvent event) {
    // Update the model with new settings data
    SettingsModel.getInstance().setLanguageSelection(settingsDialog.getLanguageSelection());
    SettingsModel.getInstance().setVolumeSliderValue(settingsDialog.getVolumeSliderValue());
    SettingsModel.getInstance().setMuted(settingsDialog.isMuteCheckBoxSelected());

    // Save the settings data to a file.
    SettingsModel.getInstance().saveSettings();

    //updates Main Menu.
    view.updateMainMenu();

    // Close the settings dialog.
    onCloseSource(event);
  }

  //TODO fix so all onSomething methods takes an event as a parameter and consume if not used.
  public void onExitViewButtonPressed(ActionEvent event) {
    event.consume();
    ExitDialog exitDialog = new ExitDialog(this);
    exitDialog.initOwner(view.getScene().getWindow());
    exitDialog.showAndWait();
  }

  @Override
  public void onLoadSelectedGame(ActionEvent event) {
    if (loadGameDialog.getSelectedGameSave() != null) {
      GameSave selectedGameSave = loadGameDialog.getSelectedGameSave();
      UnitDAO unitDAO = UnitDAO.getInstance();
      Optional<Unit> saveUnitOptional = unitDAO.find(selectedGameSave.getGame().getUnit().getId());

      if (saveUnitOptional.isPresent()) {
        Unit saveUnit = saveUnitOptional.get();
        // Set the parameters from the found unit to the unit in the selected game save
        selectedGameSave.getGame().getUnit().setUnitName(saveUnit.getUnitName());
        selectedGameSave.getGame().getUnit().setUnitHealth(saveUnit.getUnitHealth());
        selectedGameSave.getGame().getUnit().setUnitScore(saveUnit.getUnitScore());
        selectedGameSave.getGame().getUnit().setUnitMana(saveUnit.getUnitMana());
        selectedGameSave.getGame().getUnit().setGold(saveUnit.getGold());
        selectedGameSave.getGame().getUnit().setArmour(saveUnit.getArmour());
        selectedGameSave.getGame().getUnit().clearInventory();
        selectedGameSave.getGame().getUnit().addToInventory(saveUnit.getUnitInventory());
        selectedGameSave.getGame().getUnit().setDamage(saveUnit.getDamage());

        GameView gameView = new GameView();
        GameController gameController = new GameController(gameView, selectedGameSave);
        gameView.setController(gameController);
        gameController.populatePlayerInventoryListView();
        gameController.updatePlayerStats();
        Scene newScene = view.getScene();
        newScene.setRoot(gameView);
        onCloseSource(event);
      } else {
        // Handle case when the unit cannot be found in the database
        AlertHelper.showInformationAlert(loadGameDialog.getDialogPane().getScene().getWindow(),
            loadGameDialog.getResourceBundle().getString("loadGameErrorTitle"),
            loadGameDialog.getResourceBundle().getString("unitNotFoundError"));
      }
    } else {
      AlertHelper.showInformationAlert(loadGameDialog.getDialogPane().getScene().getWindow(),
          loadGameDialog.getResourceBundle().getString("loadGameErrorTitle"),
          loadGameDialog.getResourceBundle().getString("noGameSelectedError"));
    }
  }

  @Override
  public void onDeleteGameButton(ActionEvent event) {
    GameSave selectedGameSave = loadGameDialog.getSelectedGameSave();
    if (selectedGameSave != null) {
      GameSaveDAO.getInstance().remove(selectedGameSave);
      populateSavedGamesTableView(event);
    } else {
      AlertHelper.showInformationAlert(loadGameDialog.getDialogPane().getScene().getWindow(),
          loadGameDialog.getResourceBundle().getString("loadGameErrorTitle"),
          loadGameDialog.getResourceBundle().getString("noGameSelectedError"));
    }
  }
}
