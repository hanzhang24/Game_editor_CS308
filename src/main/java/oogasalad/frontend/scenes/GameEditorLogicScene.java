package oogasalad.frontend.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import oogasalad.frontend.windows.GameEditorWindow;
import oogasalad.frontend.windows.AbstractWindow;
import oogasalad.frontend.managers.PropertiesManager;

/**
 * @author Connor Wells
 * @author Owen MacKenzie
 * @author George Fang
 */

public class GameEditorLogicScene extends AbstractScene {

    private Button editGridButton;

    private Label gameEditorLabel;

    public GameEditorLogicScene(SceneController sceneController) {
        super(sceneController);
    }

    @Override
    public Scene makeScene() {
        BorderPane root = new BorderPane();

        editGridButton = new Button();
        editGridButton.setOnAction(e -> panelController.newSceneFromPanel("test", GameEditorWindow.WindowScenes.EDITOR_SCENE));

        gameEditorLabel = new Label();
        root.setTop(gameEditorLabel);
        root.setLeft(new VBox(editGridButton));
        setScene(new Scene(root));
        setText();
        setTheme();
        return getScene();
    }

    @Override
    public void setText() {
        editGridButton.setText("visual panel");
        gameEditorLabel.setText("switched to logic scene");
    }


}