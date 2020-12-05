package com.evilcorp.stp;

public class StopTimerCmd implements STPCommand {
    private final String name = "StopTimer";
    private int  timerId;

    public StopTimerCmd(int timerId){
        this.timerId=timerId;
    }

    public int getTimerId() {
        return timerId;
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

        StopTimerCmd that = (StopTimerCmd) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
