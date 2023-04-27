package oogasalad.frontend.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import oogasalad.frontend.windows.WindowTypes;

/**
 * @author Connor Wells
 * @author Owen MacKenzie
 */

public class SplashMainScene extends AbstractScene {

  private Button playButton, editButton, modalButton;

  public SplashMainScene(SceneController sceneController) {
    super(sceneController);
  }

  @Override
  public Scene makeScene() {
    StackPane root = new StackPane();
    VBox vbox = new VBox(10);
    playButton = new Button();
    playButton.setOnAction(
        e -> panelController.getSceneController().getWindowController()
            .registerAndShow(WindowTypes.WindowType.LIBRARY_WINDOW));
    editButton = new Button();
    editButton.setOnAction(
        e -> panelController.getSceneController().getWindowController()
            .registerAndShow(WindowTypes.WindowType.EDIT_WINDOW));
    modalButton = new Button(); //ADD NEW WINDOW
    modalButton.setOnAction(
        e -> panelController.getSceneController().getWindowController()
            .registerAndShow(WindowTypes.WindowType.MODAL_WINDOW));
    ComboBox<String> languageDropdown = new ComboBox<>();
    languageDropdown.getItems().addAll("english", "spanish");
    languageDropdown.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          getPropertyManager().setTextResources(newValue);
        });
    languageDropdown.setValue("english");
    ComboBox<String> themeDropdown = new ComboBox<>();
    themeDropdown.getItems().addAll("light", "dark");
    themeDropdown.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          getThemeManager().setTheme(newValue);
        });
    themeDropdown.setValue("light");
    vbox.getChildren().addAll(playButton, editButton, modalButton, languageDropdown, themeDropdown);
    vbox.setAlignment(Pos.CENTER);
    root.getChildren().add(vbox);
    setScene(new Scene(root));
    setText();
    setTheme();
    return getScene();
  }

  @Override
  public void setText() {
    playButton.setText(getPropertyManager().getText("SplashMainScene.PlayGameButton"));
    editButton.setText(getPropertyManager().getText("SplashMainScene.EditGameButton"));
    modalButton.setText(getPropertyManager().getText("SplashMainScene.ModalButton"));
  }
}
