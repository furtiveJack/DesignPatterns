package fr.uge.poo.visitors.expr.ex3;

public interface ExprVisitor<T> {
    T visit(Value value);

    T visit(BinOp binOp);
}
