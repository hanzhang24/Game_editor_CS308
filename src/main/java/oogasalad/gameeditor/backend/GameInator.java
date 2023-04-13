package oogasalad.gameeditor.backend;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import oogasalad.gameeditor.backend.id.IdManager;
import oogasalad.gameeditor.backend.rules.Rule;
import oogasalad.gamerunner.backend.interpretables.Goal;
import oogasalad.gamerunner.backend.fsm.FSM;
import oogasalad.sharedDependencies.backend.ObjectFactory;
import oogasalad.sharedDependencies.backend.ownables.Ownable;
import oogasalad.gamerunner.backend.interpreter.Interpreter;
import oogasalad.sharedDependencies.backend.ownables.gameobjects.GameObject;
import oogasalad.sharedDependencies.backend.ownables.variables.Variable;
import oogasalad.sharedDependencies.backend.owners.GameWorld;
import oogasalad.sharedDependencies.backend.owners.Owner;
import oogasalad.sharedDependencies.backend.owners.Player;

/**
 * The Game class represents the game itself.
 * It contains Owners such as the GameWorld and Players.
 * It contains the Rules and Goals.
 * @author Michael Bryant
 * @author Max Meister
 */
public class GameInator {

  /**
   * The Rules of the game.
   */
  private final IdManager<Rule> rules = new IdManager<>();

  /**
   * The Goals of the game.
   */
  private final IdManager<Goal> goals = new IdManager<>();

  /**
   * The Players of the game.
   * Players own Ownables.
   * Ids of Players are their index in the list. Ex: Player0, Player1, Player2, etc.
   * These Ids are constant
   */
  private final ArrayList<Player> players = new ArrayList<>(); //TODO set to make number of players, make arraylist

  /**
   * The IdManager of the game for Ownables.
   */
  private final IdManager<Ownable> ownableIdManager = new IdManager<>();

  /**
   * The GameWorld of the game.
   * The GameWorld owns Ownables not owned by Players.
   */
  private final GameWorld gameWorld = new GameWorld();

  private final FSM<String> fsm = new FSM<>(ownableIdManager);

  private final Interpreter interpreter = new Interpreter();

  private int numPlayers = 1;


  /////////////////// PLAY THE GAME ///////////////////

  public void initGame() {
    interpreter.link(ownableIdManager);

    Variable<Double> turn = new Variable<>(0.);
    turn.setOwner(gameWorld);
    ownableIdManager.addObject(turn, "turn");

    Variable<Double> numPlayersVar = new Variable<>((double) numPlayers);
    numPlayersVar.setOwner(gameWorld);
    ownableIdManager.addObject(numPlayersVar, "playerCount");

    Variable<List<GameObject>> available = new Variable<>(new ArrayList<>());
    available.setOwner(gameWorld);
    ownableIdManager.addObject(available, "available");

    fsm.setState("INIT");
    fsm.transition();
  }
  /**
   * reacts to clicking a piece
   */
  public void clickPiece(String selectedObject) {
    fsm.setStateInnerValue(selectedObject);
    fsm.transition();

    if (fsm.getCurrentState().equals("DONE")){
      fsm.setState("INIT");
      // check goals
      if (checkGoals() != -1){
        // TODO end game
      }
    }
  }

  public void keyDown(String key) {

  }

  public void keyUp(String key) {

  }

  private int checkGoals() {
    for (Map.Entry<String, Goal> goal : goals){
      Goal g = goal.getValue();
      int player = g.test(interpreter, ownableIdManager);
      if (player != -1){
        return player;
      }
    }
    return -1;
  }

  // region LOADING

  /**
   * Loads a Game from a file.
   * @param directory the name of the file to load from
   */
  public void loadGame(String directory) {
    // TODO
    // get num players

    // load in players and player FSM

    // load in ownables


    // load in rules and goals
  }

  private void initPlayers(JsonObject json) {
    numPlayers = 1;
  }

  private void initOwnables(JsonObject json) {}

  private void initRulesAndGoals(JsonObject json) {}

  //endregion

  // region PLAYERS

  /**
   * The ObjectFactory of the game.
   */
  private final ObjectFactory objectFactory = new ObjectFactory(gameWorld, ownableIdManager, players);

  /**
   * Adds a Player to the game.
   * @param player the Player to add
   */
  public void addPlayer(Player player) {
    players.add(player);
  }

  /**
   * Removes a Player from the game, if there is one.
   * Destroys all Ownables owned by the Player.
   */
  public void removePlayer() {
    if (players.size() > 0) {
      //remove the last player
      players.remove(players.size() - 1);
    }
  }

  /**
   * Removes all Players from the game and their Ownables.
   */
  public void removeAllPlayers() {
    players.clear();
    ownableIdManager.clear();
    // TODO reconsider
  }

  /**
   * Gets the Players of the game.
   * @return unmodifiable List of Players
   */
  public List<Player> getPlayers() {
    return Collections.unmodifiableList(players);
  }


  /**
   * Adds a Rule to the game.
   * @param rule the Rule to add
   */
  public void addRule(Rule rule) {
    rules.addObject(rule);
  }

  /**
   * Removes a Rule from the game, if it exists there.
   * @param rule the Rule to remove
   */
  public void removeRule(Rule rule) {
    if(!rules.isIdInUse(rules.getId(rule))) {
      return;
    }
    rules.removeObject(rule);
  }

  /**
   * Gets the Rules of the game.
   * @return unmodifiable List of Rules
   */
  public List<Rule> getRules() {
    ArrayList<Rule> listRules= new ArrayList<>();
    for(Map.Entry<String, Rule> entry : rules) {
      listRules.add(entry.getValue());
    }
    return Collections.unmodifiableList(listRules);
  }

