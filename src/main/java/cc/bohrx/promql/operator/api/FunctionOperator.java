package cc.bohrx.promql.operator.api;

import cc.bohrx.promql.exception.PromQLParseException;
import cc.bohrx.promql.expression.Expression;
import cc.bohrx.promql.expression.Function;

public interface FunctionOperator extends IOperator {

    Expression DEFAULT_PARAM = new Expression() {
        @Override
        public String parse() throws PromQLParseException {
            return "";
        }
    };

    interface Supplier extends FunctionOperator {
        Function apply();
    }

    interface UniFunc extends FunctionOperator {
        Function apply(Expression param);
    }

    interface BinaryFunc extends FunctionOperator {
        Function apply(Expression param1, Expression param2);
    }

    interface TriFunc extends FunctionOperator {
        Function apply(Expression param1, Expression param2, Expression param3);
    }

    interface QuartFunc extends FunctionOperator {
        Function apply(Expression param1, Expression param2, Expression param3, Expression param4);
    }

    interface QuintFunc extends FunctionOperator {
        Function apply(Expression param1, Expression param2, Expression param3, Expression param4, Expression param5);
    }
}
