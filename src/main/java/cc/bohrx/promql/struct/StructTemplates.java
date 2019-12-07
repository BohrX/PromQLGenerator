package cc.bohrx.promql.struct;

import cc.bohrx.promql.expression.Expression;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static cc.bohrx.promql.constant.Constant.*;
import static cc.bohrx.promql.operator.api.FunctionOperator.DEFAULT_PARAM;


public class StructTemplates {

    public static String printBinaryOperationStruct(Expression arg1, String operatorLiteral, Expression arg2) {
        return arg1.parse() + SPACE + operatorLiteral + SPACE + arg2.parse();
    }

    public static String printFunctionLikeStruct(String funcName, Expression... args) {
        return funcName + String.format(PARENTHESES, concatArgList(args));
    }

    public static String printFunctionLikeStruct(String funcName, String... args) {
        return funcName + String.format(PARENTHESES, String.join(COMMA, args));
    }

    private static String concatArgList(Expression... args) {
        return Arrays.asList(args).stream().filter(Predicate.isEqual(DEFAULT_PARAM).negate()).map(Expression::parse).collect(Collectors.joining(COMMA));
    }
}
