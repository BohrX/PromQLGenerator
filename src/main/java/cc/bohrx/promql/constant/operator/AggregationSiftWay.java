package cc.bohrx.promql.constant.operator;

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
