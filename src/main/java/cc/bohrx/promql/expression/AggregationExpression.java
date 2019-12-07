package cc.bohrx.promql.expression;

import cc.bohrx.promql.exception.PromQLParseException;
import cc.bohrx.promql.expression.type.Scalar;
import cc.bohrx.promql.operator.api.IOperator;
import cc.bohrx.promql.struct.StructTemplates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cc.bohrx.promql.constant.Constant.SPACE;

//假定聚合函数都可以参加表达式运算
public class AggregationExpression implements Expression {
    /**
     * <aggr-op>([parameter,] <vector expression>) [without|by (<label list>)]
     */

    protected final String literal;

    protected final Expression expression;

    protected final List<String> filterLabelList;

    protected final AggregationSiftWay siftWay;

    protected AggregationExpression(String literal, Expression expression, List<String> filterLabelList, AggregationSiftWay siftWay) {
        this.literal = literal;
        this.expression = expression;
        this.filterLabelList = filterLabelList;
        this.siftWay = siftWay;
    }


    @Override
    public String parse() throws PromQLParseException {
        String result = aggregationBody();
        //list不为空 siftWay不为空
        if (!filterLabelList.isEmpty()) {
            result += SPACE +
                    StructTemplates.printFunctionLikeStruct(
                            siftWay.getLiteral() + SPACE,
                            filterLabelList.toArray(new String[filterLabelList.size()])
                    );
        }
        return result;
    }

    protected String aggregationBody() {
        return StructTemplates.printFunctionLikeStruct(literal, expression);
    }

    @Override
    public String toString() {
        return parse();
    }


    public enum AggregationSiftWay implements IOperator {
        WITHOUT("without"), BY("by");


        private final String literal;

        AggregationSiftWay(String literal) {
            this.literal = literal;
        }


        @Override
        public String getLiteral() {
            return literal;
        }
    }

    public static class Builder {
        protected final String literal;

        protected Expression expression;

        protected List<String> filterLabelList = Collections.emptyList();

        protected AggregationSiftWay siftWay;

        public Builder(String funcName) {
            literal = funcName;
        }

        public Builder(Builder builder) {
            literal = builder.literal;
            expression = builder.expression;
            if (!builder.filterLabelList.isEmpty()) {
                filterLabelList = new ArrayList<>();
                filterLabelList.addAll(builder.filterLabelList);
            }
            siftWay = builder.siftWay;
        }

        public String getLiteral() {
            return literal;
        }

        public Builder apply(Expression expression) throws IllegalArgumentException {
            if (null == expression) {
                throw new IllegalArgumentException("expression can't be null!");
            }
            if (!expression.equals(this.expression)) {
                filterLabelList = Collections.emptyList();
                this.expression = expression;
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

        public ParamlizedAggregationExpression.Builder param(String param) {
            return new ParamlizedAggregationExpression.Builder(this, param);
        }

        public ParamlizedAggregationExpression.Builder param(int param) {
            return new ParamlizedAggregationExpression.Builder(this, String.valueOf(param));
        }

        public ParamlizedAggregationExpression.Builder param(double param) {
            return new ParamlizedAggregationExpression.Builder(this, String.valueOf(param));
        }

        public ParamlizedAggregationExpression.Builder param(Scalar param) {
            return new ParamlizedAggregationExpression.Builder(this, param.parse());
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
            if (null == expression) {
                throw new IllegalArgumentException("expression can't be null!");
            }
            return new AggregationExpression(literal, expression, filterLabelList, siftWay);
        }
    }
}
