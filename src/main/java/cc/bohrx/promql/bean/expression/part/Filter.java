package cc.bohrx.promql.bean.expression.part;

import cc.bohrx.promql.constant.operator.Selector;

import static cc.bohrx.promql.constant.Constant.DOUBLE_QUOTES;
import static cc.bohrx.promql.constant.Constant.METRIC_NAME_LABEL;
import static cc.bohrx.promql.constant.operator.Selector.*;

public abstract class Filter {

    public static final Filter EMPTY_FILTER = new EmptyFilter();

    protected final String label;

    protected final Selector selector;

    public Filter(String label, Selector selector) {
        this.label = label;
        this.selector = selector;
    }

    public String label() {
        return label;
    }

    public Selector operator() {
        return selector;
    }

    public abstract String condition();

    public abstract Filter negate();

    @Override
    public String toString() {
        //todo check 能不能条件传空等于无条件
        return (null == condition() || "".equals(condition())) ? "" : label() + operator().toLiteral() + DOUBLE_QUOTES + condition() + DOUBLE_QUOTES;
    }

    private static class EmptyFilter extends Filter {

        public EmptyFilter() {
            super("", null);
        }

        @Override
        public String condition() {
            return "";
        }

        @Override
        public Filter negate() {
            return this;
        }
    }

    public static class Builder {

        public static final Builder METRIC_NAME = new Builder(METRIC_NAME_LABEL);

        private final String label;

        private Builder(String label) {
            this.label = label;
        }

        public static Builder label(String label) {
            return new Builder(label);
        }

        public static Builder metricNameFilterBuilder() {
            return METRIC_NAME;
        }

        public Filter operate(Selector selector, String condition) {
            if (null == selector) {
                throw new IllegalArgumentException("selector can't be null!");
            }
            return selector.newFilter(METRIC_NAME_LABEL, condition);
        }

        public EqualFilter equal(String condition) {
            return (EqualFilter) EQUAL.newFilter(label, condition);
        }

        public EqualFilter notEqual(String condition) {
            return (EqualFilter) NOT_EQUAL.newFilter(label, condition);
        }

        public MatchFilter match(String regex) {
            return (MatchFilter) MATCH.newFilter(label, regex);
        }

        public MatchFilter misMatch(String regex) {
            return (MatchFilter) NOT_MATCH.newFilter(label, regex);
        }
    }
}