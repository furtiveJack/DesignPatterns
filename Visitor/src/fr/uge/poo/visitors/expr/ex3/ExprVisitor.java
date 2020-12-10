package fr.uge.poo.visitors.expr.ex3;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

public class ExprVisitor<T> {
    private HashMap<Class<? extends Expr>, Function<Expr,T>> map = new HashMap<>();
    private final StringBuilder builder = new StringBuilder();

    public <E extends Expr>  ExprVisitor<T> when(Class<E> clazz,Function<E,T> function){
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(function);
        if (map.containsKey(clazz)){
            throw new IllegalStateException(clazz.getName()+" is already defined");
        }
        map.put(clazz,(Function<Expr,T>) function);
        return this;
    }

    public T visit(Expr expr){
        var function = map.get(expr.getClass());
        if (function==null){
            throw new IllegalStateException(expr.getClass().getName()+" is not defined");
        }
        return function.apply(expr);
    }

    public StringBuilder getBuilder() {
        return builder;
    }

}