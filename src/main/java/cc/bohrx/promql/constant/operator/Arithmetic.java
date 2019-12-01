package cc.bohrx.promql.constant.operator;

public enum Arithmetic implements BinaryOperator {
    PLUS("+"), MINUS("-"), MULTI("*"), DIVIDE("/"), MODU("%"), POWER("^");

    private String literal;

    Arithmetic(String literal) {
        this.literal = literal;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

}
