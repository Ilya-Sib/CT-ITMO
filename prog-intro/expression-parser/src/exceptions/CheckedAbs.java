package expression.exceptions;

import expression.AbstractUnaryArithmeticOperation;
import expression.ArithmeticExpression;

public class CheckedAbs extends AbstractUnaryArithmeticOperation {
    public CheckedAbs(ArithmeticExpression expression) {
        super(expression);
    }

    @Override
    protected int computation(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("abs " + a + " need more than 32 bits");
        }
        return a < 0 ? -a : a;
    }

    @Override
    protected String getOperationSymbol() {
        return "abs";
    }
}
