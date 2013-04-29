package org.agilewiki.pactor.utilImpl.durable.collection.bmap;

import org.agilewiki.pactor.api.Mailbox;
import org.agilewiki.pactor.util.Ancestor;
import org.agilewiki.pactor.util.durable.*;
import org.agilewiki.pactor.utilImpl.durable.FactoryImpl;
import org.agilewiki.pactor.utilImpl.durable.collection.smap.LongSMapFactory;
import org.agilewiki.pactor.utilImpl.durable.scalar.vlens.UnionImpl;

/**
 * Creates LongBMap's.
 */
public class LongBMapFactory extends FactoryImpl {
    private final static int NODE_CAPACITY = 28;

    public static void registerFactories(final FactoryLocator _factoryLocator) {
        registerFactory(_factoryLocator, PAMap.LONG_PASTRING_BMAP, PAString.FACTORY_NAME);
        registerFactory(_factoryLocator, PAMap.LONG_BYTES_BMAP, Bytes.FACTORY_NAME);
        registerFactory(_factoryLocator, PAMap.LONG_BOX_BMAP, Box.FACTORY_NAME);
        registerFactory(_factoryLocator, PAMap.LONG_PALONG_BMAP, PALong.FACTORY_NAME);
        registerFactory(_factoryLocator, PAMap.LONG_PAINTEGER_BMAP, PAInteger.FACTORY_NAME);
        registerFactory(_factoryLocator, PAMap.LONG_PAFLOAT_BMAP, PAFloat.FACTORY_NAME);
        registerFactory(_factoryLocator, PAMap.LONG_PADOUBLE_BMAP, PADouble.FACTORY_NAME);
        registerFactory(_factoryLocator, PAMap.LONG_PABOOLEAN_BMAP, PABoolean.FACTORY_NAME);
    }

    public static void registerFactory(FactoryLocator factoryLocator,
                                       String actorType,
                                       String valueType) {
        UnionImpl.registerFactory(factoryLocator,
                "U." + actorType, "LM." + actorType, "IM." + actorType);

        factoryLocator.registerFactory(new LongBMapFactory(
                actorType, valueType, true, true));
        factoryLocator.registerFactory(new LongBMapFactory(
                "IN." + actorType, valueType, false, false));

        LongSMapFactory.registerFactory(
                factoryLocator, "LM." + actorType, valueType, NODE_CAPACITY);
        LongSMapFactory.registerFactory(
                factoryLocator, "IM." + actorType, "IN." + actorType, NODE_CAPACITY);
    }

    private String valueType;
    private boolean isRoot = true;
    private boolean auto = true;

    /**
     * Create an FactoryImpl.
     *
     * @param jidType   The jid type.
     * @param valueType The value type.
     */
    protected LongBMapFactory(String jidType, String valueType,
                              boolean isRoot, boolean auto) {
        super(jidType);
        this.valueType = valueType;
        this.isRoot = isRoot;
        this.auto = auto;
    }

    /**
     * Create a JLPCActor.
     *
     * @return The new actor.
     */
    @Override
    protected LongBMap instantiateActor() {
        return new LongBMap();
    }

    /**
     * Create and configure an actor.
     *
     * @param mailbox The mailbox of the new actor.
     * @param parent  The parent of the new actor.
     * @return The new actor.
     */
    @Override
    public LongBMap newSerializable(Mailbox mailbox, Ancestor parent) {
        LongBMap imj = (LongBMap) super.newSerializable(mailbox, parent);
        FactoryLocator fl = Durables.getFactoryLocator(mailbox);
        imj.valueFactory = fl.getFactory(valueType);
        imj.nodeCapacity = NODE_CAPACITY;
        imj.isRoot = isRoot;
        imj.init();
        if (auto)
            imj.setNodeLeaf();
        return imj;
    }
}
