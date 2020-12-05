package fr.uge.poo.visitors.expr.ex2;

public interface ExprVisitor<T> {
    T visit(Value value);

    T visit(BinOp binOp);
}
