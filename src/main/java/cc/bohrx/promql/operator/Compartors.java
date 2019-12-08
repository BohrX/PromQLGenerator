package cc.bohrx.promql.operator;


import cc.bohrx.promql.constant.Constant;
import cc.bohrx.promql.expression.BinaryOperation;
import cc.bohrx.promql.expression.Expression;
import cc.bohrx.promql.expression.type.Scalar;
import cc.bohrx.promql.expression.type.vector.Vector;
import cc.bohrx.promql.operator.api.Compartor;
import cc.bohrx.promql.struct.modifier.OperatorWithVectorMatch;
import cc.bohrx.promql.struct.modifier.VectorMatchs;
import cc.bohrx.promql.struct.vector.filter.Filter;


public enum Compartors implements Compartor {
    EQUAL("=="), NOT_EQUAL("!="), GREATER(">"), LESS("<"), NOT_LESS(">="), NOT_GREATER("<=");
    private final int priority = 4;

    private String literal;

    private CompartorWithBool withBool;

    Compartors(String literal) {
        this.literal = literal;
        withBool = new CompartorWithBool(this);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    public CompartorWithBool withBool() {
        return withBool;
    }

    public OperatorWithVectorMatch<Compartor> vectorMatch(VectorMatchs vectorMatch) {
        return new OperatorWithVectorMatch<>(this, vectorMatch.get());
    }

    public OperatorWithVectorMatch<Compartor> vectorMatchMulti(VectorMatchs vectorMatch, boolean groupLeft) {
        return new OperatorWithVectorMatch<>(this, groupLeft ? vectorMatch.groupLeft() : vectorMatch.groupRight());
    }

    public BinaryOperation<Compartor> apply(double arg1, double arg2) {
        return withBool().apply(arg1, arg2);
    }

    public BinaryOperation<Compartor> apply(double arg1, Expression arg2) {
        return new BinaryOperation<>(Scalar.of(arg1), this, arg2);
    }

    public BinaryOperation<Compartor> apply(Expression arg1, double arg2) {
        return new BinaryOperation<>(arg1, this, Scalar.of(arg2));
    }


    @Override
    public BinaryOperation<Compartor> apply(Expression arg1, Expression arg2) {
        return new BinaryOperation<>(arg1, this, arg2);
    }

    public static class CompartorWithBool implements Compartor {

        private Compartor compartor;

        CompartorWithBool(Compartors compartor) {
            this.compartor = compartor;
        }

        public BinaryOperation<Compartor> apply(double arg1, double arg2) {
            return new BinaryOperation<>(Scalar.of(arg1), this, Scalar.of(arg1));
        }

        public BinaryOperation<Compartor> apply(double arg1, Expression arg2) {
            return new BinaryOperation<>(Scalar.of(arg1), this, arg2);
        }

        public BinaryOperation<Compartor> apply(Expression arg1, double arg2) {
            return new BinaryOperation<>(arg1, this, Scalar.of(arg2));
        }

        @Override
        public BinaryOperation<Compartor> apply(Expression arg1, Expression arg2) {
            return new BinaryOperation<>(arg1, this, arg2);
        }

        @Override
        public String getLiteral() {
            return compartor.getLiteral() + Constant.SPACE + Constant.BOOL;
        }

        @Override
        public int getPriority() {
            return compartor.getPriority();
        }

        public OperatorWithVectorMatch<Compartor> vectorMatch(VectorMatchs vectorMatch) {
            return new OperatorWithVectorMatch<>(this, vectorMatch.get());
        }

        public OperatorWithVectorMatch<Compartor> vectorMatchMulti(VectorMatchs vectorMatch, boolean groupLeft) {
            return new OperatorWithVectorMatch<>(this, groupLeft ? vectorMatch.groupLeft() : vectorMatch.groupRight());
        }

    }

    public static void main(String[] args) {
        Filter filter1 = MatchOperators.EQUAL.apply("cpu-0", "10");
        Filter filter2 = MatchOperators.EQUAL.apply("cpu-1", ".*");
        Vector v1 = Vector.metric("cpu_idle").appendFilter(filter1).appendFilter(filter2).build();
        Vector v2 = Vector.metric("cpu_await").build();
        System.out.println(GREATER.withBool().vectorMatchMulti(VectorMatchs.IGNORING, true).setMatchLabels("abd", "233").apply(v1, v2));
    }
}
