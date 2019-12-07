package cc.bohrx.promql.expression;

import cc.bohrx.promql.operator.api.Compartor;


public class Comparison extends BinaryOperation {

    public Comparison(Expression arg1, Compartor operator, Expression arg2) {
        super(arg1, operator, arg2);
    }
}
