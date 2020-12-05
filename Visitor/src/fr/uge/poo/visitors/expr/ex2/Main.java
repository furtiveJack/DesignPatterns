package fr.uge.poo.visitors.expr.ex2;

import java.util.Iterator;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        EvalExprVisitor evalExprVisitor = new EvalExprVisitor();
        ToStringVisitor stringVisitor = new ToStringVisitor();
        Iterator<String> it = Pattern.compile(" ").splitAsStream("+ * 4 + 1 1 + 2 3").iterator();
        Expr expr = Expr.parseExpr(it);

        System.out.println(expr.accept(stringVisitor));
        System.out.println(expr.accept(evalExprVisitor));
    }
}
