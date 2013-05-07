package org.agilewiki.jactor.testService;

import org.agilewiki.jactor.testIface.Hello;
import org.agilewiki.jactor.util.osgi.MailboxFactoryActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

public class Activator extends MailboxFactoryActivator {
    private final Logger logger = LoggerFactory.getLogger(Activator.class);

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        logger.error("testUtil location: " + bundleContext.getBundle().getLocation());
        logger.error("testUtil location: " + bundleContext.getBundle().getSymbolicName());
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        context.registerService(
                Hello.class.getName(),
                new HelloService(context),
                new Hashtable<String, String>());
    }
}