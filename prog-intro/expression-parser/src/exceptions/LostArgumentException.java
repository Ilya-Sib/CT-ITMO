package expression.exceptions;

public class LostArgumentException extends ParseExpressionException {
    public LostArgumentException(final String message) {
        super(message);
    }
}
