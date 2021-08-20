package expression.exceptions;

import expression.AbstractBinaryArithmeticOperation;
import expression.ArithmeticExpression;

public class CheckedMin extends AbstractBinaryArithmeticOperation {
    public CheckedMin(ArithmeticExpression firstExpression, ArithmeticExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected int computation(int a, int b) {
        return a < b ? a : b;
    }

    @Override
    protected String getOperationSymbol() {
        return "min";
    }
}
