package oogasalad.frontend.nodeEditor.customNodeEditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import oogasalad.frontend.managers.PropertyManager;
import oogasalad.frontend.managers.StandardPropertyManager;
import oogasalad.frontend.nodeEditor.customNodeEditor.Nodes.DraggableNodes.DraggableAbstractNode;
import oogasalad.frontend.nodeEditor.customNodeEditor.Nodes.DraggableNodes.StateNode;

public abstract class AbstractNodePanel {

  public static final String NODES_FOLDER = "oogasalad.frontend.nodeEditor.customNodeEditor.Nodes.";
  protected Group group;
  protected ImageView workspace;
  protected double windowWidth, windowHeight;
  protected PropertyManager propertyManager = StandardPropertyManager.getInstance();

  protected NodeController nodeController;

  public AbstractNodePanel(NodeController nodeController) {
    this.nodeController = nodeController;
    windowWidth = propertyManager.getNumeric("WindowWidth");
    windowHeight = propertyManager.getNumeric("WindowHeight");
  }

  protected abstract List<Button> getNodeSelectionButtons();

  public void saveAllNodeContent(String filePath) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonObject statesObject = new JsonObject();
    for (Node node : group.getChildren()) {
      if (node instanceof StateNode) {
        StateNode stateNode = (StateNode) node;
        JsonObject stateObject = gson.fromJson(stateNode.sendJSONContent(), JsonObject.class);
        String stateName = stateObject.keySet().iterator().next();
        statesObject.add(stateName, stateObject.get(stateName));
      }
    }
    JsonObject contentObject = new JsonObject();
    contentObject.add("states", statesObject);
    try (FileWriter fileWriter = new FileWriter(filePath)) {
      gson.toJson(contentObject, fileWriter);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  protected Button makeButton(String buttonName, EventHandler<ActionEvent> handler) {
    Button button = new Button(buttonName);
    button.setOnAction(handler);
    button.setMaxWidth(Double.MAX_VALUE);
    GridPane.setHgrow(button, Priority.ALWAYS);
    return button;
  }

  protected void makeNode(String className) {
    try {
      Class<?> clazz = Class.forName(className);
      Constructor<?> constructor = clazz.getConstructor(NodeController.class);
      DraggableAbstractNode node = (DraggableAbstractNode) constructor.newInstance(nodeController);
      group.getChildren().add(node);
      node.setBoundingBox(workspace.getBoundsInParent());
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
             IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public GridPane makeNodeSelectionPane() {
    List<Button> buttons = getNodeSelectionButtons();
    GridPane pane = new GridPane();
    pane.setStyle("-fx-background-color: gray");
    for (Button button : buttons) {
      pane.add(button, 0, buttons.indexOf(button));
    }
    pane.setMinSize(windowWidth / 4, windowHeight / 4);
    return pane;
  }


  public ScrollPane makeWorkspacePane() {
    workspace = new ImageView(
        new Image(getClass().getResourceAsStream("/frontend/images/GameEditor/grid.png")));
    group = new Group(workspace);
    double defaultXScale = 0.15;
    double defaultYScale = 0.15;
    group.setScaleX(defaultXScale);
    group.setScaleY(defaultYScale);
    StackPane content = new StackPane(new Group(group));
    content.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    content.setOnScroll(e -> {
      if (e.isShortcutDown() && e.getDeltaY() != 0) {
        if (e.getDeltaY() < 0) {
          group.setScaleX(Math.max(group.getScaleX() - 0.1, 0.15));
        } else {
          group.setScaleX(Math.min(group.getScaleX() + 0.1, 3.0));
        }
        group.setScaleY(group.getScaleX());
        e.consume();
      }
    });
    ScrollPane scrollPane = new ScrollPane(content);
    scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setPannable(true);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    workspace.setFitWidth(5 * windowWidth);
    workspace.setFitHeight(5 * windowHeight);
    return scrollPane;
  }

}