package cc.bohrx.promql.expression;


import cc.bohrx.promql.exception.PromQLParseException;
import cc.bohrx.promql.operator.api.BinaryOperator;
import cc.bohrx.promql.struct.StructTemplates;

import static cc.bohrx.promql.constant.Constant.PARENTHESES;

public class BinaryOperation<T extends BinaryOperator> implements Expression {

    protected final Expression arg1;

    protected final T operator;

    protected final Expression arg2;


    public BinaryOperation(Expression arg1, T operator, Expression arg2) {
        this.arg1 = arg1;
        this.operator = operator;
        this.arg2 = arg2;
    }

    @Override
    public String parse() throws PromQLParseException {
        return StructTemplates.printBinaryOperationStruct(bracketIfNeeded(arg1), operator.getLiteral(), bracketIfNeeded(arg2));
    }

    private Expression bracketIfNeeded(Expression expression) {
        //数字越小越优先
        if (expression instanceof BinaryOperation<?>) {
            BinaryOperation<?> binaryOperation = (BinaryOperation) expression;
            if (binaryOperation.operator.getPriority() >= operator.getPriority()) {
                return BracketedOperation.bracket(binaryOperation);
            }
        }
        return expression;
    }

    private static class BracketedOperation implements Expression {

        BinaryOperation<?> binaryOperation;

        private BracketedOperation(BinaryOperation<?> binaryOperation) {
            this.binaryOperation = binaryOperation;
        }

        public static BracketedOperation bracket(BinaryOperation<?> binaryOperation) {
            return new BracketedOperation(binaryOperation);
        }

        @Override
        public String parse() throws PromQLParseException {
            return String.format(PARENTHESES, binaryOperation.parse());
        }
    }

    @Override
    public String toString() {
        return parse();
    }
}
