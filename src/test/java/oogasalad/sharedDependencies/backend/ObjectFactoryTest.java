package oogasalad.sharedDependencies.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import oogasalad.gameeditor.backend.GameInator;
import oogasalad.gameeditor.backend.ObjectParameter;
import oogasalad.gameeditor.backend.ObjectType;
import oogasalad.gameeditor.backend.id.IdManager;
import oogasalad.sharedDependencies.backend.ownables.Ownable;
import oogasalad.sharedDependencies.backend.ownables.gameobjects.GameObject;
import oogasalad.sharedDependencies.backend.ownables.variables.Variable;
import oogasalad.sharedDependencies.backend.owners.GameWorld;
import oogasalad.sharedDependencies.backend.owners.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectFactoryTest {

  private IdManager<Ownable> idManager;
  private ArrayList<Player> players;
  private GameWorld world;
  private GameInator game;

  @BeforeEach
  void setup() {
    game = new GameInator();
    world = game.getGameWorld();
    players = (ArrayList<Player>) game.getPlayers();
    game.addPlayer(new Player());
    game.addPlayer(new Player());
    game.addPlayer(new Player());
    idManager = game.getOwnableIdManager();
  }

  @Test
  public void testCreateOwnables() {
    // Variable
    ObjectType type = ObjectType.OWNABLE;
    Map<ObjectParameter, Object> params = new HashMap<>();
    params.put(ObjectParameter.OWNABLE_TYPE, "Variable");
    Map<Object, Object> constructorParams = new HashMap<>();
    params.put(ObjectParameter.CONSTRUCTOR_ARGS, constructorParams);
    // Variable with nothing
    game.sendObject(type, params);
    // Variable with Constructor Value
    constructorParams.put("value", 64);
    params.put(ObjectParameter.CONSTRUCTOR_ARGS, constructorParams);
    game.sendObject(type, params);
    // Variable with ID
    params.put(ObjectParameter.ID, "myId");
    game.sendObject(type, params);
    // Variable with owner
    params.put(ObjectParameter.OWNER, "2");
    game.sendObject(type, params);
    // Variable with parent ownable
    params.put(ObjectParameter.PARENT_OWNABLE_ID, "myId");
    game.sendObject(type, params);
    assertEquals(5, idManager.getSimpleIds().size());

    // GameObject
    ObjectType type2 = ObjectType.OWNABLE;
    Map<ObjectParameter, Object> params2 = new HashMap<>();
    params2.put(ObjectParameter.OWNABLE_TYPE, "GameObject");
    Map<Object, Object> constructorParams2 = new HashMap<>();
    params2.put(ObjectParameter.CONSTRUCTOR_ARGS, constructorParams2);
    // GameObject with nothing
    game.sendObject(type2, params2);
    // GameObject with ID
    params2.put(ObjectParameter.ID, "myIdGameObject");
    game.sendObject(type2, params2);
    // GameObject with owner
    params2.put(ObjectParameter.OWNER, "3");
    game.sendObject(type2, params2);
    // GameObject with parent ownable
    params2.put(ObjectParameter.PARENT_OWNABLE_ID, "myIdGameObject");
    game.sendObject(type2, params2);
    assertEquals(9, idManager.getSimpleIds().size());
  }

  @Test
  public void testBoardCreator() {
    //TODO

  }

}
