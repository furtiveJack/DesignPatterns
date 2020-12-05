package com.evilcorp.stp;

import javax.accessibility.AccessibleRelationSet;
import java.util.List;
import java.util.Objects;

public class ElapsedTimeCmd implements STPCommand {
    private final String name = "Elapsed";
    private final List<Integer> timers;

    public ElapsedTimeCmd(List<Integer> timers) {
        Objects.requireNonNull(timers);
        this.timers = List.copyOf(timers);
    }

    public List<Integer> getTimers() {
        return timers;
    }

    @Override
    public void accept(STPCommandVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElapsedTimeCmd that = (ElapsedTimeCmd) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
