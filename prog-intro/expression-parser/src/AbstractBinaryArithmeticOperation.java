package expression;

public abstract class AbstractBinaryArithmeticOperation implements ArithmeticExpression {
    private final ArithmeticExpression firstExpression, secondExpression;

    public AbstractBinaryArithmeticOperation(ArithmeticExpression firstExpression, ArithmeticExpression secondExpression) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }

    @Override
    public String toString() {
        return '(' + firstExpression.toString() + ' ' + this.getOperationSymbol() + ' '
                + secondExpression.toString() + ')';
    }

//    @Override
//    public String toMiniString() {
//        StringBuilder sb = new StringBuilder();
//        addBracketsIfNeed((isNeedFirstBracket()), firstExpression, sb);
//        sb.append(' ').append(this.getOperationSymbol()).append(' ');
//        addBracketsIfNeed((isNeedSecondBracket()), secondExpression, sb);
//        return sb.toString();
//    }
//
//    private void addBracketsIfNeed(boolean brackets, ArithmeticExpression expression, StringBuilder sb) {
//        if (brackets) sb.append('(').append(expression.toMiniString()).append(')');
//        else sb.append(expression.toMiniString());
//    }
//
//    private boolean isNeedFirstBracket() {
//        return comparisonForLess(firstExpression.getPriority(), getPriority());
//    }
//
//    private boolean isNeedSecondBracket() {
//        return comparisonForLess(secondExpression.getPriority(), getPriority())
//                || getPriority().equals(secondExpression.getPriority())
//                && (needBrackets() || !getOperationSymbol().equals("+") && secondExpression.needBrackets());
//    }
//
//    private boolean comparisonForLess(Priority firstPriority, Priority secondPriority) {
//        return firstPriority.compareTo(secondPriority) < 0;
//    }

    @Override
    public int evaluate(int x) {
        return computation(firstExpression.evaluate(x), secondExpression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return computation(firstExpression.evaluate(x, y, z), secondExpression.evaluate(x, y, z));
    }

//    @Override
//    public double evaluate(double x) {
//        return computation(firstExpression.evaluate(x), secondExpression.evaluate(x));
//    }

    @Override
    public boolean equals(Object object) {
        if (object != null && this.getClass() == object.getClass()) {
            AbstractBinaryArithmeticOperation that = (AbstractBinaryArithmeticOperation) object;
            return this.firstExpression.equals(that.firstExpression)
                    && this.secondExpression.equals(that.secondExpression);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * (31 * (31 * firstExpression.hashCode() + 31) + secondExpression.hashCode()) + this.getClass().hashCode();
    }

    protected abstract int computation(int a, int b);
    protected abstract String getOperationSymbol();
//    protected abstract double computation(double a, double b);
}
