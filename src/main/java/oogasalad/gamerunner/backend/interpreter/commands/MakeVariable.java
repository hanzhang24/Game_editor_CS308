package oogasalad.gamerunner.backend.interpreter.commands;


import oogasalad.gamerunner.backend.interpreter.Environment;
import oogasalad.gamerunner.backend.interpreter.tokens.*;

/**
 * Creates a new variable with the given name and value
 */
public class MakeVariable extends OperatorToken {

    public MakeVariable(){
        super(2, "MakeVariable");
    }

    @Override
    public ValueToken<?> evaluate(Environment env) {
        VariableToken v = checkArgument(env, getArg(0), VariableToken.class);

        String name = v.NAME;

        Token t = getArg(1);
        if (!(t instanceof ExpressionToken)) {
            t = t.evaluate(env);
        }

        env.addVariable(name, t);
        return null;
    }
}
