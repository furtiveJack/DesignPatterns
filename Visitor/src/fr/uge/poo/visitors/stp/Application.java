package fr.uge.poo.visitors.stp;
import com.evilcorp.stp.*;
import fr.uge.poo.visitors.stp.Context.CmdCountObserver;

import java.util.Optional;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        var visitor = new Context();
        visitor.registerObserver(new CmdCountObserver());
        var scan = new Scanner(System.in);
        while(scan.hasNextLine()){
            var line = scan.nextLine();
            Optional<STPCommand> answer = STPParser.parse(line);
            if (line.equals("quit")) {
                break;
            }
            if (answer.isEmpty()){
                System.out.println("Unrecognized command");
                continue;
            }
            STPCommand cmd = answer.get();
            cmd.accept(visitor);
        }
        visitor.printReport();
    }
}
