package cc.bohrx.promql.operator;

import cc.bohrx.promql.expression.Expression;
import cc.bohrx.promql.expression.Function;
import cc.bohrx.promql.expression.type.vector.Vector;
import cc.bohrx.promql.operator.api.FunctionOperator;

public interface Functions {

    SupplierImpl TIME = new SupplierImpl("time");

    UniFuncHasDefaultVal MINUTE = new UniFuncHasDefaultVal("minute");

    UniFuncHasDefaultVal HOUR = new UniFuncHasDefaultVal("hour");

    UniFuncHasDefaultVal DAY_OF_MONTH = new UniFuncHasDefaultVal("day_of_month");

    UniFuncHasDefaultVal DAY_OF_WEEK = new UniFuncHasDefaultVal("day_of_week");

    UniFuncHasDefaultVal DAYS_IN_MONTH = new UniFuncHasDefaultVal("days_in_month");

    UniFuncHasDefaultVal MONTH = new UniFuncHasDefaultVal("month");

    UniFuncHasDefaultVal YEAR = new UniFuncHasDefaultVal("year");

    UniFuncImpl ABS = new UniFuncImpl("abs");
    UniFuncImpl ABSENT = new UniFuncImpl("absent");
    UniFuncImpl AVG_OVER_TIME = new UniFuncImpl("avg_over_time");
    UniFuncImpl CEIL = new UniFuncImpl("ceil");
    UniFuncImpl CHANGES = new UniFuncImpl("changes");
    UniFuncImpl COUNT_OVER_TIME = new UniFuncImpl("count_over_time");
    UniFuncImpl DELTA = new UniFuncImpl("delta");
    UniFuncImpl DERIV = new UniFuncImpl("deriv");
    UniFuncImpl EXP = new UniFuncImpl("exp");
    UniFuncImpl FLOOR = new UniFuncImpl("floor");
    UniFuncImpl IDELTA = new UniFuncImpl("idelta");
    UniFuncImpl INCREASE = new UniFuncImpl("increase");
    UniFuncImpl IRATE = new UniFuncImpl("irate");
    UniFuncImpl LN = new UniFuncImpl("ln");
    UniFuncImpl LOG10 = new UniFuncImpl("log10");
    UniFuncImpl LOG2 = new UniFuncImpl("log2");
    UniFuncImpl MAX_OVER_TIME = new UniFuncImpl("max_over_time");
    UniFuncImpl MIN_OVER_TIME = new UniFuncImpl("min_over_time");
    UniFuncImpl RATE = new UniFuncImpl("rate");
    UniFuncImpl RESETS = new UniFuncImpl("resets");
    UniFuncImpl SCALAR = new UniFuncImpl("scalar");
    UniFuncImpl SORT = new UniFuncImpl("sort");
    UniFuncImpl SORT_DESC = new UniFuncImpl("sort_desc");
    UniFuncImpl SQRT = new UniFuncImpl("sqrt");
    UniFuncImpl STDDEV_OVER_TIME = new UniFuncImpl("stddev_over_time");
    UniFuncImpl STDVAR_OVER_TIME = new UniFuncImpl("stdvar_over_time");
    UniFuncImpl SUM_OVER_TIME = new UniFuncImpl("sum_over_time");
    UniFuncImpl TIMESTAMP = new UniFuncImpl("timestamp");
    UniFuncImpl VECTOR = new UniFuncImpl("vector");
    BinaryFuncHasDefaultVal ROUND = new BinaryFuncHasDefaultVal("round");
    BinaryFuncImpl HISTOGRAM_QUANTILE = new BinaryFuncImpl("histogram_quantile");
    BinaryFuncImpl QUANTILE_OVER_TIME = new BinaryFuncImpl("quantile_over_time");
    BinaryFuncImpl CLAMP_MAX = new BinaryFuncImpl("clamp_max");
    BinaryFuncImpl CLAMP_MIN = new BinaryFuncImpl("clamp_min");
    BinaryFuncImpl PREDICT_LINEAR = new BinaryFuncImpl("predict_linear");
    TriFuncImpl HOLT_WINTERS = new TriFuncImpl("holt_winters");
    QuartFuncHasVarLenthParam LABEL_JOIN = new QuartFuncHasVarLenthParam("label_join");
    QuintFuncImpl LABEL_REPLACE = new QuintFuncImpl("label_replace");

