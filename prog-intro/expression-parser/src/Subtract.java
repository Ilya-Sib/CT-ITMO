package expression;

public class Subtract extends AbstractBinaryArithmeticOperation {
    public Subtract(ArithmeticExpression firstExpression, ArithmeticExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected int computation(int a, int b) {
        return a - b;
    }

//    @Override
//    protected double computation(double a, double b) {
//        return a - b;
//    }

    @Override
    protected String getOperationSymbol() {
        return "-";
    }

//    @Override
//    public Priority getPriority() {
//        return Priority.ADD_SUBTRACT;
//    }
//
//    @Override
//    public boolean needBrackets() {
//        return true;
//    }
}
