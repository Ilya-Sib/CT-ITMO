package ticTacToe;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DataReader {
    private final Scanner scanner;
    private int m, n, k, numberOfRandomPlayers;
    private String gameName;

    public DataReader() {
        scanner = new Scanner(System.in);
    }

    public void read() {
        try {
            System.out.println("What game would you like to play?\n(TicTacToe or TicTacToeRhombus)");
            gameName = scanner.next();
            if (!(gameName.equals("TicTacToe") || gameName.equals("TicTacToeRhombus"))) {
                throw new IllegalArgumentException("I don't know " + gameName + " game =(");
            }
            System.out.println("How many random opponents would you like to see?\n(It must be integer and positive numbers from 1 to 3)");
            numberOfRandomPlayers = scanner.nextInt();
            if (3 < numberOfRandomPlayers || numberOfRandomPlayers < 1) {
                throw new IllegalArgumentException("Wrong number of opponents");
            }
            if (gameName.equals("TicTacToe")) {
                System.out.println("Write parameters m, n and k:\n(It must be integer and positive numbers)");
                n = scanner.nextInt();
                m = scanner.nextInt();
            } else {
                System.out.println("Write parameters n and k:\n(It must be integer and positive numbers)");
                n = scanner.nextInt();
                m = 2 * n - 1;
            }
            k = scanner.nextInt();
            if (m < 1 || n < 1 || k < 1) {
                throw new IllegalArgumentException("Some parameter not positive");
            } else if (k > Math.max(m, n)) {
                throw new IllegalArgumentException("Nobody can win because k too big");
            }
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException("Parameters must be integer numbers");
        }
    }

    public boolean isSquareBoard() {
        return gameName.equals("TicTacToe");
    }

    public int getNumberOfRandomPlayers() {
        return numberOfRandomPlayers;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int getK() {
        return k;
    }
}
