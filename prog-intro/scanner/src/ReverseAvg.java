import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

public class ReverseAvg {
    public static long[] overflowCheckerLong(long[] array, int index){
        if (index == array.length){
            array = Arrays.copyOf(array, array.length*2);
        }
        return array;
    }

    public static int[] overflowCheckerInt(int[] array, int index){
        if (index == array.length){
            array = Arrays.copyOf(array, array.length*2);
        }
        return array;
    }

    public static void main(String[] args) throws IOException {
        FastScanner.Checker check = new FastScanner.Checker() {
            @Override
            public boolean isWordOrNum (int c) {
                return Character.isDigit(c) || (char) c == '-';
            }
        };
        int totalNums = 0;  
        int totalLines = 0; 
        int[] allNums = new int[1];  
        int[] numsInLine = new int[1];  
        int[] numsInRow = new int[1];  
        long[] sumRow = new long[1];  
        long[] sumLine = new long[1]; 
        FastScanner scanner = new FastScanner(System.in, "UTF-8", check);  
        try{    
            while (scanner.hasNext()) {
                FastScanner nextLine = new FastScanner(scanner.nextLine(), check);
                try{
                    int count = 0;
                    while (nextLine.hasNext()) {
                        allNums = overflowCheckerInt(allNums, totalNums);
                        allNums[totalNums] = nextLine.nextInt();
                        sumRow = overflowCheckerLong(sumRow, count);
                        sumRow[count] += allNums[totalNums];
                        sumLine = overflowCheckerLong(sumLine, totalLines);
                        sumLine[totalLines] += allNums[totalNums++];
                        numsInRow = overflowCheckerInt(numsInRow, count);
                        numsInRow[count++]++;
                    }
                    numsInLine = overflowCheckerInt(numsInLine, totalLines);
                    sumLine = overflowCheckerLong(sumLine, totalLines);
                    numsInLine[totalLines++] = count;  
                } finally{
                    nextLine.close();
                }
            }
        } finally{
            scanner.close();
        }
        int numIndex = 0;
        for (int i = 0; i < totalLines; i++) {
            for (int j = 0; j < numsInLine[i]; j++) {
                System.out.print(((sumLine[i] + sumRow[j] - allNums[numIndex++]) / (numsInRow[j] + numsInLine[i] - 1)));
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}