public class PassengerEntersTramEvent extends Event {
    private final Passenger passenger;
    private final Stop startingStop;
    private final Stop destinationStop;
    private final Tram tram;

    public PassengerEntersTramEvent(int time, Passenger passenger, Tram tram,
                                    Stop startingStop, Stop destinationStop,
                                    Simulation simulation) {
        super(time, simulation);
        this.passenger = passenger;
        this.startingStop = startingStop;
        this.destinationStop = destinationStop;
        this.tram = tram;
    }

    @Override
    public String toString () {
        return getTimeString() + ": "
                + passenger + " wsiadł do " + tram
                + " z " + startingStop
                + " z zamiarem dojechania na " + destinationStop;
    }

    @Override
    public void happen () {
        System.out.println(this);
        getSimulation().addToTimeWaitedDay(getTime() - passenger.getStopArrivalTime());
        getSimulation().increaseNumberOfRidesDay();
    }
}
