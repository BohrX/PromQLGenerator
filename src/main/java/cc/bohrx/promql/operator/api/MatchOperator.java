package cc.bohrx.promql.operator.api;

import cc.bohrx.promql.struct.vector.filter.Filter;

public interface MatchOperator extends IOperator {

    Filter apply(String label, String value);
}
