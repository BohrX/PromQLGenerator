package cc.bohrx.promql.struct;


import cc.bohrx.promql.constant.TimeUnit;

import java.util.Objects;

/**
 * 能否使用小数 或混合单位使用(如：3d13h) 需要检查
 */
public class Time {

    public static final Time ZERO = new Time(0, TimeUnit.SECOND);

    private final long value;

    private final TimeUnit unit;

    public Time(long value, TimeUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public long getValue() {
        return value;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return value == 0 ? "" : value + unit.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Time time = (Time) o;
        return value == time.value &&
                unit == time.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }
}
