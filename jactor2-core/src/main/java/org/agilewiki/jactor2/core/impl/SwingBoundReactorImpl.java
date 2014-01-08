package org.agilewiki.jactor2.core.impl;

import org.agilewiki.jactor2.core.plant.Scheduler;
import org.agilewiki.jactor2.core.reactors.SwingBoundReactor;
import org.agilewiki.jactor2.core.util.Recovery;

import javax.swing.*;

public class SwingBoundReactorImpl extends ThreadBoundReactorImpl {

    public SwingBoundReactorImpl(final NonBlockingReactorImpl _parentReactorImpl,
                                 final int _initialOutboxSize, final int _initialLocalQueueSize,
                                 final Recovery _recovery, final Scheduler _scheduler) throws Exception {
        super(_parentReactorImpl, _initialOutboxSize, _initialLocalQueueSize, _recovery, _scheduler, null);
    }

    @Override
    public SwingBoundReactor asReactor() {
        return (SwingBoundReactor) getReactor();
    }

    @Override
    protected void afterAdd() {
        SwingUtilities.invokeLater(this);
    }
}