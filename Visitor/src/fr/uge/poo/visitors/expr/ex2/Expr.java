package fr.uge.poo.visitors.expr.ex2;

import java.util.Iterator;

public interface Expr {
    <T> T accept(ExprVisitor<T> exprVisitor);

    static Expr parseExpr(Iterator<String> it) {
        if (!it.hasNext()) {
            throw new IllegalArgumentException("no more tokens");
        }
        String token = it.next();
        return switch (token) {
            case "+" -> new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a + b);
            case "-" -> new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a - b);
            case "*" -> new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a * b);
            case "/" -> new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a / b);
            default -> new Value(Integer.parseInt(token));
        };
    }

}
