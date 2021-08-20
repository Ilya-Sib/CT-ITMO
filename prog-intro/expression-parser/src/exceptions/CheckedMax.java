package expression.exceptions;

import expression.AbstractBinaryArithmeticOperation;
import expression.ArithmeticExpression;

public class CheckedMax extends AbstractBinaryArithmeticOperation {
    public CheckedMax(ArithmeticExpression firstExpression, ArithmeticExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected int computation(int a, int b) {
        return a > b ? a : b;
    }

    @Override
    protected String getOperationSymbol() {
        return "max";
    }
}
