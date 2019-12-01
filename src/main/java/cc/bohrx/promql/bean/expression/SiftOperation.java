package cc.bohrx.promql.bean.expression;

import cc.bohrx.promql.constant.operator.BinaryOperator;
import cc.bohrx.promql.exception.PromQLParseException;

import static cc.bohrx.promql.constant.Constant.BOOL;
import static cc.bohrx.promql.constant.Constant.SPACE;

public class SiftOperation extends BinaryOperation {
    public SiftOperation(Expression arg1, BinaryOperator operator, Expression arg2) {
        super(arg1, operator, arg2);
    }

    @Override
    public String parse() throws PromQLParseException {
        return arg1.parse() + SPACE + operator.getLiteral() + SPACE + BOOL + SPACE + arg2.parse();
    }
}
