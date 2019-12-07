package cc.bohrx.promql.struct.modifier;


import cc.bohrx.promql.expression.BinaryOperation;
import cc.bohrx.promql.expression.Comparison;
import cc.bohrx.promql.expression.Expression;
import cc.bohrx.promql.operator.api.Compartor;
import cc.bohrx.promql.operator.api.VectorMatchableOperator;

import java.util.Objects;

public class OperatorWithVectorMatch<T extends VectorMatchableOperator> implements Compartor {

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
    public BinaryOperation apply(Expression arg1, Expression arg2) {
        return new Comparison(arg1, this, arg2);
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
