package oogasalad.gameeditor.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import oogasalad.Controller.ConvertingStrategy;
import oogasalad.frontend.components.Component;
import oogasalad.frontend.components.ComponentsFactory;
import oogasalad.frontend.components.gameObjectComponent.GameObject;
import oogasalad.frontend.components.textObjectComponent.TextObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class ConvertingStrategyTest extends DukeApplicationTest {
  private ConvertingStrategy converter;

  private ComponentsFactory factory;
  private Pane root;

  @Override
  public void start(Stage stage){
    converter = new ConvertingStrategy();
    factory = new ComponentsFactory();
    root = new Pane();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
  @Test
  public void sendGameObject(){
    TextObject c = (TextObject) factory.create("TextObject");
    Map<String, String> map = converter.paramsToMap(c);
    System.out.println(map);
    assertEquals(c.getID(), 0);
  }
}
