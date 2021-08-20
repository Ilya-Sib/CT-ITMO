import java.io.*;
import java.util.HashMap;
import java.util.Arrays;

public class WordStatWordsPrefix{
	public static boolean isItWord(char c) {
		return Character.getType(c) == Character.DASH_PUNCTUATION || Character.isLetter(c) || c == '\'';
	}

	public static void main(String[] args) {
		try {
			char[] buff = new char[8192];
			StringBuilder word = new StringBuilder();
			Map<String, Integer> res = new HashMap<String, Integer>();
			Reader reader = new InputStreamReader(new FileInputStream(args[0]), "UTF-8");
			int len = 3;
			try {
				while (true) {
					int charsInBuff = reader.read(buff);
					if (charsInBuff == -1) {
						break;
					}
					for (int i = 0; i < charsInBuff; i++) { 
						if (isItWord(buff[i])) {
		                    word.append(buff[i]);
		                } else {
		                    if (word.length() >= len) {
		                    	String key = word.substring(0, len).toString().toLowerCase();
		                    	int value = res.getOrDefault(key, 0);
		                        res.put(key, ++value);
	                        }
	                        word.setLength(0);
						}
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