package org.agilewiki.jactor2.core.requests;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;

/**
 * Test code.
 */
public class Blade1 extends NonBlockingBladeBase {

    public Blade1(final NonBlockingReactor mbox) throws Exception {
        super(mbox);
    }

    public SyncRequest<String> hiSReq() {
        return new SyncBladeRequest<String>() {
            @Override
            public String processSyncRequest() throws Exception {
                return "Hello world!";
            }
        };
    }
}
