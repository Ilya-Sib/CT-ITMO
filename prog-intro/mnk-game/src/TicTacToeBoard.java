package ticTacToe;

import java.util.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class TicTacToeBoard implements Board, Position {
    protected static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.T, '-',
            Cell.C, '|',
            Cell.E, '.'
    );

    protected final Cell[][] cells;
    protected int empty;
    private final int m, n, k, numberOfPlayers;
    private final Cell[] turn;
    private int turnIndex;

    public TicTacToeBoard(int m, int n, int k, int numberOfPlayers) {
        this.cells = new Cell[m][n];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        this.m = m;
        this.n = n;
        this.k = k;
        this.numberOfPlayers = numberOfPlayers + 1;
        empty = m * n;
        turn = new Cell[this.numberOfPlayers];
        turnIndex = 0;
        List<Cell> cellList = new ArrayList<>(List.of(Cell.X, Cell.O, Cell.T, Cell.C));
        for (int i = 0; i < this.numberOfPlayers; i++) {
            turn[i] = cellList.get(i);
        }
    }

    @Override
    public Position getPosition() {
        return new ProtectedTicTacToeBoard(this);
    }

    @Override
    public Cell getCell() {
        return turn[turnIndex];
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.CHEAT;
        }

        int row = move.getRow();
        int column = move.getColumn();

        cells[row][column] = move.getValue();
        empty--;

        if (checkWin(row, column)) {
            return Result.WIN;
        }

        if (empty == 0) {
            return Result.DRAW;
        }

        turnIndex++;
        turnIndex %= numberOfPlayers;

        return Result.UNKNOWN;
    }

    private boolean checkWin(int x, int y) {
        return checker(x, y, -1, 0) || checker(x, y, -1, 1)
                || checker(x, y, 0, 1) || checker(x, y, 1, 1);
    }

    private int counter(int x, int y, int dx, int dy) {
        final Cell cell = cells[x][y];
        int count = 0;
        x += dx;
        y += dy;
        while (0 <= x && x < m && 0 <= y && y < n && cells[x][y] == cell) {
            count++;
            x += dx;
            y += dy;
        }
        return count;
    }

    private boolean checker(int x, int y, int dx, int dy) {
        return counter(x, y, dx, dy) + counter(x, y, -dx, -dy) + 1 >= k;
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < m
                && 0 <= move.getColumn() && move.getColumn() < n
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn[turnIndex] == move.getValue();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                sb.append(SYMBOLS.get(cells[r][c])).append(' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public int getM() {
        return m;
    }

    @Override
    public int getN() {
        return n;
    }
}
