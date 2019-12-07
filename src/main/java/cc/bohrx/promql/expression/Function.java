package cc.bohrx.promql.expression;

import cc.bohrx.promql.exception.PromQLParseException;
import cc.bohrx.promql.operator.api.FunctionOperator;
import cc.bohrx.promql.struct.StructTemplates;

public class Function implements Expression {

    private final FunctionOperator func;

    private final Expression[] expression;

    public Function(FunctionOperator func, Expression... expression) {
        this.func = func;
        this.expression = expression;
    }

    @Override
    public String parse() throws PromQLParseException {
        return StructTemplates.printFunctionLikeStruct(func.getLiteral(), expression);
    }

    @Override
    public String toString() {
        return parse();
    }
}
