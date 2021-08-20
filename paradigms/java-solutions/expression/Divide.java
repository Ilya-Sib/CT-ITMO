package expression;

import expression.mods.Operation;

public class Divide<T> extends AbstractBinaryArithmeticOperation<T> {
    public Divide(ArithmeticExpression<T> firstExpression,
                  ArithmeticExpression<T> secondExpression,
                  Operation<T> operation) {
        super(firstExpression, secondExpression, operation);
    }

    @Override
    protected T computation(T a, T b) {
        return operation.divide(a, b);
    }

    @Override
    protected String getOperationSymbol() {
        return "/";
    }
}
