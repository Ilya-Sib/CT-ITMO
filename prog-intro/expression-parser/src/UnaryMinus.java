package expression;

public class UnaryMinus extends AbstractUnaryArithmeticOperation {
    public UnaryMinus(ArithmeticExpression expression) {
        super(expression);
    }

    @Override
    protected int computation(int a) {
        return -a;
    }

    @Override
    protected String getOperationSymbol() {
        return "-";
    }

//    @Override
//    protected double computation(double a) {
//        return -a;
//    }
}
