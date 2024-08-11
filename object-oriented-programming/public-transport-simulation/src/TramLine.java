import MinQueue.MinQueue;
public class TramLine extends Line{
    private final Tram [] trams;
    private final int gap;

    public TramLine (int number, int vehicleCount, Route route) {
        super (number, vehicleCount, route);
        //rounding up
        this.gap = (route.getTotalTravelTime() * 2 + vehicleCount - 1) / vehicleCount;
        this.trams = new Tram[vehicleCount];
        //creating trams
        for (int i = 0; i < vehicleCount; i++) {
            trams[i] = new Tram(Line.getTotalVehicleCount(), this, getSimulation());
            Line.incrementTotalVehicleCount();
        }
    }

    @Override
    public void clearVehicles () {
        for (Tram tram : trams) {
            tram.clear();
        }
    }

    @Override
    public void addStopEvents(MinQueue<Event> Q) {
        int halfTramCount = (trams.length + 1) / 2;
        int time = Simulation.START_OF_DAY;
        //first part of trams going in ascending direction
        for (int i = 0; i < halfTramCount; i++) {
            trams[i].scheduleStopsDay(Q, time);
            time += gap;
        }
        time = Simulation.START_OF_DAY;
        //second part of trains going in descending direction
        for (int i = halfTramCount; i < trams.length; i++) {
            trams[i].scheduleStopsDayReversed(Q, time);
            time += gap;
        }
    }
}
