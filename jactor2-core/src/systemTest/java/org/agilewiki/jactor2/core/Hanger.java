package org.agilewiki.jactor2.core;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.SyncRequest;

class Hanger extends NonBlockingBladeBase {
    Hanger() throws Exception {
        super(new NonBlockingReactor());
    }

    SyncRequest<Void> looperSReq() {
        return new SyncBladeRequest<Void>() {
            @Override
            public Void processSyncRequest() throws Exception {
                while (true) {}
            }
        };
    }

    SyncRequest<Void> sleeperSReq() {
        return new SyncBladeRequest<Void>() {
            @Override
            public Void processSyncRequest() throws Exception {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException ie) {
                    throw ie;
                }
                return null;
            }
        };
    }
}
