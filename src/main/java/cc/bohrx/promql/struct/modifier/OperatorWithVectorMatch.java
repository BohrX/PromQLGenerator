package cc.bohrx.promql.struct.modifier;


import cc.bohrx.promql.expression.BinaryOperation;
import cc.bohrx.promql.expression.Expression;
import cc.bohrx.promql.operator.api.VectorMatchableOperator;

import java.util.Objects;

public class OperatorWithVectorMatch<T extends VectorMatchableOperator> implements VectorMatchableOperator {

    private T operator;

    private VectorMatchModifier.Builder builder;

    public OperatorWithVectorMatch(T operator, VectorMatchModifier.Builder builder) {
        if (Objects.isNull(builder) || Objects.isNull(operator)) {
            throw new IllegalArgumentException("params can't be null!");
        }

        this.operator = operator;
        this.builder = builder;
    }

    @Override
    public int getPriority() {
        return operator.getPriority();
    }

    @Override
    public BinaryOperation<OperatorWithVectorMatch<T>> apply(Expression arg1, Expression arg2) {
        return new BinaryOperation<>(arg1, this, arg2);
    }

    @Override
    public String getLiteral() {
        return operator.getLiteral() + builder.build().print();
    }

    public OperatorWithVectorMatch setMatchLabels(String... matchLabels) {
        builder.setMatchLabels(matchLabels);
        return this;
    }

    public OperatorWithVectorMatch setGroupLabels(String... groupLabels) {
        builder.setGroupLabels(groupLabels);
        return this;
    }
}
