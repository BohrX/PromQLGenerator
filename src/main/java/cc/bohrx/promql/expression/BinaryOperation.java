package cc.bohrx.promql.expression;


import cc.bohrx.promql.exception.PromQLParseException;
import cc.bohrx.promql.operator.api.BinaryOperator;
import cc.bohrx.promql.struct.StructTemplates;

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
        return StructTemplates.printBinaryOperationStruct(arg1, operator.getLiteral(), arg2);
    }

    @Override
    public String toString() {
        return parse();
    }
}
