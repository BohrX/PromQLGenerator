package cc.bohrx.promql.expression;


import cc.bohrx.promql.struct.StructTemplates;

import java.util.List;

public class ParamlizedAggregationExpression extends AggregationExpression {

    private final String param;

    protected ParamlizedAggregationExpression(String literal, String param, Expression expression, List<String> filterLabelList, AggregationSiftWay siftWay) {
        super(literal, expression, filterLabelList, siftWay);
        this.param = param;
    }

    @Override
    protected String aggregationBody() {
        return StructTemplates.printFunctionLikeStruct(literal, param, expression.parse());
    }

    public static class Builder extends AggregationExpression.Builder {

        private String param;

        public Builder(String funcName) {
            super(funcName);
        }

        public Builder(Builder builder) {
            super(builder);
            param = builder.param;
        }

        protected Builder(AggregationExpression.Builder builder, String param) {
            super(builder);
            this.param = param;
        }

        @Override
        public ParamlizedAggregationExpression build() {
            if (null == expression) {
                throw new IllegalArgumentException("expression can't be null!");
            }
            if (null == param) {
                throw new IllegalArgumentException("param can't be null in ParamlizedAggregationExpression!");
            }
            return new ParamlizedAggregationExpression(literal, param, expression, filterLabelList, siftWay);
        }
    }
}
