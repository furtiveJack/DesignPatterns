package fr.uge.poo.visitors.stp;

import com.evilcorp.stp.STPParser;

import java.util.Scanner;

public class Triviale {

    public static void main(String[] args) {
        var scan = new Scanner(System.in);
        var visitor = new Context();
        while(scan.hasNextLine()) {
            var line = scan.nextLine();
            var answer = STPParser.parse(line);
            if (answer.isEmpty()) {
                System.out.println("Pas compris");
                continue;
            }
            var cmd = answer.get();
            cmd.accept(visitor);
        }
    }
}


/*
- Quel principe SOLID n'est pas implémenté par la librairie stp ?

OpenClose Principle : l'utilisateur ne peut  pas ajouter de code
-> Aucun moyen d'agir différemment selon le type spécifique de la commande

- Comment pourrait-on résoudre le problème en utilisant le polymorphisme ?
    Est-ce que c'est envisageable dans cette situation ?

On pourrait ajouter une méthode à l'interface STPCommand, implémentée par chaque classe de l'interface et qui
fournirait le traitement à effectuer pour chaque option.
Cela n'est pas forcément adapté, car cela donne beaucoup de responsabilités aux classes implémentant STPCommand.

- Quel patron permet d'ajouter des nouvelles opérations aux classes implémentant l'interface
   STPCommand sans modifier ces classes ?

C'est le pattern Visitor.
 */