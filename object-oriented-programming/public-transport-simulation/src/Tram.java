import MinQueue.MinQueue;
import Vector.Vector;

public class Tram extends Vehicle {
    private static int capacity;

    private final Simulation simulation;
    private final Vector<Passenger> passengers;
    private Direction direction;
    private int currentStopIndex;

    public Tram (int sideNumber, Line line, Simulation simulation) {
        super(sideNumber, line);
        passengers = new Vector<Passenger>();
        this.simulation = simulation;
    }

    public void scheduleStopsDay (MinQueue<Event> Q, int time) {
        int totalTravelTime = getLine().getRoute().getTotalTravelTime();
        direction = Direction.ASCENDING;
        //currentStopIndex is increased when tram arrives at first stop
        currentStopIndex = -1;
        while (time <= Simulation.LAST_TRAM_DEPARTURE) {
            scheduleStopsRoute(Q, time);
            time += totalTravelTime;
            //tram changes direction
            scheduleStopsRouteReversed(Q, time);
            time += totalTravelTime;
        }
    }

    public void scheduleStopsDayReversed (MinQueue<Event> Q, int time) {
        int totalTravelTime = getLine().getRoute().getTotalTravelTime();
       direction = Direction.DESCENDING;
        //currentStopIndex is decreased when tram arrives at first stop
        currentStopIndex = getStopCount();
        while (time <= Simulation.LAST_TRAM_DEPARTURE) {
            scheduleStopsRouteReversed(Q, time);
            time += totalTravelTime;
            //tram changes direction
            scheduleStopsRoute(Q, time);
            time += totalTravelTime;
        }
    }

    public void scheduleStopsRoute (MinQueue<Event> Q, int time) {
        Stop [] stops = getLine().getRoute().getStops();
        int [] travelTime = getLine().getRoute().getTravelTime();
        for (int i = 0; i < stops.length; i++) {
            Q.insert(new TramStopArrivalEvent(time, this, stops[i], simulation));
            time += travelTime[i];
        }
    }

    public void scheduleStopsRouteReversed(MinQueue<Event> Q, int time){
        Stop [] stops = getLine().getRoute().getStops();
        int [] travelTime = getLine().getRoute().getTravelTime();
        for (int i = stops.length - 1; i >= 0; i--) {
            Q.insert(new TramStopArrivalEvent(time, this, stops[i], simulation));
            if (i == 0) {
                time += travelTime[travelTime.length - 1];
            } else {
                time += travelTime[i - 1];
            }
        }
    }

    public void arrivesAtStop (int time, Stop stop) {
        //currentStopIndex is increased or decreased when tram arrives at first stop
        if (direction == Direction.ASCENDING) {
            currentStopIndex++;
        }
        else { //direction == DESCENDING
            currentStopIndex--;
        }
        releasePassengers(time, stop);
        if (!isOnLastStop()) {
            letPassengersIn(time, stop);
        }
        if (isOnLastStop()) {
            changeDirection();
        }
    }

    private void changeDirection () {
        if (direction == Direction.ASCENDING) {
            currentStopIndex = getStopCount();
            direction = Direction.DESCENDING;
        } else {
            currentStopIndex = -1;
            direction = Direction.ASCENDING;
        }
    }

    private boolean isOnLastStop () {
        if (direction == Direction.ASCENDING) {
            return currentStopIndex == getStopCount() - 1;
        } else {
            return currentStopIndex == 0;
        }
    }

    private void releasePassengers (int time, Stop stop) {
        for (int i = 0; i < passengers.size(); i++) {
            if (!stop.isFull() && passengers.at(i).getDestinationStop() == stop) {
                passengers.at(i).exitTram(time, this, stop);
                passengers.remove(i);
            }
        }
    }

    private void letPassengersIn (int time, Stop stop) {
        while (passengers.size() < Tram.getCapacity() && !stop.isEmpty()) {
            Passenger firstPassenger = stop.getFirstPassenger();
            firstPassenger.enterTram(time, this, stop);
            passengers.push_back(firstPassenger);
            stop.removeFirstPassenger();
        }
    }

    public void clear () {
        while (!passengers.isEmpty()) {
            passengers.pop_back();
        }
    }

    public static void setCapacity(int capacity) {
        Tram.capacity = capacity;
    }

    public static int getCapacity() {
        return Tram.capacity;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getCurrentStopIndex() {
        return currentStopIndex;
    }

    public int getStopCount () {
        return getLine().getRoute().getStopCount();
    }

    @Override
    public String toString () {
        return "tramwaj linii nr " + getLine().getNumber()
                + " (nr boczny: " + getSideNumber() + ")";
    }
}
