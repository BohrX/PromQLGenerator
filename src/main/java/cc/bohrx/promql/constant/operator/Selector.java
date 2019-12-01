package cc.bohrx.promql.constant.operator;

import cc.bohrx.promql.bean.expression.part.EqualFilter;
import cc.bohrx.promql.bean.expression.part.Filter;
import cc.bohrx.promql.bean.expression.part.MatchFilter;

import java.util.regex.Pattern;

public enum Selector {
    EQUAL("=") {
        @Override
        public Selector negate() {
            return Selector.NOT_EQUAL;
        }

        @Override
        public EqualFilter newFilter(String label, String condition) {
            return new EqualFilter(label, condition);
        }
    }, NOT_EQUAL("!=") {
        @Override
        public Selector negate() {
            return Selector.EQUAL;
        }

        @Override
        public EqualFilter newFilter(String label, String condition) {
            return new EqualFilter(label, condition, true);
        }
    }, MATCH("=~") {
        @Override
        public Selector negate() {
            return Selector.NOT_MATCH;
        }

        @Override
        public MatchFilter newFilter(String label, String condition) {
            if (null == condition) {
                throw new IllegalArgumentException("condition(regex expression) can't be null!");
            }
            return new MatchFilter(label, Pattern.compile(condition));
        }
    }, NOT_MATCH("!=~") {
        @Override
        public Selector negate() {
            return Selector.MATCH;
        }

        @Override
        public MatchFilter newFilter(String label, String condition) {
            if (null == condition) {
                throw new IllegalArgumentException("condition(regex expression) can't be null!");
            }
            return new MatchFilter(label, Pattern.compile(condition), true);
        }
    };

    private String literal;

    Selector(String literal) {
        this.literal = literal;
    }

    public String toLiteral() {
        return literal;
    }

    public abstract Selector negate();

    public abstract Filter newFilter(String label, String condition);
}
