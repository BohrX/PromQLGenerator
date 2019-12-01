package cc.bohrx.promql.exception;

public class PromQLParseException extends RuntimeException {

    private static final long serialVersionUID = -875156698572739364L;

    public PromQLParseException() {
    }

    public PromQLParseException(String message) {
        super(message);
    }

    public PromQLParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public PromQLParseException(Throwable cause) {
        super(cause);
    }

    public PromQLParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
