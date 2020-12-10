package fr.uge.poo.visitors.expr.ex2;

public class ToStringVisitor implements ExprVisitor<StringBuilder> {
    private final StringBuilder builder = new StringBuilder();
    @Override
    public StringBuilder visit(Value value) {
        return builder.append(value.getValue());
    }

    @Override
    public StringBuilder visit(BinOp binOp) {
        builder.append("(");
        binOp.getLeft().accept(this);
        builder.append(' ')
                .append(binOp.getOpName())
                .append(' ');
        binOp.getRight().accept(this);
        builder.append(')');
        return builder;
    }
}