  /**
   * Adds a Goal to the game.
   * @param goal the Goal to add
   */
  public void addGoal(Goal goal) {
    goals.addObject(goal);
  }

  /**
   * Removes a Goal from the game, if it exists there.
   * @param goal the Goal to remove
   */
  public void removeGoal(Goal goal) {
    if(!goals.isIdInUse(goals.getId(goal))) {
      return;
    }
    goals.removeObject(goal);
  }

  /**
   * Gets the Goals of the game.
   * @return unmodifiable List of Goals
   */
  public List<Goal> getGoals() {
    ArrayList<Goal> listGoals= new ArrayList<>();
    for(Map.Entry<String, Goal> entry : goals) {
      listGoals.add(entry.getValue());
    }
    return Collections.unmodifiableList(listGoals);
  }

  /**
   * Gets the GameWorld of the game.
   * @return the GameWorld
   */
  public GameWorld getGameWorld() {
    return gameWorld;
  }


  /**
   * Adds an Ownable to the IdManager and Owner.
   * @param owner the Owner of the Ownable
   * @param ownable the Ownable being added to owner
   */
  public void changeOwner(Owner owner, Ownable ownable) {
    ownable.setOwner(owner);
  }

  //region sendObject API


//  /**
//   * Creates an ownable using ownableFactory for player
//   * Pass in null for any unused parameters (cannot pass null for type)
//   * @param type the string type of ownable
//   * @param owner the owner of the ownable
//   * @param parentOwnable the parent of the ownable
//   */
//  @Deprecated
//  private void createOwnable(String type, Owner owner, Ownable parentOwnable) {
//    Owner destinationOwner = owner;
//    if (owner == null){
//      destinationOwner = gameWorld;
//    }
//    Ownable newOwnable = OwnableFactory.createOwnable(type, destinationOwner);
//    ownableIdManager.addObject(newOwnable, parentOwnable);
//  }

  /**
   * Creates an ownable using objectFactory and adds it to the game through the IdManager
   * (IdManager will configure the owner, id, etc. all within the factory)
   * @param params the parameters of the ownable
   */
  private void createOwnable(Map<ObjectParameter, String> params) {
    objectFactory.createOwnable(params);
  }

  /**
   * Creates a new player and adds it to the game
   */
  private void createPlayer() {
    addPlayer(new Player());
  }

  /**
   * Creates an ownable using ownableFactory for player
   * Pass in null for any unused parameters (cannot pass null for type)
   * @param params the parameters of the ownable
   */
  private void createRule(Map<ObjectParameter, String> params) {
    Rule newRule = ObjectFactory.createRule(params);
    rules.addObject(newRule);
  }

  /**
   * Creates a new goal and adds it to the game
   */
  private void createGoal(Map<ObjectParameter, String> params) {
    Goal newGoal = ObjectFactory.createGoal(params);
    goals.addObject(newGoal);
  }

  /**
   Method is called in order to send information about a newly constructed   object that was made in the front end sent to the backend. The
   controller sends to the backend for the backend to input these into a
   file
   Note: currently it only constructs Ownables with default values. Ex. a variable has no initialized value
   In the future this would be added
   Currently, this responsibility is handled by the updateObjectProperties API call
   @Type The class the object belongs to
   @Params The params of the object
   **/
  public void sendObject(ObjectType type, Map<ObjectParameter, String> params) throws IllegalArgumentException{
    switch (type) {
      case PLAYER -> createPlayer();
      case OWNABLE -> createOwnable(params);
      case RULE -> createRule(params);
      case GOAL -> createGoal(params);
      default -> throw new IllegalArgumentException("Invalid type"); //TODO add to properties
    }






//    //TODO this sucks and is sorta hardcoded
//    List<String> parameters = Arrays.asList(params.split(";"));
//    //{"owner=id1", "parentOwnable=id2"}
//    String ownerID = parameters.get(0).substring(parameters.get(0).indexOf("=") + 1);
//    String parentOwnableID = parameters.get(1).substring(parameters.get(1).indexOf("=") + 1);
//    if (ownerID.equals("NULL")){
//      if (parentOwnableID.equals("NULL")){
//        createOwnable(type, null, null);
//      }
//      createOwnable(type, null, getOwnable(parentOwnableID));
//    }
//    else if (parentOwnableID.equals("NULL")) {
//      createOwnable(type, playerIdManager.getObject(ownerID), null);
//    }
//    else {
//      createOwnable(type, playerIdManager.getObject(ownerID), getOwnable(parentOwnableID));
//    }
  }

  //endregion sendObject API


  /**
   * Gets the Owner of an Ownable with id.
   * @param id the id of the Ownable
   * @return the Owner of the Ownable, null if the id is not in use
   */
  public Owner getOwner(String id) {
    if (!ownableIdManager.isIdInUse(id)) {
      return null;
    }
    return getOwnable(id).getOwner();
  }

  /**
   * Sets the Owner of an Ownable with id.
   * @param id the id of the Ownable
   * @param owner the new owner of the Ownable
   * @throws IllegalArgumentException if owner is null, the Ownable is owned by the GameWorld
   */
  public void setOwner(String id, Owner owner) throws IllegalArgumentException{
    getOwnable(id).setOwner(owner);
  }


  /**
   * Gets an Ownable from the IdManager for a given id.
   * @param id the id of the Ownable
   * @return the Ownable
   * @throws IllegalArgumentException if the id is not in use
   */
  public Ownable getOwnable(String id) throws IllegalArgumentException {
    if (!ownableIdManager.isIdInUse(id)) {
      return null;
    }
    return ownableIdManager.getObject(id);
  }

  //TODO TURN
}