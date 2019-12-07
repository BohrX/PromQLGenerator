package cc.bohrx.promql.struct.vector.filter;


import cc.bohrx.promql.expression.type.StringExpr;
import cc.bohrx.promql.operator.MatchOperators;

import java.util.regex.Pattern;

import static cc.bohrx.promql.operator.MatchOperators.MATCH;
import static cc.bohrx.promql.operator.MatchOperators.NOT_MATCH;


public class MatchFilter extends Filter {

    private final Pattern pattern;

    private final StringExpr condition;

    public MatchFilter(String label, Pattern pattern) {
        this(label, pattern, false);
    }

    public MatchFilter(String label, Pattern pattern, boolean negate) {
        this(label, negate ? NOT_MATCH : MATCH, pattern);
    }

    private MatchFilter(String label, MatchOperators negateMatcher, Pattern pattern) {
        super(label, negateMatcher);
        this.pattern = pattern;
        condition = StringExpr.of(pattern.toString());
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public StringExpr condition() {
        return condition;
    }

    @Override
    public MatchFilter negate() {
        return new MatchFilter(label, matcher.negate(), pattern);
    }
}