    public static void main(String[] args) {
        Vector v2 = Vector.metric("cpu_await").build();
        System.out.println(TIME.apply());
    }

    class SupplierImpl implements FunctionOperator.Supplier {

        private String literal;

        SupplierImpl(String literal) {
            this.literal = literal;
        }

        @Override
        public Function apply() {
            return new Function(this);
        }

        @Override
        public String getLiteral() {
            return literal;
        }
    }

    class UniFuncHasDefaultVal implements FunctionOperator.Supplier, FunctionOperator.UniFunc {


        private String literal;

        UniFuncHasDefaultVal(String literal) {
            this.literal = literal;
        }

        @Override
        public String getLiteral() {
            return literal;
        }

        @Override
        public Function apply() {
            return new Function(this);
        }

        @Override
        public Function apply(Expression param) {
            return new Function(this, param);
        }
    }

    class UniFuncImpl implements FunctionOperator.UniFunc {


        private String literal;

        UniFuncImpl(String literal) {
            this.literal = literal;
        }

        @Override
        public Function apply(Expression param) {
            return null;
        }

        @Override
        public String getLiteral() {
            return literal;
        }
    }

    class BinaryFuncHasDefaultVal implements FunctionOperator.UniFunc, FunctionOperator.BinaryFunc {


        private String literal;

        BinaryFuncHasDefaultVal(String literal) {
            this.literal = literal;
        }

        @Override
        public String getLiteral() {
            return literal;
        }


        @Override
        public Function apply(Expression param) {
            return new Function(this, param);
        }

        @Override
        public Function apply(Expression param1, Expression param2) {
            return new Function(this, param1, param2);
        }
    }

    class BinaryFuncImpl implements FunctionOperator.BinaryFunc {
        private String literal;

        public BinaryFuncImpl(String literal) {
            this.literal = literal;
        }

        @Override
        public String getLiteral() {
            return literal;
        }

        @Override
        public Function apply(Expression param1, Expression param2) {
            return new Function(this, param1, param2);
        }
    }

    class TriFuncImpl implements FunctionOperator.TriFunc {
        private String literal;

        public TriFuncImpl(String literal) {
            this.literal = literal;
        }

        @Override
        public String getLiteral() {
            return literal;
        }

        @Override
        public Function apply(Expression param1, Expression param2, Expression param3) {
            return new Function(this, param1, param2, param3);
        }
    }

    class QuartFuncHasVarLenthParam implements FunctionOperator.QuartFunc {
        private String literal;

        public QuartFuncHasVarLenthParam(String literal) {
            this.literal = literal;
        }

        @Override
        public String getLiteral() {
            return literal;
        }

        @Override
        public Function apply(Expression param1, Expression param2, Expression param3, Expression param4) {
            return new Function(this, param1, param2, param3, param4);
        }

        public Expression apply(Expression param1, Expression param2, Expression param3, Expression param4, Expression... expressions) {
            Expression[] params = new Expression[expressions.length + 4];
            params[0] = param1;
            params[1] = param2;
            params[2] = param3;
            params[3] = param4;
            System.arraycopy(expressions, 0, params, 4, expressions.length);
            return new Function(this, params);
        }
    }

    class QuintFuncImpl implements FunctionOperator.QuintFunc {
        private String literal;

        public QuintFuncImpl(String literal) {
            this.literal = literal;
        }

        @Override
        public String getLiteral() {
            return literal;
        }

        @Override
        public Function apply(Expression param1, Expression param2, Expression param3, Expression param4, Expression param5) {
            return new Function(this, param1, param2, param3, param4, param5);
        }
    }
}
