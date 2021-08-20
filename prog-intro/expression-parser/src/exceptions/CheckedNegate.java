package expression.exceptions;

import expression.AbstractUnaryArithmeticOperation;
import expression.ArithmeticExpression;

public class CheckedNegate extends AbstractUnaryArithmeticOperation {
    public CheckedNegate(ArithmeticExpression expression) {
        super(expression);
    }

    @Override
    protected int computation(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("-" + a + " need more than 32 bits");
        }
        return -a;
    }

    @Override
    protected String getOperationSymbol() {
        return "-";
    }
}
