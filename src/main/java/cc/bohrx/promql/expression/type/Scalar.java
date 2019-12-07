package cc.bohrx.promql.expression.type;

import cc.bohrx.promql.exception.PromQLParseException;
import cc.bohrx.promql.expression.Expression;

public class Scalar implements Expression {

    private final double value;

    private Scalar(double value) {
        this.value = value;
    }

    public static Scalar of(double value) {
        return new Scalar(value);
    }

    @Override
    public String parse() throws PromQLParseException {
        String str = String.valueOf(value);
        return isWholeNumber(value) ? str.substring(0, str.indexOf('.')) : str;
    }

    @Override
    public String toString() {
        return parse();
    }

    /**
     * 判断double是否是整数
     *
     * @param obj
     * @return
     */
    private static boolean isWholeNumber(double obj) {
        double eps = Double.MIN_VALUE;  // 精度范围
        return obj - Math.floor(obj) < eps;
    }
}
