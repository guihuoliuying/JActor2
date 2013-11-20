package org.agilewiki.jactor2.core.util;

import org.agilewiki.jactor2.core.facilities.Plant;
import org.agilewiki.jactor2.core.facilities.ServiceClosedException;
import org.agilewiki.jactor2.core.messages.SyncRequest;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

abstract public class CloserBase extends CloseableBase implements Closer {

    /**
     * A set of AutoCloseable objects.
     * Can only be accessed via a request to the facility.
     */
    private Set<AutoCloseable> closeables;

    /**
     * Returns true when the first phase of closing has begun.
     *
     * @return True when the first phase of closing has begun.
     */
    abstract protected boolean startedClosing();

    /**
     * Performs the second phase of closing.
     */
    abstract protected void close2() throws Exception;

    /**
     * Returns the logger.
     *
     * @return A logger.
     */
    abstract public Logger getLog();

    /**
     * Returns the CloseableSet. Creates it if needed.
     */
    protected final Set<AutoCloseable> getCloseableSet() {
        if (closeables == null) {
            closeables = Collections.newSetFromMap(new WeakHashMap<AutoCloseable, Boolean>());
        }
        return closeables;
    }

    protected boolean isCloseablesEmpty() {
        return closeables == null || closeables.isEmpty();
    }

    @Override
    public SyncRequest<Boolean> addCloseableSReq(final AutoCloseable _closeable) {
        return new SyncRequest<Boolean>(getReactor()) {
            @Override
            protected Boolean processSyncRequest() throws Exception {
                if (startedClosing())
                    throw new ServiceClosedException();
                if (!getCloseableSet().add(_closeable))
                    return false;
                if (_closeable instanceof Closeable)
                    ((Closeable) _closeable).addCloserSReq(CloserBase.this).signal();
                return true;
            }
        };
    }

    @Override
    public SyncRequest<Boolean> removeCloseableSReq(final AutoCloseable _closeable) {
        return new SyncRequest<Boolean>(getReactor()) {
            @Override
            protected Boolean processSyncRequest() throws Exception {
                if (closeables == null)
                    return false;
                if (!closeables.remove(_closeable))
                    return false;
                if (_closeable instanceof Closeable)
                    ((Closeable) _closeable).removeCloserSReq(CloserBase.this).signal();
//                System.out.println("removeClosable "+closeables.size());
                if (startedClosing() && closeables.isEmpty()) {
                    close2();
                }
                return true;
            }
        };
    }

    protected void closeAll() {
        if (closeables == null)
            return;
//        System.out.println("to close "+closeables.size());
        Iterator<AutoCloseable> it = closeables.iterator();
        while (it.hasNext()) {
            AutoCloseable closeable = it.next();
            try {
//                System.out.println("    "+closeable);
                closeable.close();
                if (!(closeable instanceof Closeable)) {
//                    System.out.println("    removed");
                    it.remove();
                }
            } catch (final Throwable t) {
                if (closeable != null && Plant.DEBUG) {
                    getLog().warn("Error closing a " + closeable.getClass().getName(), t);
                }
            }
        }
    }
}
