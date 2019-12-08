package cc.bohrx.promql.operator.api;


import cc.bohrx.promql.expression.BinaryOperation;
import cc.bohrx.promql.expression.Expression;

public interface BinaryOperator extends IOperator {

    int MAX_PRIORITY = 6;

    int getPriority();

    BinaryOperation<? extends BinaryOperator> apply(Expression arg1, Expression arg2);

}
