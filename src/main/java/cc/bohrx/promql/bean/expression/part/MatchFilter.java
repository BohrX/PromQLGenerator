package cc.bohrx.promql.bean.expression.part;

import cc.bohrx.promql.constant.operator.Selector;

import java.util.regex.Pattern;

import static cc.bohrx.promql.constant.operator.Selector.MATCH;
import static cc.bohrx.promql.constant.operator.Selector.NOT_MATCH;

public class MatchFilter extends Filter {

    private final Pattern pattern;

    public MatchFilter(String label, Pattern pattern) {
        this(label, pattern, false);
    }

    public MatchFilter(String label, Pattern pattern, boolean negate) {
        super(label, negate ? NOT_MATCH : MATCH);
        this.pattern = pattern;
    }

    private MatchFilter(String label, Selector negateSelector, Pattern pattern) {
        super(label, negateSelector);
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String condition() {
        return pattern.toString();
    }

    @Override
    public MatchFilter negate() {
        return new MatchFilter(label, selector.negate(), pattern);
    }
}
