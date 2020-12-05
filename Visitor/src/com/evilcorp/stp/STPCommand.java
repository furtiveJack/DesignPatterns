package com.evilcorp.stp;

public interface STPCommand {
    void accept(STPCommandVisitor visitor);

    String getName();
}
