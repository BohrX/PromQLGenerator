package cc.bohrx.promql.operator;

import cc.bohrx.promql.operator.api.MatchOperator;
import cc.bohrx.promql.struct.vector.filter.EqualFilter;
import cc.bohrx.promql.struct.vector.filter.Filter;
import cc.bohrx.promql.struct.vector.filter.MatchFilter;

import java.util.regex.Pattern;

import static cc.bohrx.promql.constant.Constant.METRIC_NAME_LABEL;

public enum MatchOperators implements MatchOperator {
    EQUAL("=") {
        @Override
        public MatchOperators negate() {
            return MatchOperators.NOT_EQUAL;
        }

        @Override
        public Filter apply(String label, String value) {
            return new EqualFilter(label, value);
        }
    }, NOT_EQUAL("!=") {
        @Override
        public MatchOperators negate() {
            return MatchOperators.EQUAL;
        }

        @Override
        public Filter apply(String label, String value) {
            return new EqualFilter(label, value, true);
        }
    }, MATCH("=~") {
        @Override
        public MatchOperators negate() {
            return MatchOperators.NOT_MATCH;
        }

        @Override
        public Filter apply(String label, String value) {
            if (null == value) {
                throw new IllegalArgumentException("condition(regex expression) can't be null!");
            }
            return new MatchFilter(label, Pattern.compile(value));
        }
    }, NOT_MATCH("!=~") {
        @Override
        public MatchOperators negate() {
            return MatchOperators.MATCH;
        }

        @Override
        public Filter apply(String label, String value) {
            if (null == value) {
                throw new IllegalArgumentException("condition(regex expression) can't be null!");
            }
            return new MatchFilter(label, Pattern.compile(value), true);
        }
    };

    public static Filter metricNameFilter(MatchOperators matchType, String value) {
        return matchType.apply(METRIC_NAME_LABEL, value);
    }

    private String literal;

    MatchOperators(String literal) {
        this.literal = literal;
    }

    public abstract MatchOperators negate();

    @Override
    public abstract Filter apply(String label, String value);

    @Override
    public String getLiteral() {
        return literal;
    }
}
