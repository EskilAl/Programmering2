package no.ntnu.idatg2001.frontend.view;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import no.ntnu.idatg2001.backend.SettingsModel;
import no.ntnu.idatg2001.backend.gameinformation.Story;
import no.ntnu.idatg2001.frontend.controller.CreateStoryController;

public class CreateStoryView extends BorderPane {

  private static final String cssFile = "/CSS/NewStoryView.css";
  private ButtonBar buttonBar;
  private TableView<Story> storyTableView;
  private TableColumn<Story, String> columnStoryName;
  private TableColumn<Story, Integer> columnStoryPassageAmount;
  private TableColumn<Story, Integer> columnStoryLinkAmount;
  private CreateStoryController controller;
  private JFXButton storyNameButton;
  private JFXButton editStoryButton;
  private JFXButton deleteButton;
  private JFXButton importButton;
  private JFXButton backButton;
  private ResourceBundle resourceBundle;

  public CreateStoryView() {
    Locale locale = new Locale(SettingsModel.getInstance().getLocale().toString());
    resourceBundle = ResourceBundle.getBundle("languages/createStoryView", locale);
    setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.9), null, null)));
    this.setController(controller);
    // Initialize the view
    getStylesheets().add(cssFile);
    init();
  }

  private void init() {
    // Set up the Objects in the View
    createNewStoryNameButton();
    createEditStoryButton();
    createImportStoryButton();
    createDeleteButton();
    createBackButton();
    createStoryTableView();

    buttonBar = new ButtonBar();
    buttonBar.setButtonMinWidth(10);
    buttonBar.getButtons().addAll(storyNameButton,editStoryButton, deleteButton, importButton, backButton);
    HBox buttonHbox = new HBox(buttonBar);
    buttonHbox.setAlignment(Pos.BOTTOM_CENTER);
    buttonHbox.setPadding(new Insets(25,0,0,0));


    // Set up the layout
    setPadding(new Insets(130,100,0,100));

    VBox centerBox = new VBox();

    centerBox.getChildren().addAll(storyTableView,buttonHbox);
    centerBox.setAlignment(Pos.TOP_CENTER);
    setCenter(centerBox);
  }

  private void createStoryTableView() {
    createStoryTableColumnName();
    createStoryTableColumnPassageAmount();
    createStoryTableColumnLinkAmount();
    storyTableView = new TableView<>();
    storyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    storyTableView.getColumns().addAll(columnStoryName,columnStoryPassageAmount,columnStoryLinkAmount);

  }

  private void createStoryTableColumnName() {
    columnStoryName = new TableColumn<>(resourceBundle.getString("newStoryView.tableName"));
    columnStoryName.setPrefWidth(100);
    columnStoryName.setReorderable(false);
  }

  private void createStoryTableColumnPassageAmount() {
    columnStoryPassageAmount = new TableColumn<>(resourceBundle.getString("newStoryView.tablePassageAmount"));
    columnStoryPassageAmount.setPrefWidth(100);
    columnStoryPassageAmount.setReorderable(false);
  }

  private void createStoryTableColumnLinkAmount() {
    columnStoryLinkAmount = new TableColumn<>(resourceBundle.getString("newStoryView.tableLinkAmount"));
    columnStoryLinkAmount.setPrefWidth(100);
    columnStoryLinkAmount.setReorderable(false);
  }

  private void createNewStoryNameButton() {
    storyNameButton = new JFXButton(resourceBundle.getString("newStoryView.newStoryButton"));
    storyNameButton.setOnAction(event -> {
      controller.onNewStory();
    });
  }

  private void createEditStoryButton() {
    editStoryButton = new JFXButton(resourceBundle.getString("newStoryView.editStoryButton"));
    editStoryButton.setOnAction(event -> {
      controller.onEditButton();
    });
  }

  private void createImportStoryButton() {
    importButton = new JFXButton(resourceBundle.getString("newStoryView.loadStoryButton"));
    importButton.setOnAction(event -> controller.onImportButtonPressed());
  }

  public void createDeleteButton() {
    deleteButton = new JFXButton(resourceBundle.getString("newStoryView.deleteStoryButton"));
    deleteButton.setOnAction(event -> controller.onDeleteButtonPressed());
  }

  private void createBackButton() {
    backButton = new JFXButton(resourceBundle.getString("newStoryView.backButton"));
    backButton.setCancelButton(true);
    backButton.setOnAction(event -> {
      try {
        controller.onBackToMainMenuButtonPressed();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  public void setController(CreateStoryController createStoryController) {
    this.controller = createStoryController;
  // Add any additional methods, event handlers, or getters/setters as needed
  }

  public JFXButton getBackButton() {
    return backButton;
  }
  public JFXButton getImportButton() {
    return importButton;
  }
  public JFXButton getStoryNameButton() {
    return storyNameButton;
  }

  public TableView<Story> getStoryTableView() {
    return storyTableView;
  }

  public TableColumn<Story, String> getColumnStoryName() {
    return columnStoryName;
  }

  public TableColumn<Story, Integer> getColumnStoryPassageAmount() {
    return columnStoryPassageAmount;
  }

  public TableColumn<Story, Integer> getColumnStoryLinkAmount() {
    return columnStoryLinkAmount;
  }

  public ResourceBundle getResourceBundle() {
    return resourceBundle;
  }
}

