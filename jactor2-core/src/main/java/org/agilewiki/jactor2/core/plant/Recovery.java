package org.agilewiki.jactor2.core.plant;

import org.agilewiki.jactor2.core.impl.BlockingReactorImpl;
import org.agilewiki.jactor2.core.impl.ReactorImpl;
import org.agilewiki.jactor2.core.impl.RequestImpl;

/**
 * Base class for managing failure detection and recovery.
 * The default Recovery is created by PlantConfiguration.
 */
public class Recovery {
    /**
     * Controls how frequently reactors are polled for message timeouts.
     *
     * @return The number of milliseconds between polls. Default = 500.
     */
    public long getReactorPollMillis() {
        return 500;
    }

    /**
     * Determines how long a message can be processed before timing out.
     * Default for Blocking reactors: 5 minutes.
     * Default for all other reactors: 1 second.
     *
     * @param _reactorImpl  The reactor which may have a timed-out message.
     * @return Number of milliseconds.
     */
    public long getMessageTimeoutMillis(final ReactorImpl _reactorImpl) {
        if (_reactorImpl instanceof BlockingReactorImpl)
            return 300000;
        return 1000;
    }

    /**
     * Handles message timeout.
     * Default action: close the reactor.
     *
     * @param _reactorImpl    The reactor with the timed-out message
     */
    public void onMessageTimeout(final ReactorImpl _reactorImpl) throws Exception {
        _reactorImpl.getLogger().error("message timeout -> reactor close");
        _reactorImpl.close();
    }

    /**
     * Determines how long a delay after a thread interrupt before the thread is
     * considered hung. Default: 1 second.
     *
     * @param _reactorImpl    The reactor whose thread is being interrupted.
     * @return Number of milliseconds.
     */
    public long getThreadInterruptMillis(final ReactorImpl _reactorImpl) {
        return 1000;
    }

    /**
     * Handles hung thread. Default action: exit the plant.
     *
     * @param _reactorImpl    The reactor whose thread is hung.
     */
    public void onHungThread(final ReactorImpl _reactorImpl) {
        _reactorImpl.getLogger().error("hung thread -> plant exit");
        Plant.exit();
    }

    /**
     * Handles hung request. Default action: close the reactor.
     *
     * @param _requestImpl    The reactor with the hung request.
     */
    public void onHungRequest(final RequestImpl _requestImpl) throws Exception {
        ReactorImpl reactor = _requestImpl.getTargetReactorImpl();
        reactor.getLogger().error("request hung -> reactor close");
        reactor.close();
    }
}
