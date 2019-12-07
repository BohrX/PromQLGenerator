package cc.bohrx.promql.struct.vector.filter;

import cc.bohrx.promql.expression.type.StringExpr;
import cc.bohrx.promql.operator.MatchOperators;

public abstract class Filter {

    public static final Filter EMPTY_FILTER = new EmptyFilter();

    protected final String label;

    protected final MatchOperators matcher;

    public Filter(String label, MatchOperators matcher) {
        this.label = label;
        this.matcher = matcher;
    }

    public String label() {
        return label;
    }

    public MatchOperators operator() {
        return matcher;
    }

    public abstract StringExpr condition();

    public abstract Filter negate();

    @Override
    public String toString() {
        //todo check 能不能条件传空等于无条件
        return (StringExpr.isEmpty(condition())) ? "" : label() + operator().getLiteral() + condition().parse();
    }

    private static class EmptyFilter extends Filter {

        public EmptyFilter() {
            super("", null);
        }

        @Override
        public StringExpr condition() {
            return StringExpr.NOT_EXIST;
        }

        @Override
        public Filter negate() {
            return this;
        }
    }

}