/**
 * <h1>Messages, Blades, Reactors and Facilities</h1>
 * <p>
 *     The JActor architecture is divided into 4 parts: messages, blades, reactors and facilities.
 * </p>
 * <h2>Messaging</h2>
 * <p>
 *     Both 1-way messages (events) and 2-way messages (request/response) are supported.
 * </p>
 * <p>
 *     Events are reusable and generally immutable. This allows a single event to be passed to multiple
 *     blades. Event messages however must carry a reference to the target blades, so events are wrapped
 *     in an event message before being passed to the target targetReactor for delivery.
 * </p>
 * <p>
 *     In contrast, requests are not reusable. They are themselves messages which are passed to a
 *     target targetReactor and then (usually) returned with a result.
 * </p>
 * <h2>Blades</h2>
 * <p>
 *     Blades are the application logic. Blades define requests, create the requests for the blades
 *     they wish to doSend the requests too, doSend requests, process requests and doSend and process events.
 *     Blades also define and assign exception handlers for the messages they receive.
 *     Every blades has a targetReactor which processes the messages sent to that blades and for sending requests
 *     to other blades. Thread safety for a blades is achieved by the targetReactor, which processes only only one message at a time.
 * </p>
 * <h2>Reactors</h2>
 * <p>
 *     A targetReactor receives and subsequently processes the messages for one or more blades, processing those
 *     messages one at a time. A targetReactor can also buffer messages being sent to other blades,
 *     and then sends all the messages for each destination as a group to reduce the overhead
 *     of message passing.
 * </p>
 * <p>
 *     A targetReactor has an inbox, which is a queue of received messages, and an outbox that has a doSend buffer
 *     for each destination.
 * </p>
 * <h2>Facilities</h2>
 * <p>
 *     A facility is used to assign threads to reactors that have an inbox that is not empty,
 *     with at most one thread being assigned to any given targetReactor.
 * </p>
 * <p>
 *     There can be more than one facility within a program--which is important when working
 *     with OSGi. Each facility has a property set and an independent lifecycle. When messages are or have been
 *     sent to a facility that is closed, an exception is raised in the originating blades.
 * </p>
 * <p>
 *     Multiple facilities can also be important when a Swing program has multiple frames that
 *     have associated background tasks. If a frame has its own facility that is closed when
 *     the frame is closed, all the background activity associated with that frame is terminated.
 * </p>
 */
package org.agilewiki.jactor2.core;