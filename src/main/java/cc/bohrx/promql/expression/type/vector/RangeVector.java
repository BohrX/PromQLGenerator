package cc.bohrx.promql.expression.type.vector;


import cc.bohrx.promql.constant.Constant;
import cc.bohrx.promql.constant.TimeUnit;
import cc.bohrx.promql.exception.PromQLParseException;
import cc.bohrx.promql.struct.Time;
import cc.bohrx.promql.struct.vector.filter.Filter;

import java.util.List;

public class RangeVector extends Vector {


    private final Time duration;

    RangeVector(Filter metricNameFilter, List<Filter> filters, Time duration, Time offset) {
        super(metricNameFilter, filters, offset);
        this.duration = duration;
    }

    private String appendDuration() {
        String timeString = duration.toString();
        return "".equals(timeString) ? "" : String.format(Constant.DURATION_SELECTOR, timeString);
    }

    @Override
    public String parse(boolean putNameBeforeFilterIfEnabled) throws PromQLParseException {
        return vectorBody(putNameBeforeFilterIfEnabled) + appendDuration() + appendOffset();
    }

    public static class Builder extends Vector.Builder {

        private Time duration;

        /**
         * actually, it's a expression buidler
         *
         * @param metricNameFilter
         */
        private Builder(Filter metricNameFilter) {
            super(metricNameFilter);
        }

        Builder(Filter metricNameFilter, List<Filter> filters, Time duration, Time offset) {
            super(metricNameFilter, filters, offset);
            this.duration = duration;
        }

        protected Builder(Filter metricNameFilter, List<Filter> filters, Time offset) {
            super(metricNameFilter, filters, offset);
        }

        @Override
        public Builder duration(long value, TimeUnit unit) {
            if (0 != value) {
                duration = new Time(value, unit);
            }
            return this;
        }


        @Override
        public RangeVector build() throws PromQLParseException {
            if (isIllegal()) {
                throw new PromQLParseException("Vector is illegal, please check your filter. Vector selectors must either specify a name or at least one label matcher that does not match the empty string.");
            }
            return new RangeVector(metricNameFilter, filters, duration, offset);
        }
    }
}
