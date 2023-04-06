package oogasalad.gamerunner.backend;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import oogasalad.gameeditor.backend.goals.EmptyGoal;
import oogasalad.gameeditor.backend.goals.Goal;
import oogasalad.gameeditor.backend.id.IdManager;
import oogasalad.gameeditor.backend.ownables.gameobjects.EmptyGameObject;
import oogasalad.gameeditor.backend.rules.EmptyRule;
import oogasalad.gameeditor.backend.rules.Rule;
import oogasalad.sharedDependencies.backend.ownables.Ownable;
import oogasalad.sharedDependencies.backend.ownables.variables.Variable;
import oogasalad.sharedDependencies.backend.owners.GameWorld;
import oogasalad.sharedDependencies.backend.owners.Owner;
import oogasalad.sharedDependencies.backend.owners.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
  Game game;
  IdManager<Ownable> ownableIdManager;
  IdManager<Rule> ruleIdManager;
  IdManager<Goal> goalIdManager;
  GameWorld gameworld;
  ArrayList<Player> players;

  @BeforeEach
  void setUp() {
    game = new Game();
    ownableIdManager = new IdManager<>();
    ruleIdManager = new IdManager<>();
    goalIdManager = new IdManager<>();
    gameworld = new GameWorld(ownableIdManager);
    players = new ArrayList<>();
  }

  @Test
  void addPlayer() {
    assertTrue(game.getPlayers().isEmpty());
    game.addPlayer(new Player(ownableIdManager));
    assertEquals(1, game.getPlayers().size());
    game.addPlayer(new Player(ownableIdManager));
    assertEquals(2, game.getPlayers().size());
  }

  @Test
  void addNPlayers() {
    assertTrue(game.getPlayers().isEmpty());
    game.addNPlayers(5);
    assertEquals(5, game.getPlayers().size());
    game.addNPlayers(7);
    assertEquals(12, game.getPlayers().size());
  }

  @Test
  void removePlayer() {
    assertTrue(game.getPlayers().isEmpty());
    game.addNPlayers(5);
    assertEquals(5, game.getPlayers().size());
    game.removePlayer(game.getPlayers().get(0));
    assertEquals(4, game.getPlayers().size());
  }

  @Test
  void removeAllPlayers() {
    assertTrue(game.getPlayers().isEmpty());
    game.addNPlayers(5);
    assertEquals(5, game.getPlayers().size());
    game.removeAllPlayers();
    assertTrue(game.getPlayers().isEmpty());
  }

  @Test
  void getPlayers() {
    assertTrue(game.getPlayers().isEmpty());
    Player player = new Player(ownableIdManager);
    game.addPlayer(player);
    assertEquals(player, game.getPlayers().get(0));
  }

  @Test
  void addRule() {
    assertTrue(game.getRules().isEmpty());
    game.addRule(new EmptyRule(ruleIdManager));
    assertEquals(1, game.getRules().size());
    game.addRule(new EmptyRule(ruleIdManager));
    assertEquals(2, game.getRules().size());
  }

  @Test
  void removeRule() {
    assertTrue(game.getRules().isEmpty());
    game.addRule(new EmptyRule(ruleIdManager));
    assertEquals(1, game.getRules().size());
    game.removeRule(game.getRules().get(0));
    assertTrue(game.getRules().isEmpty());
  }

  @Test
  void getRules() {
    assertTrue(game.getRules().isEmpty());
    EmptyRule rule = new EmptyRule(ruleIdManager);
    game.addRule(rule);
    assertEquals(rule, game.getRules().get(0));
  }

  @Test
  void addGoal() {
    assertTrue(game.getGoals().isEmpty());
    game.addGoal(new EmptyGoal(goalIdManager));
    assertEquals(1, game.getGoals().size());
    game.addGoal(new EmptyGoal(goalIdManager));
    assertEquals(2, game.getGoals().size());
  }

  @Test
  void removeGoal() {
    assertTrue(game.getGoals().isEmpty());
    game.addGoal(new EmptyGoal(goalIdManager));
    assertEquals(1, game.getGoals().size());
    game.removeGoal(game.getGoals().get(0));
    assertTrue(game.getGoals().isEmpty());
  }

  @Test
  void getGoals() {
    assertTrue(game.getGoals().isEmpty());
    EmptyGoal goal = new EmptyGoal(goalIdManager);
    game.addGoal(goal);
    assertEquals(goal, game.getGoals().get(0));
  }
//
//  @Test
//  void getGameWorld() {
//    assertEquals(gameworld, game.getGameWorld());
  // in instance of
//  }
//
//  @Test
//  void addOwnable() {
//  }
//
//  @Test
//  void getOwner() {
//    Player player1 = new Player(ownableIdManager);
//
//  }
//
//  @Test
//  void setOwner() {
//  }
//
//  @Test
//  void getOwnable() {
//  }
}