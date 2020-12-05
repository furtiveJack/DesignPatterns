package fr.uge.poo.visitors.expr.ex2;

public class EvalExprVisitor implements ExprVisitor<Integer> {
    public Integer visit(Value value) {
        return value.getValue();
    }

    @Override
    public Integer visit(BinOp binOp) {
        return binOp.getOperator().applyAsInt(
                binOp.getLeft().accept(this), binOp.getRight().accept(this)
        );
    }
}
