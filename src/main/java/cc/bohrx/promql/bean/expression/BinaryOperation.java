package cc.bohrx.promql.bean.expression;

import cc.bohrx.promql.constant.operator.BinaryOperator;
import cc.bohrx.promql.exception.PromQLParseException;

import static cc.bohrx.promql.constant.Constant.SPACE;

public class BinaryOperation implements Expression {

    protected final Expression arg1;

    protected final BinaryOperator operator;

    protected final Expression arg2;


    public BinaryOperation(Expression arg1, BinaryOperator operator, Expression arg2) {
        this.arg1 = arg1;
        this.operator = operator;
        this.arg2 = arg2;
    }

    @Override
    public String parse() throws PromQLParseException {
        return arg1.parse() + SPACE + operator.getLiteral() + SPACE + arg2.parse();
    }

    @Override
    public String toString() {
        return parse();
    }
}
