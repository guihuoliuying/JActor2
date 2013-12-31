package org.agilewiki.jactor2.core;

import org.agilewiki.jactor2.core.plant.BasicPlant;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;

public class HungClose {
    static public void main(final String[] _args) throws Exception {
        final BasicPlant plant = new BasicPlant();
        try {
            NonBlockingReactor reactor = new NonBlockingReactor();
            Hanger hanger = new Hanger(reactor);
            hanger.looperSReq().signal();
        } finally {
            System.out.println("closing");
            plant.close();
            System.out.println("closed");
        }
    }
}