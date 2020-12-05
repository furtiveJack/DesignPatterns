package fr.uge.poo.visitors.stp;

import com.evilcorp.stp.*;
import fr.uge.poo.cmdline.ex6.CmdLineParser;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Context implements STPCommandVisitor {
    interface CmdStatObserver {
        default void onVisitedHello(HelloCmd helloCmd) {}

        default void onVisitedStart(StartTimerCmd startCmd) {}

        default void onVisitedStop(StopTimerCmd stopCmd) {}

        default void onVisitedStopWithTime(StopTimerCmd stopCmd, long elapsed) {}

        default void onVisitedElapsed(ElapsedTimeCmd elapsedCmd) {}

        String getReport();
    }

    static class CmdCountObserver implements CmdStatObserver {
        HashMap<STPCommand, Integer> commandsCount = new HashMap<>();

        @Override
        public void onVisitedHello(HelloCmd helloCmd) {
            commandsCount.compute(helloCmd, (cmd, count) -> (count == null) ? 1 : count+1);
        }

        @Override
        public void onVisitedStart(StartTimerCmd startCmd) {
            commandsCount.compute(startCmd, (cmd, count) -> (count == null) ? 1 : count+1);
        }

        @Override
        public void onVisitedStop(StopTimerCmd stopCmd) {
            commandsCount.compute(stopCmd, (cmd, count) -> (count == null) ? 1 : count+1);
        }

        @Override
        public void onVisitedElapsed(ElapsedTimeCmd elapsedCmd) {
            commandsCount.compute(elapsedCmd, (cmd, count) -> (count == null) ? 1 : count+1);
        }

        @Override
        public String getReport() {
            var builder = new StringBuilder();
            commandsCount.forEach((cmd, count) -> builder.append(cmd.getName())
                    .append(" called ").append(count).append(" times.\n"));
            return builder.toString();
        }
    }

    static class CmdMeanTimeObserver implements CmdStatObserver {
        private long meanTime = 0;
        private int nbCalls = 0;

        @Override
        public void onVisitedStopWithTime(StopTimerCmd cmd, long elapsed) {
            meanTime += elapsed;
            nbCalls++;
        }

        @Override
        public String getReport() {
            if (nbCalls == 0) {
                return "Timers mean execution time : 0ms (none was started).\n";
            }
            return "Timers mean execution time : " + meanTime/nbCalls + "ms.\n";
        }
    }

    private final HashMap<Integer,Long> timers = new HashMap<>();
    private final List<CmdStatObserver> observers = new ArrayList<>();

    public void registerObserver(CmdStatObserver observer) {
        Objects.requireNonNull(observer);
        observers.add(observer);
    }

    private void notifyObservers(Consumer<CmdStatObserver> consumer) {
        for (var o : observers) {
            consumer.accept(o);
        }
    }

    public void printReport() {
        var report = new StringBuilder();
        for (var obs : observers) {
            report.append(obs.getReport());
        }
        System.out.println(report.toString());
    }

    @Override
    public void visit(HelloCmd helloCmd) {
        notifyObservers(obs -> obs.onVisitedHello(helloCmd));
        System.out.println("Hello the current date is "+ LocalDateTime.now());
    }

    @Override
    public void visit(StartTimerCmd startTimerCmd) {
        notifyObservers(obs -> obs.onVisitedStart(startTimerCmd));
        var timerId = startTimerCmd.getTimerId();
        if (timers.get(timerId)!=null){
            System.out.println("Timer "+timerId+" was already started");
            return;
        }
        var currentTime =  System.currentTimeMillis();
        timers.put(timerId,currentTime);
        System.out.println("Timer "+timerId+" started");
    }

    @Override
    public void visit(StopTimerCmd stopTimerCmd) {
        notifyObservers(obs -> obs.onVisitedStop(stopTimerCmd));
        var timerId = stopTimerCmd.getTimerId();
        var startTime = timers.get(timerId);
        if (startTime==null){
            System.out.println("Timer "+timerId+" was never started");
            return;
        }
        var currentTime =  System.currentTimeMillis();
        long elapsed = currentTime - startTime;
        notifyObservers(obs -> obs.onVisitedStopWithTime(stopTimerCmd, elapsed));
        System.out.println("Timer "+timerId+" was stopped after running for "+ elapsed +"ms");
        timers.put(timerId,null);
    }

    @Override
    public void visit(ElapsedTimeCmd elapsedTimeCmd) {
        notifyObservers(obs -> obs.onVisitedElapsed(elapsedTimeCmd));
        var currentTime =  System.currentTimeMillis();
        for(var timerId : elapsedTimeCmd.getTimers()){
            var startTime = timers.get(timerId);
            if (startTime==null){
                System.out.println("Unknown timer "+timerId);
                return;
            }
            System.out.println("Elapsed time on timerId "+timerId+" : "+(currentTime-startTime)+"ms");
        }
    }
}
