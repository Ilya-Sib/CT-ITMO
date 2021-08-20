package expression.exceptions;

import expression.AbstractUnaryArithmeticOperation;
import expression.ArithmeticExpression;

public class CheckedSqrt extends AbstractUnaryArithmeticOperation {
    public CheckedSqrt(ArithmeticExpression expression) {
        super(expression);
    }

    @Override
    protected int computation(int a) {
        if (a < 0) {
            throw new SqrtArgumentException("sqrt must have not negative argument: " + a);
        }
        return (int) Math.sqrt(a);
    }

    @Override
    protected String getOperationSymbol() {
        return "sqrt";
    }
}
