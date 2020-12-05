package com.evilcorp.stp;

public class HelloCmd implements STPCommand {
    private final String name = "Hello";
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

        HelloCmd helloCmd = (HelloCmd) o;

        return name.equals(helloCmd.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
