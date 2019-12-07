package cc.bohrx.promql.operator;


import cc.bohrx.promql.expression.BinaryOperation;
import cc.bohrx.promql.expression.Expression;
import cc.bohrx.promql.operator.api.BinaryOperator;

public enum SetOperators implements BinaryOperator {
    INTERSECT("add"), UNION("or"), COMPLEMENT("unless");
    private String literal;

    SetOperators(String literal) {
        this.literal = literal;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    @Override
    public BinaryOperation apply(Expression arg1, Expression arg2) {
        return new BinaryOperation(arg1, this, arg2);
    }
}
