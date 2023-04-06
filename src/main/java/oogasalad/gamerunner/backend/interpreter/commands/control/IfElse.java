package oogasalad.gamerunner.backend.interpreter.commands.control;

import oogasalad.gamerunner.backend.interpreter.Environment;
import oogasalad.gamerunner.backend.interpreter.tokens.ExpressionToken;
import oogasalad.gamerunner.backend.interpreter.tokens.OperatorToken;
import oogasalad.gamerunner.backend.interpreter.tokens.Token;
import oogasalad.gamerunner.backend.interpreter.tokens.ValueToken;

/**
 * Repeats the given expressions the given number of times
 */
public class IfElse extends OperatorToken {
    public IfElse() {
        super(3, "IfElse");
    }

    public Token evaluate(Environment env) throws IllegalArgumentException{

        Token t = getArg(0).evaluate(env);

        ExpressionToken exprs1 = checkArgument(getArg(1), ExpressionToken.class, env);
        ExpressionToken exprs2 = checkArgument(getArg(2), ExpressionToken.class, env);
        ValueToken<Boolean> b = checkArgumentWithSubtype(t, ValueToken.class, Boolean.class.getName(), env);

        if (b.VALUE) {
            exprs1.evaluate(env);
        } else {
            exprs2.evaluate(env);
        }

        return null;
    }
}
