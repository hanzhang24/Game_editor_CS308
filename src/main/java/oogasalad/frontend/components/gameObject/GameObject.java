package oogasalad.frontend.components.gameObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.image.Image;
import oogasalad.frontend.components.AbstractComponent;


/**
 * @author Han, Aryan
 * Concrete Class for GameObject, a reflection of what is going to be a "GameObject" on the backend
 */
public class GameObject extends AbstractComponent implements GameObjectComponent{
  private String name;
  private List<Node> children;
  private boolean playable;
  private final String DEFAULT_FILE_PATH = "frontend.properties.Defaults.GameObject";
  private ResourceBundle DEFAULT_BUNDLE = ResourceBundle.getBundle(DEFAULT_FILE_PATH);

  public GameObject(int ID){
    super(ID);
    children = null;
    Image newImage = new Image(DEFAULT_BUNDLE.getString("DEFAULT_IMAGE"));
    setImage(DEFAULT_BUNDLE.getString("DEFAULT_IMAGE"));
    followMouse();
  }
  public GameObject(int ID, Node container){
    super(ID, container);
  }

  //TODO fix default values for map constructor
  public GameObject(int ID, Map<String, String> map){
    super(ID);
    children = null;
    setImage(DEFAULT_BUNDLE.getString("DEFAULT_IMAGE"));
    followMouse();
    for(String param: map.keySet()){
      try{
        Field field = this.getClass().getDeclaredField(param);
        field.setAccessible(true);
        Class<?> fieldType = field.getType();

        Object value = fieldType.cast(map.get(param));
        field.set(this, value);
      } catch (Exception e){
        e.printStackTrace();
      }
    }
  }
  @Override
  public void setName(String newName) {
    name = newName;
  }

  @Override
  public List<Node> getChildren() {
    return children;
  }

  @Override
  public void setPlayable(boolean play) {
    playable = play;
  }

  @Override
  public Node getNode(){
    return getImage();
  }
}