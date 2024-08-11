public class PassengerStopArrivalEvent extends Event {
    private final Passenger passenger;
    private final Stop stop;

    public PassengerStopArrivalEvent(int time, Passenger passenger, Stop stop,
                                     Simulation simulation) {
        super(time, simulation);
        this.passenger = passenger;
        this.stop = stop;
    }

    @Override
    public String toString () {
        return getTimeString() + ": "
                + passenger + " próbuje wejść na " + stop;
    }

    @Override
    public void happen () {
        System.out.println(this);
        if (!stop.isFull()) {
            System.out.println("  Udało się!");
            passenger.enterStop(getTime(), stop);
        } else {
            System.out.println( "  Nie udało się - " + passenger + " rezygnuje z podróżowania.");
            passenger.setStopArrivalTime(Simulation.NOT_WAITING);
        }
    }
}
