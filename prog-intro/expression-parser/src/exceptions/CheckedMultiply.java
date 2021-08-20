package expression.exceptions;

import expression.AbstractBinaryArithmeticOperation;
import expression.ArithmeticExpression;

public class CheckedMultiply extends AbstractBinaryArithmeticOperation {
    public CheckedMultiply(ArithmeticExpression firstExpression, ArithmeticExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected int computation(int a, int b) {
        if (a != 0 && b != 0 && (a * b / b != a || a * b / a != b)) {
            throw new OverflowException(a + " * " + b + " need more than 32 bits");
        }
        return a * b;
    }

    @Override
    protected String getOperationSymbol() {
        return "*";
    }
}