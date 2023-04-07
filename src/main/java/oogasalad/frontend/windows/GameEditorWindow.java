package oogasalad.frontend.windows;

import oogasalad.frontend.scenes.AbstractScene;
import oogasalad.frontend.scenes.SceneTypes;
import oogasalad.frontend.scenes.GameEditorEditorScene;
import oogasalad.frontend.scenes.GameEditorMainScene;

/**
 * @author Connor Wells
 * @author Owen MacKenzie
 */

public class GameEditorWindow extends AbstractWindow {

  public enum WindowScenes implements SceneTypes {
    MAIN_SCENE,
    EDITOR_SCENE
  }

  public GameEditorWindow(WindowMediator windowController) {
    super(windowController);
  }

  @Override
  public SceneTypes getDefaultSceneType() {
    return WindowScenes.MAIN_SCENE;
  }

  @Override
  public AbstractScene addNewScene(SceneTypes sceneType) {
    if (sceneType.equals(WindowScenes.MAIN_SCENE)) {
      return new GameEditorMainScene(this);
    } else if (sceneType.equals(WindowScenes.EDITOR_SCENE)) {
      return new GameEditorEditorScene(this);
    }
    throw new IllegalArgumentException("Invalid scene type: " + sceneType);

    //TODO: replace with reflection
  }


}
