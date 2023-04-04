package ntnu.no.idatg2001.FrontEnd.View;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.*;
import javafx.stage.*;
import ntnu.no.idatg2001.FrontEnd.Controller.SettingsController;
import ntnu.no.idatg2001.FrontEnd.Controller.SettingsModel;

public class SettingsView extends Dialog<ButtonType> {

  private SettingsController settingsController;

  private Dialog<ButtonType> dialog;
  private ChoiceBox<String> languageSelection;
  private Slider volumeSlider;
  private CheckBox muteCheckBox;
  private ButtonType saveButtonType;
  private ButtonType cancelButtonType;
  private ResourceBundle resourceBundle;

  private SettingsModel settings = new SettingsModel();

  public SettingsView(SettingsModel model) {
    dialog = new Dialog<>();
    initStyle(StageStyle.UNDECORATED);
    dialog.getDialogPane().getStylesheets().add(("css/ExitConfirmationDialogStyleSheet.css"));
    layout();

  }

  private void createSaveButton() {
    ButtonType saveButton = new ButtonType(resourceBundle.getString("settings.save"), ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().add(saveButton);
    saveButtonType = saveButton;
  }

  private void createCancelButton() {
    ButtonType cancelButton = new ButtonType(resourceBundle.getString("settings.cancel"), ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().add(cancelButton);
    cancelButtonType = cancelButton;
  }

  public void setLanguageSelection(String language) {
    languageSelection.setValue(language);
  }

  public String getLanguageSelection() {
    return languageSelection.getValue();
  }

  public void setVolumeSliderValue(double volumeSliderValue) {
    volumeSlider.setValue(volumeSliderValue);
  }

  public double getVolumeSliderValue() {
    return volumeSlider.getValue();
  }

  public void setMuteCheckBoxSelected(boolean isMuted) {
    muteCheckBox.setSelected(isMuted);
  }

  public boolean isMuteCheckBoxSelected() {
    return muteCheckBox.isSelected();
  }

  public CheckBox getMuteCheckBox() {
    return muteCheckBox;
  }

  public Slider getVolumeSlider() {
    return volumeSlider;
  }
  public ChoiceBox<String> getLanguage() {
    return languageSelection;
  }

  public Stage getDialogStage() {
    return (Stage)dialog.getDialogPane().getScene().getWindow();
  }

  public Scene getDialogScene() {
    return dialog.getDialogPane().getScene();
  }

  public Dialog<ButtonType> getDialog() {
    return dialog;

  }
  public ButtonType getSaveButton() {
    return saveButtonType;
  }
  public ButtonType getCancelButton() {
    return cancelButtonType;
  }
  public GridPane layout() {
    Locale locale = new Locale(settings.getLocale().toString());
    resourceBundle = ResourceBundle.getBundle("exitDialog", locale);
    // Create language selection box
    languageSelection = new ChoiceBox<>();
    languageSelection.getItems().addAll(
        resourceBundle.getString("settings.language.english"),
        resourceBundle.getString("settings.language.norwegian"),
        resourceBundle.getString("settings.language.french"));
    languageSelection.setValue(resourceBundle.getString("settings.language.selected"));

    // Create volume slider
    volumeSlider = new Slider(0, 100, 50);
    volumeSlider.setShowTickLabels(false);
    volumeSlider.setShowTickMarks(false);
    volumeSlider.setMajorTickUnit(50);
    volumeSlider.setMinorTickCount(5);
    volumeSlider.setBlockIncrement(10);

    // Create mute check box
    muteCheckBox = new CheckBox(resourceBundle.getString("settings.muteLable"));
    // Create layout and add components
    GridPane layout = new GridPane();
    layout.setHgap(10);
    layout.setVgap(10);
    layout.addRow(0, new Label(resourceBundle.getString("settings.language")), languageSelection);
    layout.addRow(1, new Label(resourceBundle.getString("settings.volume")), volumeSlider);
    layout.addRow(2, new Label(resourceBundle.getString("settings.mute")), muteCheckBox);
    dialog.getDialogPane().setContent(layout);

    // Add buttons to dialog
    createSaveButton();
    createCancelButton();
    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType,cancelButtonType);
    return layout;
  }
}

