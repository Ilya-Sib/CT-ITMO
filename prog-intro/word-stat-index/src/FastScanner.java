import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.CharArrayReader;
import java.io.FileInputStream;

public class FastScanner implements AutoCloseable {
    private final Reader reader;
    private final char[] buffer = new char[1024];
    private final Checker checker;
    private int position = 0;
    private int charsInBuffer = 0;

    public interface Checker {
        boolean isWordOrNum(int c);
    }

    public FastScanner(String fileName, String charsetName, Checker check) throws IOException {
        this(new FileInputStream(fileName), charsetName, check);
    }

    public FastScanner(InputStream input, String charsetName, Checker check) throws IOException {
        reader = new InputStreamReader(input, charsetName);
        checker = check;
    }

    public FastScanner(StringBuilder string, Checker check) {
        char[] charArray = new char[string.length()];
        string.getChars(0, string.length(), charArray, 0);
        reader = new CharArrayReader(charArray);
        checker = check;
    }

    public void close() throws IOException {
        reader.close();
    }

    private boolean bufferEnd() {
        return charsInBuffer == position;
    }

    private void read() throws IOException {
        charsInBuffer = reader.read(buffer);
        position = 0;
    }

    private int nextChar() throws IOException {
        if (bufferEnd()) {
            read();
        }
        if (charsInBuffer == -1) {
            return -1;
        }
        return buffer[position++];
    }

    public boolean hasNext() throws IOException {
        skipSpace();
        return charsInBuffer != -1;
    }

    private void skipSpace() throws IOException {
        int c = nextChar();
        while (!checker.isWordOrNum(c) && c != -1 && c != '\n' && c != '\r') {
            c = nextChar();
        }
        position--;
    }

    public String next() throws IOException {
        StringBuilder res = new StringBuilder();
        int c = nextChar();
        while (checker.isWordOrNum(c)) {
            res.append((char) c);
            c = nextChar();
        }
        return res.toString();
    }

    public StringBuilder nextLine() throws IOException {
        StringBuilder res = new StringBuilder();
        int c = nextChar();
        while (c != -1) {
            if (c == '\n') {
                return res;
            }
            if (c == '\r') {
                c = nextChar();
                if (c != '\n') {
                    position--;
                }
                return res;
            }
            res.append((char) c);
            c = nextChar();
        }
        return res;
    }
}