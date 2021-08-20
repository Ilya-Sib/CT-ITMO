package ticTacToe;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Game {
    private final boolean log;
    private final Player[] players;
    private final int numberOfPlayers;

    public Game(final boolean log, final Player player1, int numberOfRandomPlayers) {
        this.log = log;
        this.numberOfPlayers = numberOfRandomPlayers + 1;
        players = new Player[numberOfPlayers];
        players[0] = player1;
        for (int i = 1; i < numberOfPlayers; i++) players[i] = new RandomPlayer();
    }

    public int play(Board board) {
        while (true) {
            final int[] result = new int[numberOfPlayers];
            for (int i = 0; i < numberOfPlayers; i++) {
                result[i] = move(board, players[i], i + 1);
                if (result[i] != -1) {
                    return result[i];
                }
            }
        }
    }

    private int move(final Board board, final Player player, final int no) {
        final Move move = player.move(board.getPosition(), board.getCell());
        final Result result = board.makeMove(move);
        log("Player " + no + " move: " + move);
        log("Position:\n" + board);
        if (result == Result.WIN) {
            log("Player " + no + " won");
            return no;
        } else if (result == Result.CHEAT) {
            log("Player " + no + " cheating");
            return -2;
        } else if (result == Result.DRAW) {
            log("Draw");
            return 0;
        } else {
            return -1;
        }
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
