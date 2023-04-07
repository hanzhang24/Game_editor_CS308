package oogasalad.gamerunner.backend.interpreter;

import oogasalad.gameeditor.backend.id.IdManager;
import oogasalad.gamerunner.backend.interpreter.tokens.ExpressionToken;
import oogasalad.gamerunner.backend.interpreter.tokens.Token;
import oogasalad.gamerunner.backend.interpreter.tokens.ValueToken;
import oogasalad.sharedDependencies.backend.ownables.Ownable;
import oogasalad.sharedDependencies.backend.ownables.variables.Variable;

import java.util.*;

public class Environment {
    private final List<Map<String, Token>> scope = new ArrayList<>();
    private IdManager<Ownable> game;

    private static final String LANGUAGE_RESOURCE_PATH = "backend.interpreter.languages";
    private String language = "English";

    ResourceBundle resources;

    public Environment(){
        scope.add(new HashMap<>());
        resources = ResourceBundle.getBundle(LANGUAGE_RESOURCE_PATH + "." + language);
    }

    public void setLanguage(String language){
        this.language = language;
        resources = ResourceBundle.getBundle(LANGUAGE_RESOURCE_PATH + "." + language);
    }

    public String getLanguageResource(String key){
        return resources.getString(key);
    }

    public void linkSimulation(IdManager<Ownable> game){
        this.game = game;
    }

    /////////////////// SCOPE ///////////////////

    /**
     * retrieve a variable from the scope (checks from most current scope to global scope)
     * @param name the name of the variable
     * @return the token corresponding to the variable
     */
    public Token getLocalVariable(String name){

        if (name.startsWith(":game_")){
            return getGameVariableToken(name);
        }

        if (!name.startsWith("interpreter-")) name = "interpreter-" + name;

        for (int i = scope.size() - 1; i >= 0; i--){
            if (scope.get(i).containsKey(name)){
                return scope.get(i).get(name);
            }
        }

        return null;
    }

    private Token getGameVariableToken(String name) {
        name = name.substring(6);
        if (game.isIdInUse(name)){
            Ownable v = game.getObject(name);
            if (v instanceof Variable<?> var){
                Token t = convertVariableToToken(var);
                t.linkVariable(var);
                return t;
            }
            return new ValueToken<>(v);
        }
        else return null;
    }

    /**
     * Creates a variable to the current scope
     * @param name the name of the variable
     * @param val the token corresponding to the variable
     */
    public void addVariable(String name, Token val){

        if (name.startsWith(":game_")) {
            addGameVariable(name, val);
            return;
        }

        name = "interpreter-" + name;

        Variable<?> var = convertTokenToVariable(val);

        if (var.get() != null) {
            if (!game.isIdInUse(name) && !game.isObjectInUse(var)){
                game.addObject(var, name);
            }
            else if (game.isIdInUse(name)){
                ((Variable)game.getObject(name)).set(var.get());
            }
        }

        Map<String, Token> curScope = scope.get(scope.size() - 1);
        if (curScope.containsKey(name + "-global")){
            // loop through all scopes and replace the global variable, if it doesn't exist put in global
            for (int i = scope.size() - 1; i >= 0; i--){
                if (scope.get(i).containsKey(name) || i == 0){
                    scope.get(i).put(name, val);
                    return;
                }
            }
        }
        scope.get(scope.size() - 1).put(name, val);
    }

    private void addGameVariable(String name, Token val) {
        name = name.substring(6);
        if (game.isIdInUse(name)) {
            Variable setter = convertTokenToVariable(val);
            ((Variable)game.getObject(name)).set(setter.get());
        }
        else{
            Variable<?> var = convertTokenToVariable(val);
            game.addObject(var, name);
        }
    }

    Token convertVariableToToken(Variable<?> var){
        Object obj = var.get();
        if (obj instanceof Integer i){ return new ValueToken<>(i.doubleValue()); }
        else if (obj instanceof List<?>){
            ExpressionToken list = new ExpressionToken();
            for (Object o : (List<?>) obj){
                if (o instanceof Variable<?> v) list.addToken(convertVariableToToken(v));
                if (o instanceof Integer) list.addToken(new ValueToken<>((double) ((int) o)));
                else list.addToken(new ValueToken<>(o));
            }
            return list;
        }
        else return new ValueToken<>(obj);
    }

    Variable<?> convertTokenToVariable(Token t){
        Variable<?> v = new Variable<>(game, t.export(this));
        t.linkVariable(v);
        return v;
    }

    /**
     * Adds a new scope to the scope list (to be used when a function is called)
     */
    public void createLocalScope(){
        scope.add(new HashMap<>());
    }

    /**
     * Removes scope from the scope list (to be used after function finishes)
     */
    public void endLocalScope(){

        Map<String, Token> curScope = scope.get(scope.size() - 1);

        List<String> removed = new ArrayList<>();
        for (String key : curScope.keySet()){
            if (!key.contains("-global") && game.isIdInUse(key)){
                game.removeObject(key);
                removed.add(key);
            }
        }

        scope.remove(curScope);

        for (String key : removed){
            Token var = getLocalVariable(key);
            if (var != null){
                Variable<?> v = convertTokenToVariable(var);
                game.addObject(v, key);
            }
        }
    }

    /////////////////// SEND EVENTS ///////////////////

}
