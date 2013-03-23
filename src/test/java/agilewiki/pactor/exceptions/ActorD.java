package agilewiki.pactor.exceptions;

import org.agilewiki.pactor.*;

public class ActorD {
    private final Mailbox mailbox;

    public ActorD(final Mailbox mbox) {
        this.mailbox = mbox;
    }

    Request<Void> doSomethin() {
        return new RequestBase<Void>(mailbox) {
            @Override
            public void processRequest(
                    final ResponseProcessor<Void> responseProcessor)
                    throws Exception {
                responseProcessor.processResponse(null);
            }
        };
    }

    public Request<String> throwRequest() {
        return new RequestBase<String>(mailbox) {
            @Override
            public void processRequest(
                    final ResponseProcessor<String> responseProcessor)
                    throws Exception {
                mailbox.setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public void processException(final Throwable throwable)
                            throws Exception {
                        responseProcessor.processResponse(throwable.toString());
                    }
                });
                doSomethin().reply(mailbox, new ResponseProcessor<Void>() {
                    @Override
                    public void processResponse(final Void response)
                            throws Exception {
                        throw new SecurityException("thrown on request");
                    }
                });
            }
        };
    }
}
