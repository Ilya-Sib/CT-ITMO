import java.io.*;
import java.util.HashMap;
import java.util.Arrays;

public class WordStatWordsPrefix{
	public static void main(String[] args) {
		try {
			HashMap<String, Integer> res = new HashMap<>();
			FastScanner.Checker check = new FastScanner.Checker() {
				@Override
            	public boolean isWordOrNum (int c) {
                	return Character.getType(c) == Character.DASH_PUNCTUATION || Character.isLetter(c) || c == '\'';
            	}
        	};
			FastScanner reader = new FastScanner(args[0], "UTF-8", check);
			int len = 3;
			try {
				while (reader.hasNext()){
					String word = reader.next();
					if (word.length() >= len) {
		               	String key = word.substring(0, len).toString().toLowerCase();
		               	int value = res.getOrDefault(key, 0);
	                    res.put(key, ++value);
	                }
				}
			} finally {
				reader.close();
			}			
			String[] order = res.keySet().toArray(new String[0]);
			Arrays.sort(order);
			Writer writer = new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8");
			try {
				for (String i : order) {
					writer.write(i + ' ' + res.get(i) + '\n');
				}
			} finally {
				writer.close();
			}
		} catch (IOException e) {
            System.out.println("Input/Output error: " + e.getMessage());
        } 
	}
}