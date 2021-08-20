package ticTacToe;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Main {
    public static void main(String[] args) {
        final DataReader data = new DataReader();
        data.read();
        final Game game = new Game(true, new HumanPlayer(), data.getNumberOfRandomPlayers());
        final int result;
        if (data.isSquareBoard()) {
            result = game.play(new TicTacToeBoard(data.getM(), data.getN(), data.getK(), data.getNumberOfRandomPlayers()));
        } else {
            result = game.play(new MnkRhombusBoard(data.getN(), data.getK(), data.getNumberOfRandomPlayers()));
        }
        System.out.println("Game result: " + result);
        // 1 <= result <= 4 - player № result win
        // result == 0 - draw
        // result == -2 - somebody cheating
    }
}
