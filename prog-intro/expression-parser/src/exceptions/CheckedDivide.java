package expression.exceptions;

import expression.AbstractBinaryArithmeticOperation;
import expression.ArithmeticExpression;

public class CheckedDivide extends AbstractBinaryArithmeticOperation {
    public CheckedDivide(ArithmeticExpression firstExpression, ArithmeticExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected int computation(int a, int b) {
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException(a + " / " + b + " need more than 32 bits");
        }else if (b == 0) {
            throw new DivisionByZeroException(a + " can't be divided by zero");
        }
        return a / b;
    }

    @Override
    protected String getOperationSymbol() {
        return "/";
    }
}
