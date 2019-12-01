package cc.bohrx.promql.bean;

import cc.bohrx.promql.bean.expression.InstantVector;
import cc.bohrx.promql.constant.operator.AggregationSiftWay;
import cc.bohrx.promql.exception.PromQLParseException;

import java.util.List;

import static cc.bohrx.promql.constant.Constant.*;

public class ParamlizedAggregationExpression extends AggregationExpression {

    private final String param;

    protected ParamlizedAggregationExpression(String literal, String param, InstantVector vector, List<String> filterLabelList, AggregationSiftWay siftWay) {
        super(literal, vector, filterLabelList, siftWay);
        this.param = param;
    }

    @Override
    public String parse() throws PromQLParseException {
        String siftWay = "";
        String labels = "";
        //list不为空 siftWay不为空
        if (!filterLabelList.isEmpty()) {
            siftWay = SPACE + this.siftWay.getLiteral() + SPACE;
            labels = String.format(PARENTHESES, String.join(FILTER_SPLIT, filterLabelList));

        }
        return literal + String.format(PARENTHESES, param + FILTER_SPLIT + vector.parse()) + siftWay + labels;
    }

    public static class Builder extends AggregationExpression.Builder {

        public Builder(String funcName) {
            super(funcName);
        }

        public Builder(Builder builder) {
            super(builder);
            param = builder.param;
        }

        @Override
        public ParamlizedAggregationExpression build() {
            if (null == param) {
                throw new IllegalArgumentException("param can't be null in ParamlizedAggregationExpression!");
            }
            return (ParamlizedAggregationExpression) super.build();
        }
    }
}
