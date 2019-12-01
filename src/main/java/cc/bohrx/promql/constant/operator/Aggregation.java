package cc.bohrx.promql.constant.operator;

import cc.bohrx.promql.bean.AggregationExpression;
import cc.bohrx.promql.bean.AggregationExpression.Builder;
import cc.bohrx.promql.bean.ParamlizedAggregationExpression;
import cc.bohrx.promql.bean.expression.InstantVector;
import cc.bohrx.promql.bean.expression.Vector;

public enum Aggregation implements IOperator {
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


    private final Builder builder;

    Aggregation(AggregationExpression.Builder builder) {
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
        InstantVector v2 = Vector.Builder.metric("cpu_await").buildInstant();
        System.out.println(SUM.newAggregationExpr().target(v2).without("list", "fabric").build().parse());
        System.out.println(AVG.newAggregationExpr().target(v2).build().parse());
    }
}
