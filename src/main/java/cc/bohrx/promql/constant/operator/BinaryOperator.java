package cc.bohrx.promql.constant.operator;

import cc.bohrx.promql.bean.expression.BinaryOperation;
import cc.bohrx.promql.bean.expression.Expression;
import cc.bohrx.promql.bean.expression.Scalar;

public interface BinaryOperator extends IOperator {


    default Expression packExpr(double arg1, double arg2) {
        return new BinaryOperation(Scalar.of(arg1), this, Scalar.of(arg2));
    }

    default Expression packExpr(double arg1, Expression arg2) {
        return new BinaryOperation(Scalar.of(arg1), this, arg2);
    }

    default Expression packExpr(Expression arg1, double arg2) {
        return new BinaryOperation(arg1, this, Scalar.of(arg2));
    }

    default Expression packExpr(Expression arg1, Expression arg2) {
        return new BinaryOperation(arg1, this, arg2);
    }
}
