package cc.bohrx.promql.expression.type;

import cc.bohrx.promql.exception.PromQLParseException;
import cc.bohrx.promql.expression.Expression;

import static cc.bohrx.promql.constant.Constant.STRING_PARTERN;

public class StringExpr implements Expression {

    /**
     * parse return ""
     */
    public static final StringExpr NOT_EXIST = new StringExpr("") {
        @Override
        public String parse() throws PromQLParseException {
            return "";
        }
    };

    /**
     * parse() return "\"\""
     */
    public static final StringExpr EMPTY = new StringExpr("");

    private String value;

    private StringExpr(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static StringExpr of(String value) {
        if (null == value || value.isEmpty()) {
            return EMPTY;
        }
        return new StringExpr(value);
        //可以做个缓存
    }

    public static boolean isEmpty(StringExpr string) {
        return null == string || string == NOT_EXIST || string == EMPTY || null == string.value || "".equals(string.value);
    }

    @Override
    public String parse() throws PromQLParseException {
        return String.format(STRING_PARTERN, value);
    }
}
