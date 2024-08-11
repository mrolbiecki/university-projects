public class Passenger {
    private Simulation simulation;
    private final int number;
    private final Stop nearHomeStop;
    private Stop destinationStop;
    private int stopArrivalTime;

    public Passenger(int number, Stop nearHomeStop) {
        this.number = number;
        this.nearHomeStop = nearHomeStop;
    }

    public void enterStop (int time, Stop stop) {
        stop.addNewPassenger(this);
        stopArrivalTime = time;
    }

    public void exitTram (int time, Tram tram, Stop stop) {
        enterStop(time, stop);
        PassengerExitsTramEvent event =
                new PassengerExitsTramEvent(time, this, tram, stop, simulation);
        event.happen();
    }

    private Stop chooseDestinationStop(Tram tram) {
        int lo, hi;
        //choose random stop from stops on route in that direction
        if (tram.getDirection() == Direction.ASCENDING) {
            lo = tram.getCurrentStopIndex() + 1;
            hi = tram.getStopCount() - 1;
        } else { //direction == DESCENDING
            lo = 0;
            hi = tram.getCurrentStopIndex() - 1;
        }
        return tram.getLine().getRoute()
                .getStopAtIndex(RandomGeneration.generate(lo, hi));
    }

    public void enterTram (int time, Tram tram, Stop stop) {
        destinationStop = chooseDestinationStop(tram);
        PassengerEntersTramEvent event = new PassengerEntersTramEvent(time, this,
                                                    tram, stop, destinationStop, simulation);
        event.happen();
        //passengers stuck inside trams are not counted as waiting in statistics
        stopArrivalTime = Simulation.NOT_WAITING;
    }

    public void setStopArrivalTime (int time) {
        stopArrivalTime = time;
    }

    public int getStopArrivalTime() {
        return stopArrivalTime;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    Stop getNearHomeStop () {
        return nearHomeStop;
    }

    Stop getDestinationStop () {
        return destinationStop;
    }

    @Override
    public String toString() {
        return "pasażer nr " + number;
    }
}
