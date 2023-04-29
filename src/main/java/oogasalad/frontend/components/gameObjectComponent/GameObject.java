package oogasalad.frontend.components.gameObjectComponent;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import oogasalad.frontend.components.AbstractComponent;
import oogasalad.frontend.components.Component;
import oogasalad.frontend.components.ConversionContext;
import oogasalad.frontend.components.ParamFactory;
import oogasalad.frontend.components.Point;


/**
 * @author Han, Aryan Concrete Class for GameObject, a reflection of what is going to be a
 * "GameObject" on the backend
 */
public class GameObject extends AbstractComponent implements GameObjectComponent{

  private String name;
  private List<Node> children;

  private ImageView image;

  public GameObject(String ID) {
    super(ID);
    children = null;
    instantiatePropFile("frontend.properties.Defaults.GameObject");
  }
  public GameObject(String ID, Map<String, String> map){
    super(ID);
    children = null;
    instantiatePropFile("frontend.properties.Defaults.GameObject");
    //setImage(getDEFAULT_BUNDLE().getString(replaceWithFileLoadingByID()));
    setValuesfromMap(map);
    followMouse();
  }

  @Override
  public void setImage(String imagePath) {
    Image newImage;
    try {
      newImage = new Image(new FileInputStream(imagePath));
    } catch (Exception e) {
      System.out.println("Image " + imagePath + " not found");
      return;
    }
    image = new ImageView(newImage);
    image.setFitWidth(size);
    image.setFitHeight(size);
  }

  @Override
  public ImageView getImage() {
    return image;
  }

  @Override
  public void setName(String newName) {
    name = newName;
  }
  public String getName(){
    return name;
  }

  @Override
  public List<Node> getChildren() {
    return children;
  }

  @Override
  public Node getNode() {
    return getImage();
  }

  @Override
  public void setDefault() {

  }
}
