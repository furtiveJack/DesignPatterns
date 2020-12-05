package fr.uge.poo.visitors.expr.ex2;

import java.util.StringJoiner;

public class ToStringVisitor implements ExprVisitor<StringBuilder> {

    @Override
    public StringBuilder visit(Value value) {
        System.out.println("getting value : " + value.getValue());
        return new StringBuilder().append(value.getValue());
    }

    @Override
    public StringBuilder visit(BinOp binOp) {
        return new StringBuilder("(")
                .append(binOp.getLeft().accept(this))
                .append(' ')
                .append(binOp.getOpName())
                .append(' ')
                .append(binOp.getRight().accept(this))
                .append(')');
    }
}
