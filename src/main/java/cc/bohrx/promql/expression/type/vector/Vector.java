package cc.bohrx.promql.expression.type.vector;


import cc.bohrx.promql.constant.Constant;
import cc.bohrx.promql.constant.TimeUnit;
import cc.bohrx.promql.exception.PromQLParseException;
import cc.bohrx.promql.expression.Expression;
import cc.bohrx.promql.operator.Arithmetics;
import cc.bohrx.promql.operator.MatchOperators;
import cc.bohrx.promql.struct.Time;
import cc.bohrx.promql.struct.vector.filter.Filter;
import cc.bohrx.promql.struct.vector.filter.MatchFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cc.bohrx.promql.operator.MatchOperators.EQUAL;
import static cc.bohrx.promql.struct.Time.ZERO;


public abstract class Vector implements Expression {
    private final Filter metricNameFilter;

    private final List<Filter> filters;

    private final Time offset;

    protected Vector(Filter metricNameFilter, List<Filter> filters, Time offset) {
        this.metricNameFilter = metricNameFilter;
        this.filters = filters;
        this.offset = offset;
    }

    @Override
    public String toString() throws PromQLParseException {
        return parse();
    }

    @Override
    public String parse() throws PromQLParseException {
        return parse(true);
    }

    public abstract String parse(boolean putNameBeforeFilterIfEnabled) throws PromQLParseException;

    protected String vectorBody(boolean putNameBeforeFilterIfEnabled) {
        String filterStr = "";
        if (!filters.isEmpty()) {
            filterStr = String.join(Constant.COMMA, filters.stream().map(Filter::toString).collect(Collectors.toList()));
        }

        String vectorPrefix;
        if (EQUAL == metricNameFilter.operator() && putNameBeforeFilterIfEnabled) {
            vectorPrefix = metricNameFilter.condition().getValue();
            if (!"".equals(filterStr)) {
                vectorPrefix += (String.format(Constant.CONDITION_FILTER, filterStr));
            }
        } else {
            String allFilters = metricNameFilter.toString();
            if (!"".equals(filterStr)) {
                if (!"".equals(allFilters)) {
                    allFilters += Constant.COMMA;
                }
                allFilters += filterStr;
            }
            vectorPrefix = String.format(Constant.CONDITION_FILTER, allFilters);

        }
        return vectorPrefix;
    }

    protected String appendOffset() {
        String timeString = offset.toString();
        return "".equals(timeString) ? "" : Constant.SPACE + Constant.OFFSET + Constant.SPACE + timeString;
    }

    public static Builder allMetrics() {
        return new InstantVector.Builder(Filter.EMPTY_FILTER);
    }

    public static Builder metric(String name) {
        return new InstantVector.Builder(MatchOperators.metricNameFilter(MatchOperators.EQUAL, name));
    }

    public static Builder metricSift(String regex, MatchOperators matcher) {
        return new InstantVector.Builder(MatchOperators.metricNameFilter(matcher, regex));
    }

    public abstract static class Builder {
        protected final Filter metricNameFilter;

        protected final List<Filter> filters = new ArrayList<>();

        protected Time offset = ZERO;

        /**
         * actually, it's a expression buidler
         *
         * @param metricNameFilter
         */

        protected Builder(Filter metricNameFilter) {
            this.metricNameFilter = metricNameFilter;
        }

        protected Builder(Filter metricNameFilter, List<Filter> filters, Time offset) {
            this.metricNameFilter = metricNameFilter;
            if (null != filters && !filters.isEmpty()) {
                filters.addAll(filters);
            }
            this.offset = offset;
        }

        public Builder appendFilter(Filter filter) {
            filters.add(filter);
            return this;
        }

        public Builder appendFilters(Filter... filter) {
            filters.addAll(Arrays.asList(filter));
            return this;
        }

        public Builder removeFilter(Filter filter) {
            filters.remove(filter);
            return this;
        }

        public Builder removeFilters(Filter... filter) {
            filters.removeAll(Arrays.asList(filter));
            return this;
        }

        public Builder duration(long value, TimeUnit unit) {
            if (0 == value) {
                return this;
            }
            return new RangeVector.Builder(metricNameFilter, filters, new Time(value, unit), offset);
        }

        public Builder offset(long value, TimeUnit unit) {
            if (0 != value) {
                offset = new Time(value, unit);
            }
            return this;
        }

        public abstract Vector build() throws PromQLParseException;

        //todo !=~""的场景测试  是否与=~"。*"等效
        protected boolean isIllegal() {
            boolean isNotValid = true;
            if (!"".equals(metricNameFilter.condition())) {
                return false;
            }
            for (Filter filter : filters) {
                if (filter instanceof MatchFilter) {
                    isNotValid = ((MatchFilter) filter).getPattern().matcher("").matches();
                } else {
                    isNotValid = "".equals(filter.condition());
                }
            }
            return isNotValid;
        }

        public static void main(String[] args) {
            Filter filter1 = MatchOperators.EQUAL.apply("cpu-0", "10");
            Filter filter2 = MatchOperators.EQUAL.apply("cpu-1", ".*");

            Vector v1 = Vector.metric("cpu_idle").appendFilter(filter1).appendFilter(filter2).build();
            Vector v2 = Vector.metric("cpu_await").build();

            System.out.println(v1);
            System.out.println(Vector.metric("cpu_idle").offset(3, TimeUnit.MINUTE).duration(20, TimeUnit.SECOND).build());
            System.out.println(Vector.metric("cpu_idle").appendFilter(filter1).appendFilter(filter2).build().parse(false));
            System.out.println(Vector.metric("cpu_idle").build().parse(false));

            System.out.println(Vector.allMetrics().appendFilter(filter1).build());
            System.out.println(Vector.metricSift("bcac.*", MatchOperators.MATCH).appendFilter(filter1).build());
            System.out.println(Pattern.compile(".*").matcher("").matches());
            System.out.println(Pattern.compile(".+").matcher("").matches());

            System.out.println("--------------------------------------------------------");
            System.out.println(Arithmetics.DIVIDE.apply(1, 2).parse());
            System.out.println(Arithmetics.DIVIDE.apply(1, v1).parse());
            System.out.println(Arithmetics.DIVIDE.apply(v2, 2).parse());
            System.out.println(Arithmetics.DIVIDE.apply(v1, v2).parse());
        }
    }
}
