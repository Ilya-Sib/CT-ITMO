import java.util.Arrays;
import java.io.IOException;

public class ReverseHexDec {
    public static int[] overflowChecker(int[] array, int index) {
        if (index == array.length){
            array = Arrays.copyOf(array, array.length*2);
        }
        return array;
    }

    public static void main(String[] args) throws IOException {
        int totalNums = 0;  
        int totalLines = 0; 
        int[] allNums = new int[1];  
        int[] numsInLine = new int[1];  
        FastScanner.Checker check = new FastScanner.Checker() {
            @Override
            public boolean isWordOrNum (int c) {
                return Character.isLetterOrDigit(c) || (char) c == '-';
            }
        };
        FastScanner scanner = new FastScanner(System.in, "UTF-8", check);  
        try {
            while (scanner.hasNext()) {
                FastScanner nextLine = new FastScanner(scanner.nextLine(), check);
                try {
                    int count = 0;
                    while (nextLine.hasNext()) {
                        allNums = overflowChecker(allNums, totalNums);
                        allNums[totalNums++] = nextLine.nextInt();
                        count++;
                    }
                    numsInLine = overflowChecker(numsInLine, totalLines);
                    numsInLine[totalLines++] = count;  
                } finally {
                    nextLine.close();
                }
            }
        } finally {
            scanner.close();
        }
        int numIndex = totalNums - 1;
        for (int i = (totalLines - 1); i > -1; i--) {
            for (int j = (numsInLine[i] - 1); j > -1; j--) {
                System.out.print(allNums[numIndex--]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }
}