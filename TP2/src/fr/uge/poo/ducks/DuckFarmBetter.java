package fr.uge.poo.ducks;

import java.util.ArrayList;
import java.util.ServiceLoader;

public class DuckFarmBetter {
    public static void main(String[] args) {
        ServiceLoader<DuckFactory> loader = ServiceLoader.load(fr.uge.poo.ducks.DuckFactory.class);

        ArrayList<Duck> ducks = new ArrayList<>(3);
        for (var duck : loader) {
            ducks.add(duck.withName("Riri"));
            ducks.add(duck.withName("Fifi"));
            ducks.add(duck.withName("Lulu"));
        }
        ducks.forEach(duck -> System.out.println(duck.quack()));
    }
}
