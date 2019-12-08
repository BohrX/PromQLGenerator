package cc.bohrx.promql.operator;


import cc.bohrx.promql.expression.BinaryOperation;
import cc.bohrx.promql.expression.Expression;
import cc.bohrx.promql.expression.type.Scalar;
import cc.bohrx.promql.expression.type.vector.Vector;
import cc.bohrx.promql.operator.api.Arithmetic;
import cc.bohrx.promql.struct.modifier.OperatorWithVectorMatch;
import cc.bohrx.promql.struct.modifier.VectorMatchs;
import cc.bohrx.promql.struct.vector.filter.Filter;

public enum Arithmetics implements Arithmetic {
    PLUS(3, "+"), MINUS(3, "-"), MULTI(2, "*"), DIVIDE(2, "/"), MODU(2, "%"), POWER(1, "^");
    private final int priority;

    private String literal;

    Arithmetics(int priority, String literal) {
        this.priority = priority;
        this.literal = literal;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    public BinaryOperation<Arithmetic> apply(double arg1, double arg2) {
        return new BinaryOperation<>(Scalar.of(arg1), this, Scalar.of(arg2));
    }

    public BinaryOperation<Arithmetic> apply(double arg1, Expression arg2) {
        return new BinaryOperation<>(Scalar.of(arg1), this, arg2);
    }

    public BinaryOperation<Arithmetic> apply(Expression arg1, double arg2) {
        return new BinaryOperation<>(arg1, this, Scalar.of(arg2));
    }

    @Override
    public BinaryOperation<Arithmetic> apply(Expression arg1, Expression arg2) {
        return new BinaryOperation<>(arg1, this, arg2);
    }

    public OperatorWithVectorMatch<Arithmetic> vectorMatch(VectorMatchs vectorMatch) {
        return new OperatorWithVectorMatch<>(this, vectorMatch.get());
    }

    public OperatorWithVectorMatch<Arithmetic> vectorMatchMulti(VectorMatchs vectorMatch, boolean groupLeft) {
        return new OperatorWithVectorMatch<>(this, groupLeft ? vectorMatch.groupLeft() : vectorMatch.groupRight());
    }

    public static void main(String[] args) {
        Filter filter1 = MatchOperators.EQUAL.apply("cpu-0", "10");
        Filter filter2 = MatchOperators.EQUAL.apply("cpu-1", ".*");
        Vector v1 = Vector.metric("cpu_idle").appendFilter(filter1).appendFilter(filter2).build();
        Vector v2 = Vector.metric("cpu_await").build();
        System.out.println(MINUS.apply(1.23, MINUS.vectorMatchMulti(VectorMatchs.IGNORING, true).setMatchLabels("abd", "233").apply(v1, v2)));
    }
}
