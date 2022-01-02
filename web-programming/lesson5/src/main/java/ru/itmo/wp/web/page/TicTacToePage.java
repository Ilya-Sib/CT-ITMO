package ru.itmo.wp.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    public static final class State {
        private final int size;
        private final String[][] cells;
        private String phase;
        private boolean crossesMove;
        private int emptyCells;

        public State() {
            this.size = 3;
            this.cells = new String[size][size];
            this.phase = "RUNNING";
            this.crossesMove = true;
            this.emptyCells = size * size;
        }

        public int getSize() {
            return size;
        }

        public String[][] getCells() {
            return cells;
        }

        public String getPhase() {
            return phase;
        }

        public boolean isCrossesMove() {
            return crossesMove;
        }

        public void doMove(int row, int col) {
            if (correctMove(row, col)) {
                String value = crossesMove ? "X" : "O";
                cells[row][col] = value;
                emptyCells--;
                crossesMove = !crossesMove;
                checkWinOrDraw(row, col, value);
            }
        }

        private boolean correctMove(int row, int col) {
            return 0 <= row && row < size
                    && 0 <= col && col < size
                    && cells[row][col] == null;
        }

        public void checkWinOrDraw(int row, int col, String value) {
            if (checkWin(row, col, value)) {
                phase = "WON_" + value;
            } else if (emptyCells == 0) {
                phase = "DRAW";
            }
        }

        private boolean checkWin(int row, int col, String value) {
            int diag1Count = 0;
            int diag2Count = 0;
            int rowCount = 0;
            int colCount = 0;
            for (int dx = 0; dx < 3; dx++) {
                if (value.equals(cells[row][dx])) {
                    rowCount++;
                }
                if (value.equals(cells[dx][col])) {
                    colCount++;
                }
                if (value.equals(cells[dx][dx])) {
                    diag1Count++;
                }
                if (value.equals(cells[dx][2 - dx])) {
                    diag2Count++;
                }
            }
            return diag1Count == 3 || diag2Count == 3 || rowCount == 3 || colCount == 3;
        }
    }

    private void action(Map<String, Object> view, HttpServletRequest request) {
        newGame(request, view);
    }

    private void onMove(HttpServletRequest request, Map<String, Object> view) {
        for (Map.Entry<String, String[]> parameter : request.getParameterMap().entrySet()) {
            if (checkParameter(parameter)) {
                State state = (State) request.getSession().getAttribute("state");
                state.doMove(parameter.getKey().charAt(5) - '0',
                        parameter.getKey().charAt(6) - '0');
                view.put("state", state);
                break;
            }
        }
    }

    private boolean checkParameter(Map.Entry<String, String[]> parameter) {
        // Pattern.compile("cell_\\d{2}").
        return parameter.getKey().startsWith("cell_")
                && parameter.getKey().length() == 7
                && Character.isDigit(parameter.getKey().charAt(5))
                && Character.isDigit(parameter.getKey().charAt(6))
                && parameter.getValue()[0].equals(" ");
    }

    private void newGame(HttpServletRequest request, Map<String, Object> view) {
        State state = new State();
        view.put("state", state);
        request.getSession().setAttribute("state", state);
    }
}
