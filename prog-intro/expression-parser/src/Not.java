package expression;

public class Not extends AbstractUnaryArithmeticOperation {
    public Not(ArithmeticExpression expression) {
        super(expression);
    }

    @Override
    protected int computation(int a) {
        return ~a;
    }

    @Override
    protected String getOperationSymbol() {
        return "~";
    }

//    @Override
//    protected double computation(double a) {
//        throw new UnsupportedOperationException();
//    }
}
