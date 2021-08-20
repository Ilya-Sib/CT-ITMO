public class IntList {
    private int[][] array;
    public int length;
    private final String delimiter;
    private final int vectorLength;

    public IntList(int vectorLength, String delimiter) {
        this.vectorLength = vectorLength;
        this.delimiter = delimiter;
        array = new int[1][vectorLength];
        length = 0;
    }

    public IntList() {
        this(1, " ");
    }

    public IntList(int vectorLength) {
        this(vectorLength, " ");
    }

    private int[][] overflowChecker(int[][] array, int length) {
        if (length == array.length) {
            int[][] temp = new int[array.length * 2][vectorLength];
            System.arraycopy(array, 0, temp, 0, array.length);
            return temp;
        }
        return array;
    }

    public void add(int num) {
        addVector(new int[]{num});
    }

    public void addVector(int[] vector) {
        array = overflowChecker(array, length);
        System.arraycopy(vector, 0, array[length++], 0, vectorLength);
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(length);
        for (int i = 0; i < length; i++) {
            res.append(" ");
            for (int j = 0; j < vectorLength; j++) {
                res.append(array[i][j]).append(delimiter);
            }
            res.deleteCharAt(res.length() - 1);
        }
        return res.toString();
    }
}
