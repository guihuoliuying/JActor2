package org.agilewiki.pactor;

/**
 * <p>
 * Request Object represents the User/Application data that needs to be processed. 
 * Request abstracts the Application/User Request(data) that is dispatched to the Actor's mailbox for asynchronous execution.
 * The Request object should be created in the PActor, it abstracts the user/application data that needs to be processed 
 * by the lightweight thread attached to the Actors mailbox.
 * </p>
 */

public interface UnboundRequest<RESPONSE_TYPE, TARGET_ACTOR_TYPE extends Actor>
        extends _Request<RESPONSE_TYPE, TARGET_ACTOR_TYPE> {

    /**
     * This will signal the current Request to the mailbox for asynchronous processing.
     * 
     */
    public void signal(final TARGET_ACTOR_TYPE _targetActor) throws Exception;

    public void signal(final Mailbox source, final TARGET_ACTOR_TYPE _targetActor)
            throws Exception;

    /**
     * send will be used when chain of PActors needs to process the User/Application Request.
     * The responseProcessor would be shared for PActor chain.
     * 
     * @param source The mailbox associated with the Request for which the ResponseMessage is to 
     * added for asynchronous processing.
     * 
     * @param responseProcessor The associated ResponseProcessor whose role is to process the response.
     * @throws Exception Will thrown Exception if the source mailbox is not running.
     */ 
    public void send(final Mailbox source,
                     final TARGET_ACTOR_TYPE _targetActor,
                     final ResponseProcessor<RESPONSE_TYPE> responseProcessor)
            throws Exception;

    /**
     * This will make the invoking thread to wait for the response before continuing ahead.
     * It will let the invocation to be synchronous for the calling thread. It is better to evaluate 
     * if plain OO call would for using instead of call.
     * 
     * @return RESPONSE_TYPE
     * @throws Exception
     */ 
    public RESPONSE_TYPE call(final TARGET_ACTOR_TYPE _targetActor) throws Exception;
}