package expression.exceptions;

import expression.*;
import expression.parser.BaseParser;
import expression.parser.ExpressionSource;
import expression.parser.StringSource;

import java.util.HashSet;
import java.util.Set;

public class ExpressionParser implements Parser {
    @Override
    public TripleExpression parse(String expression) throws ParseExpressionException {
        return parse(new StringSource(expression));
    }

    public static TripleExpression parse(ExpressionSource source) throws ParseExpressionException {
        return new TripleExpressionParser(source).parseTriple();
    }

    private static class TripleExpressionParser extends BaseParser {
        private static final Set<String> OPERATIONS = new HashSet<>(Set.of(
                "sqrt", "abs", "min", "max", "count"
        ));

        public TripleExpressionParser(final ExpressionSource source) {
            super(source);
            nextChar();
        }

        public TripleExpression parseTriple() throws ParseExpressionException {
            ArithmeticExpression result = parseMinMax();
            if (eof()) {
                return result;
            }
            unsupportedCharChecker();
            varNameChecker();
            throw error("End of expression expected");
        }

        private ArithmeticExpression parseMinMax() throws ParseExpressionException {
            ArithmeticExpression result = parseOr();
            while (true) {
                if (test("max")) {
                    result = new CheckedMax(result, parseOr());
                } else if (test("min")) {
                    result = new CheckedMin(result, parseOr());
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression parseOr() throws ParseExpressionException {
            ArithmeticExpression result = parseXor();
            while (true) {
                if (test('|')) {
                    result = new Or(result, parseXor());
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression parseXor() throws ParseExpressionException {
            ArithmeticExpression result = parseAnd();
            while (true) {
                if (test('^')) {
                    result = new Xor(result, parseAnd());
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression parseAnd() throws ParseExpressionException {
            ArithmeticExpression result = parseAddSubtract();
            while (true) {
                if (test('&')) {
                    result = new And(result, parseAddSubtract());
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression parseAddSubtract() throws ParseExpressionException {
            ArithmeticExpression result = parseMultiplyDivide();
            while (true) {
                if (test('+')) {
                    result = new CheckedAdd(result, parseMultiplyDivide());
                } else if (test('-')) {
                    result = new CheckedSubtract(result, parseMultiplyDivide());
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression parseMultiplyDivide() throws ParseExpressionException {
            ArithmeticExpression result = parseConstVariableUnary();
            while (true) {
                if (test('*')) {
                    result = new CheckedMultiply(result, parseConstVariableUnary());
                } else if (test('/')) {
                    result = new CheckedDivide(result, parseConstVariableUnary());
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression parseConstVariableUnary() throws ParseExpressionException {
            ArithmeticExpression result;
            skipWhitespace();
            if (test('(')) {
                result = parseMinMax();
                expect(')');
            } else if (test("sqrt")) {
                result = new CheckedSqrt(parseConstVariableUnary());
            } else if (test("abs")) {
                result = new CheckedAbs(parseConstVariableUnary());
            } else if (test("count")) {
                result = new Count(parseConstVariableUnary());
            } else if (test('-')) {
                result = between('0', '9') ? parseConst(true) : new CheckedNegate(parseConstVariableUnary());
            } else if (test('~')) {
                result = new Not(parseConstVariableUnary());
            } else if (isNotOperationToken()) {
                varNameChecker();
                result = new Variable(getToken());
                nextToken();
            } else if (between('0', '9')) {
                result = parseConst(false);
            } else {
                unsupportedCharChecker();
                throw new LostArgumentException("Lost argument");
            }
            skipWhitespace();
            return result;
        }

        private ArithmeticExpression parseConst(boolean isNegate) throws ParseExpressionException {
            StringBuilder constNumber = new StringBuilder(isNegate ? "-" : "");
            while (between('0', '9')) {
                constNumber.append(getChar());
                nextChar();
            }
            try {
                return new Const(Integer.parseInt(constNumber.toString()));
            } catch (NumberFormatException e) {
                throw new ConstFormatException("Wrong const number: " + constNumber.toString());
            }
        }

        private boolean isNotOperationToken() {
            return hasToken() && !OPERATIONS.contains(getToken());
        }

        private void varNameChecker() throws ParseExpressionException {
            if (getToken().length() > 0 && !(getToken().equals("x")
                    || getToken().equals("y") || getToken().equals("z"))) {
                throw new IllegalVariableNameException("Variable can't have name: " + getToken());
            }
        }

        private void unsupportedCharChecker() throws ParseExpressionException {
            skipWhitespace();
            if (getChar() != END && !(Character.isLetterOrDigit(getChar()) || getChar() == '(' || getChar() == ')'
                    || getChar() == '+' || getChar() == '/' || getChar() == '-' || getChar() == '*')) {
                throw new UnsupportedCharException(getChar(), getSource().getPos());
            }
        }
    }
}
