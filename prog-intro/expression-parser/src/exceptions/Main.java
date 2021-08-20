package expression.exceptions;

import expression.Expression;
import expression.TripleExpression;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws ParseExpressionException {
        TripleExpression exception = new ExpressionParser().parse("?");
        System.out.println(exception.evaluate(1, 1, 1));
        System.out.println(exception.toString());
//        System.out.println("x\tf");
//        for (int i = 0; i < 11; i++) {
//            try {
//                System.out.println(i + "\t" + exception.evaluate(i));
//            } catch (DivisionByZeroException e) {
//                System.out.println(i + "\tdivision by zero");
//            } catch (OverflowException e) {
//                System.out.println(i + "\toverflow");
//            }
//        }
    }
}
