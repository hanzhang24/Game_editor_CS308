package oogasalad.windowscene;

import java.util.HashMap;
import java.util.Map;
import oogasalad.windowscene.WindowTypes.WindowType;

public class WindowController implements WindowMediator {

  private Map<String, AbstractWindow> windowMap;
  private int windowIDCounter;

  public WindowController() {
    windowMap = new HashMap<>();
    registerAndShow(WindowType.SPLASH_WINDOW);
  }

  @Override
  public void registerAndShow(WindowType windowType){
    showWindow(registerWindow(windowType));
  }
  @Override
  public String registerWindow(WindowType windowType) {
    AbstractWindow window = WindowFactory.createWindow(windowType, this);
    windowIDCounter++;
    String windowID = windowType + "_" + windowIDCounter;
    windowMap.put(windowID, window);
    return windowID;
  }

  @Override
  public void showWindow(String windowID) {
    windowMap.get(windowID).show();
  }

  @Override
  public void closeWindow(String windowID) {
    windowMap.get(windowID).close();
    windowMap.remove(windowID);
  }

  @Override
  public void receiveMessage() {
    System.out.println("Received message!");

  }
}