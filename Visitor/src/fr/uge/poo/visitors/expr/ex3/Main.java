package fr.uge.poo.visitors.expr.ex3;

import java.util.Iterator;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        ExprVisitor<Integer> evalVisitor = new ExprVisitor<>();
        evalVisitor
                .when(Value.class, Value::getValue)
                .when(BinOp.class, binOp ->
                        binOp.getOperator()
                                .applyAsInt(
                                        binOp.getLeft().accept(evalVisitor),
                                        binOp.getRight().accept(evalVisitor)
                                )
                );
        ExprVisitor<StringBuilder> stringVisitor = new ExprVisitor<>();
        stringVisitor
                .when(Value.class, value -> stringVisitor.getBuilder().append(Integer.toString(value.getValue())))
                .when(BinOp.class, binOp -> {
                    stringVisitor.getBuilder().append('(');
                    binOp.getLeft().accept(stringVisitor);
                    stringVisitor.getBuilder().append(' ')
                            .append(binOp.getOpName())
                            .append(' ');
                    binOp.getRight().accept(stringVisitor);
                    return stringVisitor.getBuilder().append(')');
                });

        Iterator<String> it = Pattern.compile(" ").splitAsStream("+ * 4 + 1 1 + 2 3").iterator();
        Expr expr = Expr.parseExpr(it);

        System.out.println(stringVisitor.visit(expr));
        System.out.println(evalVisitor.visit(expr));
    }
}
