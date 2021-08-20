package expression;

public class Const implements ArithmeticExpression {
    private final Number x;

    public Const(Number x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return x.toString();
    }

    @Override
    public String toMiniString() {
        return this.toString();
    }

    @Override
    public int evaluate(int x) {
        return this.x.intValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.x.intValue();
    }

//    @Override
//    public double evaluate(double x) {
//        return this.x.doubleValue();
//    }

    @Override
    public boolean equals(Object object) {
        if (object != null && this.getClass() == object.getClass()) {
            Const that = (Const) object;
            return this.x.equals(that.x);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(x.intValue());
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
}
