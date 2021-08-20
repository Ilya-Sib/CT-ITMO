package expression.parser;

import expression.TripleExpression;
import expression.exceptions.ParseExpressionException;

public interface Parser  {
    TripleExpression parse(String expression) throws ParseExpressionException;
}