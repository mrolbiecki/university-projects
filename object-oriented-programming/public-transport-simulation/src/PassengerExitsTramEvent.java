public class PassengerExitsTramEvent extends Event {
    private final Passenger passenger;
    private final Stop stop;
    private final Tram tram;

    public PassengerExitsTramEvent(int time, Passenger passenger, Tram tram,
                                   Stop stop, Simulation simulation) {
        super(time, simulation);
        this.passenger = passenger;
        this.stop = stop;
        this.tram = tram;
    }

    @Override
    public String toString () {
        return getTimeString() + ": "
                + passenger + " wysiadł z " + tram
                + " i czeka na " + stop;
    }

    @Override
    public void happen () {
        System.out.println(this);
    }
}
