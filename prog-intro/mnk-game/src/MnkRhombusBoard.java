package ticTacToe;

public class MnkRhombusBoard extends TicTacToeBoard {
    private final int[] cellInRows;
    private final int n;

    public MnkRhombusBoard(int n, int k, int numberOfPlayers) {
        super(2 * n - 1, n, k, numberOfPlayers);
        this.cellInRows = new int[2 * n - 1];
        cellInRows[0] = 1;
        for (int i = 1; i < 2 * n - 1; i++) {
            cellInRows[i] = i < n ? cellInRows[i - 1] + 1 : cellInRows[i - 1] - 1;
        }
        this.n = n;
        removeExtraCells();
    }

    private void removeExtraCells() {
        for (int r = 0; r < 2 * n - 1; r++) {
            for (int c = cellInRows[r]; c < n; c++) {
                super.cells[r][c] = Cell.A;
            }
        }
        super.empty -= n * (n - 1);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 2 * n - 1; r++) {
            sb.append(" ".repeat(Math.max(0, n - cellInRows[r])));
            for (int c = 0; c < cellInRows[r]; c++) {
                sb.append(SYMBOLS.get(cells[r][c])).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public Cell getCell() {
        return super.getCell();
    }

    @Override
    public Result makeMove(Move move) {
        return super.makeMove(move);
    }

    @Override
    public boolean isValid(Move move) {
        return super.isValid(move);
    }

    @Override
    public Cell getCell(int r, int c) {
        return super.getCell(r, c);
    }

    @Override
    public int getM() {
        return super.getM();
    }

    @Override
    public int getN() {
        return super.getN();
    }
}
