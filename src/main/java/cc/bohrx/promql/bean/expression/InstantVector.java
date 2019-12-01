package cc.bohrx.promql.bean.expression;

import cc.bohrx.promql.bean.Time;
import cc.bohrx.promql.bean.expression.part.Filter;
import cc.bohrx.promql.exception.PromQLParseException;

import java.util.List;

public class InstantVector extends Vector {

    InstantVector(Filter metricNameFilter, List<Filter> filters, Time offset) {
        super(metricNameFilter, filters, offset);
    }

    @Override
    public String parse(boolean putNameBeforeFilterIfEnabled) throws PromQLParseException {
        return vectorPrefix(putNameBeforeFilterIfEnabled) + appendOffset();
    }
}
