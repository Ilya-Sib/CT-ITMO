package expression.parser;

import expression.*;
import expression.exceptions.ParseExpressionException;

public class ExpressionParser implements Parser {
    @Override
    public TripleExpression parse(String expression) throws ParseExpressionException {
        return parse(new StringSource(expression));
    }

    public static TripleExpression parse(ExpressionSource source) throws ParseExpressionException {
        return new TripleExpressionParser(source).parseTriple();
    }

    private static class TripleExpressionParser extends BaseParser {
        public TripleExpressionParser(final ExpressionSource source) {
            super(source);
            nextChar();
        }

        public TripleExpression parseTriple() throws ParseExpressionException {
            skipWhitespace();
            ArithmeticExpression result = parseOr();
            skipWhitespace();
            if (eof()) {
                return result;
            }
            if (test(')')) {
                throw error("Unexpected char " + getChar());
            }
            throw error("End of expression expected");
        }

        private ArithmeticExpression parseOr() throws ParseExpressionException {
            ArithmeticExpression result = parseXor();
            skipWhitespace();
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
            skipWhitespace();
            while (true) {
                if (test('^')) {
                    result = new Xor(result, parseAnd());
                }  else {
                    return result;
                }
            }
        }

        private ArithmeticExpression parseAnd() throws ParseExpressionException {
            ArithmeticExpression result = parseAddSubtract();
            skipWhitespace();
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
            skipWhitespace();
            while (true) {
                if (test('+')) {
                    result = new Add(result, parseMultiplyDivide());
                } else if (test('-')) {
                    result = new Subtract(result, parseMultiplyDivide());
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression parseMultiplyDivide() throws ParseExpressionException {
            ArithmeticExpression result = parseConstVariableNotCount();
            skipWhitespace();
            while (true) {
                if (test('*')) {
                    result = new Multiply(result, parseConstVariableNotCount());
                } else if (test('/')) {
                    result = new Divide(result, parseConstVariableNotCount());
                } else {
                    return result;
                }
            }
        }

        private ArithmeticExpression parseConstVariableNotCount() throws ParseExpressionException {
            ArithmeticExpression result;
            skipWhitespace();
            if (test('(')) {
                result = parseOr();
                expect(')');
            } else if (test('-')) {
                if (between('0', '9')) {
                    result = parseConst("-");
                } else {
                    result = new UnaryMinus(parseConstVariableNotCount());
                }
            } else if (test('~')) {
                result = new Not(parseConstVariableNotCount());
            } else if (test('c')) {
                expect("ount");
                result = new Count(parseConstVariableNotCount());
            } else if (between('0', '9')) {
                result = parseConst("");
            } else if (Character.isLetter(getChar())) {
                String varName = parseVariable();
                if (!(varName.equals("x") || varName.equals("y") || varName.equals("z"))) {
                    throw error("Variable must have name 'x', 'y' or 'z'");
                }
                result = new Variable(varName);
            } else {
                throw error("Unknown expression");
            }
            skipWhitespace();
            return result;
        }

        private String parseVariable() {
            StringBuilder varName = new StringBuilder();
            while (Character.isLetter(getChar())) {
                varName.append(getChar());
                nextChar();
            }
            return varName.toString();
        }

        private ArithmeticExpression parseConst(String prefix) throws ParseExpressionException {
            StringBuilder constNumber = new StringBuilder(prefix);
            while (between('0', '9')) {
                constNumber.append(getChar());
                nextChar();
            }
            try {
                return new Const(Integer.parseInt(constNumber.toString()));
            } catch (NumberFormatException e) {
                throw error("Wrong const number");
            }
        }
    }
}
