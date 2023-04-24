package oogasalad.frontend.windows;

import java.util.HashMap;
import java.util.Map;
import oogasalad.frontend.windows.WindowTypes.WindowType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Connor Wells
 * @author Owen MacKenzie
 */

public class WindowController implements WindowMediator {

  private Map<String, AbstractWindow> windowMap;
  private int windowIDCounter;

  private static final Logger logger = LoggerFactory.getLogger(WindowController.class);

  public WindowController() {
    windowMap = new HashMap<>();
    registerAndShow(WindowType.SPLASH_WINDOW);
  }

  @Override
  public void registerAndShow(WindowType windowType) {
    showWindow(registerWindow(windowType));
    logger.debug(String.format("Registered a new window: Type - %s",  windowType));
  }

  @Override
  public String registerWindow(WindowType windowType) {
    String windowID = windowType + "_" + windowIDCounter;
    AbstractWindow window = WindowFactory.createWindow(windowType, windowID, this);
    windowIDCounter++;
    windowMap.put(windowID, window);
    window.sceneController.setDefaultScene(window.getDefaultSceneType()); //messy
    return windowID;
  }

  @Override
  public void showWindow(String windowID) {
    getWindow(windowID).show();
    logger.info(String.format("Show a new window: ID - %s",  windowID));
  }

  @Override
  public void closeWindow(String windowID) {
    getWindow(windowID).close();
    windowMap.remove(windowID);
    logger.info(String.format("Close window: ID - %s",  windowID));
  }

  @Override
  public AbstractWindow getWindow(String windowID) {
    return windowMap.get(windowID);
  }

}
