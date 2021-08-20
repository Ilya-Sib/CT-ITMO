package expression;

public class Count extends AbstractUnaryArithmeticOperation {
    public Count(ArithmeticExpression expression) {
        super(expression);
    }

    @Override
    protected int computation(int a) {
        return Integer.bitCount(a);
    }

    @Override
    protected String getOperationSymbol() {
        return "count ";
    }

//    @Override
//    protected double computation(double a) {
//        throw new UnsupportedOperationException();
//    }
}
