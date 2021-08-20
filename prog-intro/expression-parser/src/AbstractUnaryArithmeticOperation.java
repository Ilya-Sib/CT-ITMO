package expression;

public abstract class AbstractUnaryArithmeticOperation implements ArithmeticExpression {
    private final ArithmeticExpression expression;

    public AbstractUnaryArithmeticOperation(ArithmeticExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return getOperationSymbol() + "(" + expression.toString() + ")";
    }

    @Override
    public String toMiniString() {
        return toString();
    }


    @Override
    public int evaluate(int x) {
        return computation(expression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return computation(expression.evaluate(x, y, z));
    }

//    @Override
//    public double evaluate(double x) {
//        return computation(expression.evaluate(x));
//    }

    @Override
    public boolean equals(Object object) {
        if (object != null && this.getClass() == object.getClass()) {
            AbstractUnaryArithmeticOperation that = (AbstractUnaryArithmeticOperation) object;
            return this.expression.equals(that.expression);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * (31 * expression.hashCode() + 31) + this.getClass().hashCode();
    }

//    @Override
//    public Priority getPriority() {
//        return Priority.VARIABLE_CONST_UNARY;
//    }
//
//    @Override
//    public boolean needBrackets() {
//        return false;
//    }

    protected abstract int computation(int a);
    protected abstract String getOperationSymbol();
//    protected abstract double computation(double a);
}
