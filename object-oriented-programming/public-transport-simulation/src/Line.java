import MinQueue.MinQueue;

public abstract class Line {
    private static int totalVehicleCount = 0;

    private final int number;
    private final int vehicleCount;
    private final Route route;
    private Simulation simulation;

    public Line(int number, int vehicleCount, Route route) {
        this.number = number;
        this.vehicleCount = vehicleCount;
        this.route = route;
    }

    public static int getTotalVehicleCount() {
        return Line.totalVehicleCount;
    }

    public static void incrementTotalVehicleCount() {
        totalVehicleCount++;
    }

    public Simulation getSimulation() {
        return this.simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public int getNumber () {
        return number;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public Route getRoute() {
        return route;
    }

    public abstract void addStopEvents(MinQueue<Event> Q);

    public void clearStops () {
        for (Stop stop : route.getStops()) {
            stop.clear();
        }
    }

    public abstract void clearVehicles ();
}
