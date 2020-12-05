package fr.uge.poo.visitors.expr;

public interface ExprVisitor {
    int visit(Value value);

    int visit(BinOp binOp);
}
