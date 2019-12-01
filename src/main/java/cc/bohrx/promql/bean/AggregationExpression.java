package cc.bohrx.promql.bean;

import cc.bohrx.promql.bean.expression.Expression;
import cc.bohrx.promql.bean.expression.InstantVector;
import cc.bohrx.promql.bean.expression.Scalar;
import cc.bohrx.promql.constant.operator.AggregationSiftWay;
import cc.bohrx.promql.exception.PromQLParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cc.bohrx.promql.constant.Constant.*;

//假定聚合函数都可以参加表达式运算
public class AggregationExpression implements Expression {
    /**
     * <aggr-op>([parameter,] <vector expression>) [without|by (<label list>)]
     */

    protected final String literal;

    protected final InstantVector vector;

    protected final List<String> filterLabelList;

    protected final AggregationSiftWay siftWay;

    protected AggregationExpression(String literal, InstantVector vector, List<String> filterLabelList, AggregationSiftWay siftWay) {
        this.literal = literal;
        this.vector = vector;
        this.filterLabelList = filterLabelList;
        this.siftWay = siftWay;
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
        return literal + String.format(PARENTHESES, vector.parse()) + siftWay + labels;
    }

    @Override
    public String toString() {
        return parse();
    }


    public static class Builder {
        private final String literal;

        private InstantVector vector;

        private List<String> filterLabelList = Collections.emptyList();

        private AggregationSiftWay siftWay;

        protected String param;

        public Builder(String funcName) {
            literal = funcName;
        }

        public Builder(Builder builder) {
            literal = builder.literal;
            vector = builder.vector;
            if (!builder.filterLabelList.isEmpty()) {
                filterLabelList = new ArrayList<>();
                filterLabelList.addAll(builder.filterLabelList);
            }
            siftWay = builder.siftWay;
            param = builder.param;
        }

        public String getLiteral() {
            return literal;
        }

        public Builder target(InstantVector vector) throws IllegalArgumentException {
            if (null == vector) {
                throw new IllegalArgumentException("vector can't be null!");
            }
            if (!vector.equals(this.vector)) {
                filterLabelList = Collections.emptyList();
                this.vector = vector;
            }
            return this;
        }

        public Builder without(String... filterLabels) throws IllegalArgumentException {
            resetFilterLabelList(filterLabels);
            siftWay = AggregationSiftWay.WITHOUT;
            return this;
        }

        public Builder by(String... filterLabels) throws IllegalArgumentException {
            resetFilterLabelList(filterLabels);
            siftWay = AggregationSiftWay.BY;
            return this;
        }

        public Builder param(String param) {
            this.param = param;
            return this;
        }

        public Builder param(int param) {
            this.param = String.valueOf(param);
            return this;
        }

        public Builder param(double param) {
            this.param = String.valueOf(param);
            return this;
        }

        public Builder param(Scalar param) {
            this.param = param.parse();
            return this;
        }

        private void resetFilterLabelList(String... filterLabels) throws IllegalArgumentException {
            if (null == filterLabels || filterLabels.length == 0) {
                throw new IllegalArgumentException("filterLabels can't be empty!");
            }
            if (filterLabelList.isEmpty()) {
                filterLabelList = new ArrayList<>();
            }
            filterLabelList.addAll(Arrays.asList(filterLabels));
        }


        public AggregationExpression build() {
            if (null == vector) {
                throw new IllegalArgumentException("vector can't be null!");
            }
            return null == param || param.isEmpty() ? new AggregationExpression(literal, vector, filterLabelList, siftWay) : new ParamlizedAggregationExpression(literal, param, vector, filterLabelList, siftWay);
        }
    }
}
