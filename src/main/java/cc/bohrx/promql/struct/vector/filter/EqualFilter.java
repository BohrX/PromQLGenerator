package cc.bohrx.promql.struct.vector.filter;


import cc.bohrx.promql.expression.type.StringExpr;
import cc.bohrx.promql.operator.MatchOperators;

import static cc.bohrx.promql.operator.MatchOperators.EQUAL;
import static cc.bohrx.promql.operator.MatchOperators.NOT_EQUAL;


public class EqualFilter extends Filter {

    private final StringExpr condition;

    public EqualFilter(String label, String condition) {
        this(label, condition, false);
    }

    public EqualFilter(String label, String condition, boolean negate) {
        this(label, negate ? NOT_EQUAL : EQUAL, (null == condition || condition.isEmpty()) ? StringExpr.EMPTY : StringExpr.of(condition));
    }

    private EqualFilter(String label, MatchOperators negateMatcher, StringExpr condition) {
        super(label, negateMatcher);
        this.condition = condition;
    }

    @Override
    public StringExpr condition() {
        return condition;
    }

    @Override
    public EqualFilter negate() {
        return new EqualFilter(label, matcher.negate(), condition);
    }
}