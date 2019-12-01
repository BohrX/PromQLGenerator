package cc.bohrx.promql.constant.operator;

public enum SetOpr implements BinaryOperator {
    INTERSECT("add"), UNION("or"), COMPLEMENT("unless");
    private String literal;

    SetOpr(String literal) {
        this.literal = literal;
    }

    @Override
    public String getLiteral() {
        return literal;
    }
}
