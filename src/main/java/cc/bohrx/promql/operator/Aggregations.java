package cc.bohrx.promql.operator;


import cc.bohrx.promql.expression.AggregationExpression.Builder;
import cc.bohrx.promql.expression.ParamlizedAggregationExpression;
import cc.bohrx.promql.expression.type.vector.Vector;
import cc.bohrx.promql.operator.api.FunctionOperator;
import cc.bohrx.promql.operator.api.IOperator;

import static cc.bohrx.promql.operator.Functions.*;


public enum Aggregations implements IOperator {
    SUM(new Builder("sum")),
    MIN(new Builder("min")),
    MAX(new Builder("max")),
    AVG(new Builder("avg")),
    STDDEV(new Builder("stddev")),
    STDVAR(new Builder("stdvar")),
    COUNT(new Builder("count")),
    COUNT_VALUES(new ParamlizedAggregationExpression.Builder("count_values")),
    BOTTOMK(new ParamlizedAggregationExpression.Builder("bottomk")),
    TOPK(new ParamlizedAggregationExpression.Builder("topk")),
    QUANTILE(new ParamlizedAggregationExpression.Builder("quantile"));

    enum OverTime {
        AVG(AVG_OVER_TIME),
        MIN(MIN_OVER_TIME),
        MAX(MAX_OVER_TIME),
        SUM(SUM_OVER_TIME),
        COUNT(COUNT_OVER_TIME),
        QUANTILE(QUANTILE_OVER_TIME),
        STDDEV(STDDEV_OVER_TIME),
        STDVAR(STDVAR_OVER_TIME);
        private FunctionOperator func;

        OverTime(FunctionOperator func) {
            this.func = func;
        }

        public FunctionOperator getFunc() {
            return func;
        }
    }


    private final Builder builder;

    Aggregations(Builder builder) {
        this.builder = builder;
    }

    @Override
    public String getLiteral() {
        return builder.getLiteral();
    }

    public Builder newAggregationExpr() {
        return new Builder(builder);
    }


    public static void main(String[] args) {
        Vector v2 = Vector.metric("cpu_await").build();
        System.out.println(SUM.newAggregationExpr().apply(v2).without("list", "fabric").build().parse());
        System.out.println(AVG.newAggregationExpr().apply(v2).build().parse());
    }
}
