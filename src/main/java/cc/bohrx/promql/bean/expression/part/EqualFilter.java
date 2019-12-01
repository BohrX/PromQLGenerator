package cc.bohrx.promql.bean.expression.part;

import cc.bohrx.promql.constant.operator.Selector;

import static cc.bohrx.promql.constant.operator.Selector.EQUAL;
import static cc.bohrx.promql.constant.operator.Selector.NOT_EQUAL;

public class EqualFilter extends Filter {

    private final String condition;

    public EqualFilter(String label, String condition) {
        this(label, condition, false);
    }

    public EqualFilter(String label, String condition, boolean negate) {
        super(label, negate ? NOT_EQUAL : EQUAL);
        this.condition = null == condition ? "" : condition;
    }

    private EqualFilter(String label, Selector negateSelector, String condition) {
        super(label, negateSelector);
        this.condition = null == condition ? "" : condition;
    }

    @Override
    public String condition() {
        return condition;
    }

    @Override
    public EqualFilter negate() {
        return new EqualFilter(label, selector.negate(), condition);
    }
}