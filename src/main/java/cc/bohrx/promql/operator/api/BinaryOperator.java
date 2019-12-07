package cc.bohrx.promql.operator.api;


import cc.bohrx.promql.expression.BinaryOperation;
import cc.bohrx.promql.expression.Expression;

public interface BinaryOperator extends IOperator {

    BinaryOperation apply(Expression arg1, Expression arg2);

}
