package expression.exceptions;

import expression.AbstractBinaryArithmeticOperation;
import expression.ArithmeticExpression;

public class CheckedSubtract extends AbstractBinaryArithmeticOperation {
    public CheckedSubtract(ArithmeticExpression firstExpression, ArithmeticExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected int computation(int a, int b) {
        if (b > 0 && a < Integer.MIN_VALUE + b
                || b < 0 && a > Integer.MAX_VALUE + b) {
            throw new OverflowException(a + " - " + b + " need more than 32 bits");
        }
        return a - b;
    }

    @Override
    protected String getOperationSymbol() {
        return "-";
    }
}
