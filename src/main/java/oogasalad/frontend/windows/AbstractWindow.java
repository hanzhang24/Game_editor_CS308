package oogasalad.frontend.windows;

import java.util.HashMap;
import java.util.Map;
import javafx.stage.Stage;
import oogasalad.frontend.scenes.SceneTypes;
import oogasalad.frontend.managers.PropertiesManager;
import oogasalad.frontend.scenes.AbstractScene;

/**
 * @author Connor Wells
 * @author Owen MacKenzie
 */

public abstract class AbstractWindow extends Stage {
  private static final String MAIN_ID = "main";

  protected WindowMediator windowController;
  protected Map<String, AbstractScene> scenes;

  //protected Manager manager = PropertiesFactory.createManager(); //TODO: pass in factory DI

  public AbstractWindow(WindowMediator windowController) {
    this.windowController = windowController;
    scenes = new HashMap<>();
    SceneTypes mainSceneType = getDefaultSceneType();
    addAndLinkScene(mainSceneType,MAIN_ID);
    switchToScene(MAIN_ID);
    setWidth(PropertiesManager.getNumeric("WindowHeight"));
    setHeight(PropertiesManager.getNumeric("WindowWidth"));
  }

  protected abstract SceneTypes getDefaultSceneType();
  protected abstract AbstractScene addNewScene(SceneTypes sceneType);

  public void addAndLinkScene(SceneTypes sceneType, String sceneID){
    AbstractScene newScene = addNewScene(sceneType);
    scenes.put(sceneID,newScene);
  }

  public void switchToScene(String sceneID) {
    setScene(scenes.get(sceneID).makeScene());
  }

  public WindowMediator getWindowController() {
    return windowController;
  }
}