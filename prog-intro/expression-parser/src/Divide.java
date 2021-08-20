package expression;

public class Divide extends AbstractBinaryArithmeticOperation {
    public Divide(ArithmeticExpression firstExpression, ArithmeticExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected int computation(int a, int b) {
        return a / b;
    }

//    @Override
//    protected double computation(double a, double b) {
//        return a / b;
//    }

    @Override
    protected String getOperationSymbol() {
        return "/";
    }

//    @Override
//    public Priority getPriority() {
//        return Priority.MULTIPLY_DIVIDE;
//    }
//
//    @Override
//    public boolean needBrackets() {
//        return true;
//    }
}
