package cc.bohrx.promql.constant;

public enum TimeUnit {
    SECOND("s"), MINUTE("m"), HOUR("h"), DAY("d"), WEEK("w"), YEAR("y");

    private String literal;

    TimeUnit(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}
