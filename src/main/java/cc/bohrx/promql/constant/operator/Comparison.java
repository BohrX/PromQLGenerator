package cc.bohrx.promql.constant.operator;

import cc.bohrx.promql.bean.expression.Expression;
import cc.bohrx.promql.bean.expression.Scalar;
import cc.bohrx.promql.bean.expression.SiftOperation;

public enum Comparison implements BinaryOperator {
    EQUAL("=="), NOT_EQUAL("!="), GREATER(">"), LESS("<"), NOT_LESS(">="), NOT_GREATER("<=");


    private String literal;

    Comparison(String literal) {
        this.literal = literal;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    @Override
    public Expression packExpr(double arg1, double arg2) {
        //标量对标量 没得筛选
        return new SiftOperation(Scalar.of(arg1), this, Scalar.of(arg2));
    }

    public Expression packSiftExpr(double arg1, Expression arg2) {
        return new SiftOperation(Scalar.of(arg1), this, arg2);
    }

    public Expression packSiftExpr(Expression arg1, double arg2) {
        return new SiftOperation(arg1, this, Scalar.of(arg2));
    }

    public Expression packSiftExpr(Expression arg1, Expression arg2) {
        return new SiftOperation(arg1, this, arg2);
    }
}
