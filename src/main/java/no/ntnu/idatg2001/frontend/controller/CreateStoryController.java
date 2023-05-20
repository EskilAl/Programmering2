package no.ntnu.idatg2001.frontend.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import no.ntnu.idatg2001.backend.gameinformation.Game;
import no.ntnu.idatg2001.backend.gameinformation.GameSave;
import no.ntnu.idatg2001.backend.gameinformation.Story;
import no.ntnu.idatg2001.backend.utility.AlertHelper;
import no.ntnu.idatg2001.backend.gameinformation.StoryFileReader;
import no.ntnu.idatg2001.backend.utility.AlertHelper;
import no.ntnu.idatg2001.dao.GameDAO;
import no.ntnu.idatg2001.dao.GameSaveDAO;
import no.ntnu.idatg2001.dao.StoryDAO;
import no.ntnu.idatg2001.frontend.view.dialogs.AddPassageDialog;
import no.ntnu.idatg2001.frontend.view.CreateStoryView;
import no.ntnu.idatg2001.frontend.view.EditStoryView;
import no.ntnu.idatg2001.frontend.view.MainMenuView;
import no.ntnu.idatg2001.frontend.view.dialogs.NewStoryDialog;

public class CreateStoryController extends Controller<CreateStoryView> {
  private AddPassageDialog addPassageDialog;
  private NewStoryDialog newStoryDialog;

  public CreateStoryController(CreateStoryView view) {
    this.view = view;
    init();
  }

  private void init() {
    configureTableView();
    populateTableView();
  }

  /*
  public void saveStoryTest(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Story");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.Paths"));
    File file = fileChooser.showSaveDialog(createStoryView.getScene().getWindow());
    if (file != null) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        // Iterate through the entries of the HashMap and save the content of each text area
        for (Map.Entry<String, TextArea> entry : createStoryView.getRoomTextAreas().entrySet()) {
          String roomName = entry.getKey();
          TextArea textArea = entry.getValue();
          String text = textArea.getText();
          // Write the room name and the content of the text area to the file
          writer.write("::" + roomName + "\n");
          writer.write(text);
          writer.write("\n\n");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

   */
  public void onEditButton() {
    if (getSelectedItemInTableView() != null) {
      EditStoryView editStoryView = new EditStoryView();
      Scene newScene = view.getScene();
      EditStoryController editStoryController = new EditStoryController(editStoryView);
      editStoryView.setController(editStoryController);
      editStoryController.setSelectedStory(getSelectedItemInTableView());
      newScene.setRoot(editStoryView);
    }
  }

  public void onBackToMainMenuButtonPressed(ActionEvent event) throws IOException {
    MainMenuView mainMenuView = new MainMenuView();
    Scene newScene = view.getScene();
    MainMenuController menuController = new MainMenuController(mainMenuView);
    mainMenuView.setController(menuController);
    newScene.setRoot(mainMenuView);
  }

  public void onCloseSource(ActionEvent event) {
    Node source = (Node) event.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.close();
  }

  public void onNewStory() {
    // Create a new dialog that opens the new story dialog, this story will be
    // saved in the StoryDao.
    newStoryDialog = new NewStoryDialog(this);
    newStoryDialog.initOwner(view.getScene().getWindow());
    newStoryDialog.initStyle(StageStyle.UNDECORATED);
    newStoryDialog.showAndWait();
    populateTableView();
  }

  public void onImportButtonPressed() {
    StoryFileReader storyReader = new StoryFileReader();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Import Story");
    fileChooser.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Paths Files", "*.Paths"));
    File selectedFile = fileChooser.showOpenDialog(view.getScene().getWindow());
    if (selectedFile != null) {
      try {
        Story story = storyReader.readFile(selectedFile.getPath());
        if (story == null) {
          throw new IllegalArgumentException();
        }
        StoryDAO.getInstance().update(story);
        populateTableView();
      } catch (IllegalArgumentException illegalArgumentException) {
          AlertHelper.showErrorAlert(view.getScene().getWindow(), "Error loading file",
              "The file you tried to load is not a valid story file. Make sure format is correct.");
      }
    }
  }

  public void onDeleteButtonPressed() {
    Story story = getSelectedItemInTableView();
    if (story != null) {
      for (GameSave gameSave : GameSaveDAO.getInstance().getAll()) {
        if (gameSave.getGame().getStory().getId().equals(story.getId())) {
          AlertHelper.showWarningAlert(view.getScene().getWindow(), "Story in use",
              "The story you are trying to delete is in use by a game save. Please delete the game save first.");
        }
      }
      StoryDAO.getInstance().remove(story);
      populateTableView();
    }
  }


  private void populateTableView() {
    view.getStoryTableView().getItems().clear();
    List<Story> storylist = StoryDAO.getInstance().getAll();
    ObservableList<Story> list = FXCollections.observableArrayList(storylist);
    view.getStoryTableView().setItems(list);
  }
  public void configureTableView() {
    view.getColumnStoryName().setCellValueFactory(new PropertyValueFactory<>("title"));
    view.getColumnStoryPassageAmount().setCellValueFactory(cell -> {
      Story story = cell.getValue();
      int passageAmount = story.getTotalAmountOfPassages();
      return new SimpleIntegerProperty(passageAmount).asObject();
    });
    view.getColumnStoryLinkAmount().setCellValueFactory(cell -> {
      Story story = cell.getValue();
      int linkAmount = story.getTotalAmountOfPassagesLinks()  +
          story.getOpeningPassage().getLinks().size();
      return new SimpleIntegerProperty(linkAmount).asObject();
    });
  }

  public Story getSelectedItemInTableView() {
    // Get the selected item from the table view
     Story selectedStory = view.getStoryTableView().getSelectionModel().getSelectedItem();
// If no item is selected, show an error message and return
    if (selectedStory == null) {
      AlertHelper.showErrorAlert(view.getScene().getWindow(), view.getResourceBundle().getString("error"),
          view.getResourceBundle().getString("error_select_story"));
      return null;
    }
    return selectedStory;
  }
}
