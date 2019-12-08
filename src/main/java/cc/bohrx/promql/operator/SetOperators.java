package cc.bohrx.promql.operator;


import cc.bohrx.promql.expression.BinaryOperation;
import cc.bohrx.promql.expression.Expression;
import cc.bohrx.promql.operator.api.BinaryOperator;

public enum SetOperators implements BinaryOperator {
    INTERSECT(5, "add"), UNION(6, "or"), COMPLEMENT(5, "unless");
    private final int priority;

    private String literal;

    SetOperators(int priority, String literal) {
        this.priority = priority;
        this.literal = literal;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    @Override
    public BinaryOperation<SetOperators> apply(Expression arg1, Expression arg2) {
        return new BinaryOperation<>(arg1, this, arg2);
    }
}
