import java.io.IOException;
import java.io.Writer;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;


public class WordStatCountLineIndex {
    public static void main(String[] args) {
        try {
            int index = 1;
            int indexLine = 1;
            Map<String, IntList> res = new LinkedHashMap<>();
            FastScanner.Checker check = new FastScanner.Checker() {
                @Override
                public boolean isWordOrNum(int c) {
                    return Character.getType(c) == Character.DASH_PUNCTUATION || Character.isLetter(c) || c == '\'';
                }
            };
            try (FastScanner scanner = new FastScanner(args[0], "UTF-8", check)) {
                while (scanner.hasNext()) {
                    try (FastScanner nextLine = new FastScanner(scanner.nextLine(), check)) {
                        while (nextLine.hasNext()) {
                            String key = nextLine.next().toLowerCase();
                            IntList value = res.getOrDefault(key, new IntList(2, ":"));
                            value.addVector(new int[]{indexLine, index++});
                            res.put(key, value);
                        }
                        index = 1;
                        indexLine++;
                    }
                }
            }
            List<Map.Entry<String, IntList>> list = new ArrayList<>(res.entrySet());
            res.clear();
            list.sort(new Comparator<>() {
                @Override
                public int compare(Map.Entry<String, IntList> first, Map.Entry<String, IntList> second) {
                    return first.getValue().length - second.getValue().length;
                }
            });
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8)) {
                for (Map.Entry<String, IntList> entry : list) {
                    writer.write(entry.getKey() + " " + entry.getValue() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Input/Output error: " + e.getMessage());
        }
    }
}