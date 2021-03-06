package org.agilewiki.jactor2.core.plant;

/**
 * A scheduler for Plant, created by PlantConfiguration.
 */
public interface Scheduler {
    /**
     * Schedule a Runnable to be run at a later time.
     *
     * @param _runnable          The Runnable to be scheduled.
     * @param _millisecondDelay How long to wait before the Runnable is to be run.
     */
    void schedule(Runnable _runnable, long _millisecondDelay);

    /**
     * Schedule a Runnable to be run repeatedly.
     *
     * @param _runnable          The Runnable to be run.
     * @param _millisecondDelay The delay between each run.
     */
    void scheduleAtFixedRate(Runnable _runnable, long _millisecondDelay);

    /**
     * Returns the approximate time.
     *
     * @return The approximate time.
     */
    long currentTimeMillis();

    /**
     * Shut down the scheduler thread pool.
     */
    void close();
}
