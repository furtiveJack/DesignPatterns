package fr.uge.poo.visitors.expr.ex2;

public class ToStringVisitor implements ExprVisitor<String> {
    @Override
    public String visit(Value value) {
        return Integer.toString(value.getValue());
    }

    @Override
    public String visit(BinOp binOp) {
        return new StringBuilder("(")
                .append(binOp.getLeft().accept(this))
                .append(' ')
                .append(binOp.getOpName())
                .append(' ')
                .append(binOp.getRight().accept(this))
                .append(')')
                .toString();
    }
}
