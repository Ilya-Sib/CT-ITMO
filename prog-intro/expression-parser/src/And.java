package expression;

public class And extends AbstractBinaryArithmeticOperation {
    public And(ArithmeticExpression firstExpression, ArithmeticExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected int computation(int a, int b) {
        return a & b;
    }

//    @Override
//    protected double computation(double a, double b) {
//        throw new UnsupportedOperationException();
//    }

    @Override
    protected String getOperationSymbol() {
        return "&";
    }

//    @Override
//    public Priority getPriority() {
//        return Priority.AND;
//    }
//
//    @Override
//    public boolean needBrackets() {
//        return false;
//    }
}
