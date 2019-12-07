package cc.bohrx.promql.expression;

import cc.bohrx.promql.exception.PromQLParseException;

public interface Expression {

    String parse() throws PromQLParseException;
}
