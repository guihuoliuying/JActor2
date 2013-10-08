import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.blades.misc.Printer;
import org.agilewiki.jactor2.core.facilities.Facility;
import org.agilewiki.jactor2.core.messages.AsyncRequest;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

public class DiningRoom extends BladeBase {
    public DiningRoom(final Reactor _reactor)
            throws Exception {
        initialize(_reactor);
    }
    
    public AsyncRequest<List<Integer>> feastAReq(final int _seats, final int _meals)
            throws Exception {
        return new AsyncBladeRequest<List<Integer>>() {
            final AsyncResponseProcessor<List<Integer>> dis = this;
            List<Integer> mealsEaten = new LinkedList<Integer>();
            
            AsyncResponseProcessor<Integer> feastResponseProcessor =
                new AsyncResponseProcessor<Integer>() {
                    @Override
                    public void processAsyncResponse(final Integer _feastResponse) 
                            throws Exception {
                        mealsEaten.add(_feastResponse);
                        if (mealsEaten.size() == _seats) {
                            dis.processAsyncResponse(mealsEaten);
                        }
                    }
            };
            
            @Override
            protected void processAsyncRequest() throws Exception {
                int i = 0;
                Reactor myReactor = getReactor();
                Facility facility = myReactor.getFacility();
                DiningTable diningTable = new DiningTable(
                    new NonBlockingReactor(facility),
                    _seats,
                    _meals);
                while (i < _seats) {
                    DiningPhilosopher diningPhilosopher =
                        new DiningPhilosopher(new NonBlockingReactor(facility));
                    AsyncRequest<Integer> feastAReq = diningPhilosopher.feastAReq(diningTable, i);
                    send(feastAReq, feastResponseProcessor);
                    ++i;
                }
            }
        };
    }
    
    public static void main(String[] args) throws Exception {
        int seats = 5;
        int meals = 1000000;
        Facility facility = new Facility();
        try {
            NonBlockingReactor diningRoomReactor = new NonBlockingReactor(facility);
            DiningRoom diningRoom = new DiningRoom(diningRoomReactor);
            AsyncRequest<List<Integer>> feastAReq = diningRoom.feastAReq(seats, meals);
            long before = System.nanoTime();
            List<Integer> mealsEaten = feastAReq.call();
            long after = System.nanoTime();
            Printer printer = new Printer(new IsolationReactor(facility));
            printer.printfSReq("Seats: %,d%n", seats).call();
            printer.printfSReq("Meals: %,d%n", meals).call();
            printer.printlnSReq("\nMeals eaten by each philosopher:").call();
            Iterator<Integer> it = mealsEaten.iterator();
            while (it.hasNext()) {
                int me = it.next();
                printer.printfSReq("    %,d%n", me).call();
            }
            long duration = after - before;
            printer.printfSReq("\nTest duration in nanoseconds: %,d%n", duration).call();
            if (duration > 0) {
                printer.printfSReq("Total meals eaten per second: %,d%n%n", 1000000000L * meals / duration).call();
            }
        } finally {
            facility.close();
        }
    }
}