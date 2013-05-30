package org.agilewiki.jactor.util.osgi;

import org.agilewiki.jactor.api.Mailbox;
import org.agilewiki.jactor.api.MailboxFactory;
import org.agilewiki.jactor.api.Properties;
import org.agilewiki.jactor.util.durable.Durables;
import org.agilewiki.jactor.util.durable.incDes.Root;
import org.agilewiki.jactor.util.osgi.durable.OsgiFactoryLocator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

final public class Osgi {

    /**
     * Returns the BundleContext saved in the bundleContext property of a MailboxFactory.
     *
     * @param _mailboxFactory The mailbox factory.
     * @return The BundleContext.
     */
    public static BundleContext getBundleContext(final MailboxFactory _mailboxFactory) {
        Properties p = _mailboxFactory.getProperties();
        return (BundleContext) p.getProperty("bundleContext");
    }

    /**
     * Returns the version in the form major.minor.micro or major.minor.micro-qualifier.
     * This is in contrast to Version.toString, which uses a . rather than a - with a qualifier.
     *
     * @param version The version.
     * @return The formatted version.
     */
    public static String getNiceVersion(Version version) {
        int q = version.getQualifier().length();
        StringBuffer result = new StringBuffer(20 + q);
        result.append(version.getMajor());
        result.append(".");
        result.append(version.getMinor());
        result.append(".");
        result.append(version.getMicro());
        if (q > 0) {
            result.append("-");
            result.append(version.getQualifier());
        }
        return result.toString();
    }

    public static OsgiFactoryLocator getOsgiFactoryLocator(final Mailbox _mailbox) {
        return (OsgiFactoryLocator) Durables.getFactoryLocator(_mailbox);
    }

    public static OsgiFactoryLocator getOsgiFactoryLocator(final MailboxFactory _mailboxFactory) {
        return (OsgiFactoryLocator) Durables.getFactoryLocator(_mailboxFactory);
    }

    public static Root contextualize(Root root) throws Exception {
        //todo
        return null;
    }
}