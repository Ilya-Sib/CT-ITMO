package expression;

public class Variable implements ArithmeticExpression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toMiniString() {
        return this.toString();
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

//    @Override
//    public double evaluate(double x) {
//        return x;
//    }

    @Override
    public boolean equals(Object object) {
        if (object != null && this.getClass() == object.getClass()) {
            Variable that = (Variable) object;
            return this.name.equals(that.name);
        }
        return false;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalArgumentException();
        };
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode();
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
