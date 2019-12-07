package cc.bohrx.promql.expression.type.vector;

import cc.bohrx.promql.exception.PromQLParseException;
import cc.bohrx.promql.struct.Time;
import cc.bohrx.promql.struct.vector.filter.Filter;

import java.util.List;


public class InstantVector extends Vector {

    public InstantVector(Filter metricNameFilter, List<Filter> filters, Time offset) {
        super(metricNameFilter, filters, offset);
    }

    @Override
    public String parse(boolean putNameBeforeFilterIfEnabled) throws PromQLParseException {
        return vectorBody(putNameBeforeFilterIfEnabled) + appendOffset();
    }


    public static class Builder extends Vector.Builder {

        public Builder(Filter metricNameFilter) {
            super(metricNameFilter);
        }

        public Builder(Filter metricNameFilter, List<Filter> filters, Time offset) {
            super(metricNameFilter, filters, offset);
        }

        @Override
        public InstantVector build() throws PromQLParseException {
            if (isIllegal()) {
                throw new PromQLParseException("Vector is illegal, please check your filter. Vector selectors must either specify a name or at least one label matcher that does not match the empty string.");
            }
            return new InstantVector(metricNameFilter, filters, offset);
        }
    }
}
