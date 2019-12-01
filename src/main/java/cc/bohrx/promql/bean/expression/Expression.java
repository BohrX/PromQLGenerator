package cc.bohrx.promql.bean.expression;

import cc.bohrx.promql.exception.PromQLParseException;

public interface Expression {

    String parse() throws PromQLParseException;
}
