package ticTacToe;

public class ProtectedTicTacToeBoard implements Position {
    private final TicTacToeBoard board;

    public ProtectedTicTacToeBoard(TicTacToeBoard board) {
        this.board = board;
    }

    @Override
    public int getM() {
        return board.getM();
    }

    @Override
    public int getN() {
        return board.getN();
    }

    @Override
    public boolean isValid(Move move) {
        return board.isValid(move);
    }

    @Override
    public Cell getCell(int r, int c) {
        return board.getCell(r, c);
    }

    @Override
    public String toString() {
        return board.toString();
    }
}
