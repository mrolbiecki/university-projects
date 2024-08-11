public class TramStopArrivalEvent extends Event {
    private final Tram tram;
    private final Stop stop;

    public TramStopArrivalEvent(int time, Tram tram, Stop stop,
                                Simulation simulation) {
        super(time, simulation);
        this.tram = tram;
        this.stop = stop;
    }

    @Override
    public String toString () {
        return getTimeString() + ": "
                + tram + " wjechał na " + stop;
    }

    @Override
    public void happen () {
        System.out.println(this);
        tram.arrivesAtStop(getTime(), stop);
     }
}
