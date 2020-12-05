package fr.uge.poo.visitors.expr.ex3;

public class ToStringVisitor implements ExprVisitor<StringBuilder> {
    private final StringBuilder builder = new StringBuilder();

    @Override
    public StringBuilder visit(Value value) {
        return builder.append(Integer.toString(value.getValue()));
    }

    @Override
    public StringBuilder visit(BinOp binOp) {
        return builder.append("(")
                .append(binOp.getLeft())
                .append(' ')
                .append(binOp.getOpName())
                .append(' ')
                .append(binOp.getRight())
                .append(')');
    }
}
