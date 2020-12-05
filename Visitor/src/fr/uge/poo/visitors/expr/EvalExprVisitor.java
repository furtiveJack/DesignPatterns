package fr.uge.poo.visitors.expr;

public class EvalExprVisitor implements ExprVisitor {
    public int visit(Value value) {
        return value.getValue();
    }

    @Override
    public int visit(BinOp binOp) {
        return binOp.getOperator().applyAsInt(
                binOp.getLeft().accept(this), binOp.getRight().accept(this)
        );
    }
}
