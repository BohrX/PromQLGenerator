package cc.bohrx.promql.bean.expression;

import cc.bohrx.promql.bean.Time;
import cc.bohrx.promql.bean.expression.part.Filter;
import cc.bohrx.promql.bean.expression.part.MatchFilter;
import cc.bohrx.promql.constant.Constant;
import cc.bohrx.promql.constant.TimeUnit;
import cc.bohrx.promql.constant.operator.Arithmetic;
import cc.bohrx.promql.constant.operator.Selector;
import cc.bohrx.promql.exception.PromQLParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cc.bohrx.promql.bean.Time.ZERO;

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

    protected String vectorPrefix(boolean putNameBeforeFilterIfEnabled) {
        String filterStr = "";
        if (!filters.isEmpty()) {
            filterStr = String.join(Constant.FILTER_SPLIT, filters.stream().map(Filter::toString).collect(Collectors.toList()));
        }

        String vectorPrefix;
        if (Selector.EQUAL == metricNameFilter.operator() && putNameBeforeFilterIfEnabled) {
            vectorPrefix = metricNameFilter.condition();
            if (!"".equals(filterStr)) {
                vectorPrefix += (String.format(Constant.CONDITION_FILTER, filterStr));
            }
        } else {
            String allFilters = metricNameFilter.toString();
            if (!"".equals(filterStr)) {
                if (!"".equals(allFilters)) {
                    allFilters += Constant.FILTER_SPLIT;
                }
                allFilters += filterStr;
            }
            vectorPrefix = String.format(Constant.CONDITION_FILTER, allFilters);

        }
        return vectorPrefix;
    }


    protected String appendOffset() {
        String timeString = appendTime(offset);
        return "".equals(timeString) ? "" : Constant.OFFSET + timeString;
    }

    protected String appendTime(Time time) {
        if (time != ZERO && 0 != time.getValue()) {
            return time.toString();
        }
        return "";
    }

    public static class Builder {
        private final Filter metricNameFilter;

        private final List<Filter> filters = new ArrayList<>();

        private Time duration = ZERO;

        private Time offset = ZERO;

        /**
         * actually, it's a vector buidler
         *
         * @param metricNameFilter
         */

        private Builder(Filter metricNameFilter) {
            this.metricNameFilter = metricNameFilter;
        }

        public static Builder allMetrics() {
            return new Builder(Filter.EMPTY_FILTER);
        }

        public static Builder metric(String name) {
            return new Builder(Filter.Builder.metricNameFilterBuilder().equal(name));
        }

        public static Builder metricFilt(String regex, Selector selector) {
            return new Builder(Filter.Builder.metricNameFilterBuilder().operate(selector, regex));
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
            if (0 != value) {
                duration = new Time(value, unit);
            }
            return this;
        }

        public Builder offset(long value, TimeUnit unit) {
            if (0 != value) {
                offset = new Time(value, unit);
            }
            return this;
        }

        public Vector build() throws PromQLParseException {
            if (isIllegal()) {
                throw new PromQLParseException("Vector is illegal, please check your filter. Vector selectors must either specify a name or at least one label matcher that does not match the empty string.");
            }
            return (ZERO == duration || duration.getValue() == 0) ? new InstantVector(metricNameFilter, filters, offset)
                    : new RangeVector(metricNameFilter, filters, duration, offset);
        }

        public InstantVector buildInstant() throws PromQLParseException {
            if (isIllegal()) {
                throw new PromQLParseException("Vector is illegal, please check your filter. Vector selectors must either specify a name or at least one label matcher that does not match the empty string.");
            }
            return new InstantVector(metricNameFilter, filters, offset);
        }

        //todo !=~""的场景测试  是否与=~"。*"等效
        private boolean isIllegal() {
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
            Filter filter1 = Filter.Builder.label("cpu-0").equal("10");
            Filter filter2 = Filter.Builder.label("cpu-1").match(".*");

            Vector v1 = Builder.metric("cpu_idle").appendFilter(filter1).appendFilter(filter2).build();
            Vector v2 = Builder.metric("cpu_await").build();

            System.out.println(v1);
            System.out.println(Builder.metric("cpu_idle").offset(3, TimeUnit.MINUTE).duration(20, TimeUnit.SECOND).build());
            System.out.println(Builder.metric("cpu_idle").appendFilter(filter1).appendFilter(filter2).build().parse(false));
            System.out.println(Builder.metric("cpu_idle").build().parse(false));

            System.out.println(Builder.allMetrics().appendFilter(filter1).build());
            System.out.println(Builder.metricFilt("bcac.*", Selector.MATCH).appendFilter(filter1).build());
            System.out.println(Pattern.compile(".*").matcher("").matches());
            System.out.println(Pattern.compile(".+").matcher("").matches());

            System.out.println("--------------------------------------------------------");
            System.out.println(Arithmetic.DIVIDE.packExpr(1, 2).parse());
            System.out.println(Arithmetic.DIVIDE.packExpr(1, v1).parse());
            System.out.println(Arithmetic.DIVIDE.packExpr(v2, 2).parse());
            System.out.println(Arithmetic.DIVIDE.packExpr(v1, v2).parse());
        }
    }
}
