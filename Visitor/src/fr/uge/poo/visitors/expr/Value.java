package fr.uge.poo.visitors.expr;

public class Value implements Expr {
    private final int value;

    public Value(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int accept(ExprVisitor exprVisitor) {
        return exprVisitor.visit(this);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

}
