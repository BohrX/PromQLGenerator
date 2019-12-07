package cc.bohrx.promql.constant;

public enum TimeUnit {
    SECOND("s"), MINUTE("m"), HOUR("h"), DAY("d"), WEEK("w"), YEAR("y");

    private String value;

    TimeUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
