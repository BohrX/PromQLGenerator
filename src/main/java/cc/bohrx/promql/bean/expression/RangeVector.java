package cc.bohrx.promql.bean.expression;

import cc.bohrx.promql.bean.Time;
import cc.bohrx.promql.bean.expression.part.Filter;
import cc.bohrx.promql.constant.Constant;
import cc.bohrx.promql.exception.PromQLParseException;

import java.util.List;

public class RangeVector extends Vector {


    private final Time duration;

    RangeVector(Filter metricNameFilter, List<Filter> filters, Time duration, Time offset) {
        super(metricNameFilter, filters, offset);
        this.duration = duration;
    }

    private String appendDuration() {
        String timeString = appendTime(duration);
        return "".equals(timeString) ? "" : String.format(Constant.DURATION_SELECTOR, timeString);
    }

    @Override
    public String parse(boolean putNameBeforeFilterIfEnabled) throws PromQLParseException {
        return vectorPrefix(putNameBeforeFilterIfEnabled) + appendDuration() + appendOffset();
    }
}
