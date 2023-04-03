package oogasalad.gamerunner.backend.interpreter.tokens;

import oogasalad.gamerunner.backend.interpreter.Environment;
import oogasalad.gamerunner.backend.interpreter.commands.UserInstruction;

public class TempFunctionToken extends OperatorToken {
  public final String NAME;

  public TempFunctionToken(int numArgs, String name) {
    super(numArgs, name);
    NAME = name;
  }

  @Override
  public Token evaluate(Environment env) throws IllegalArgumentException {
    VariableToken var = new VariableToken(NAME);
    UserInstruction instruction = (UserInstruction) var.evaluate(env);
    Token[] args = new Token[getNumArgs()];
    for (int i = 0; i < getNumArgs(); i++) {
      args[i] = getArg(i);
    }
    instruction.passArguments(args);
    return instruction.evaluate(env);
  }
}