public class Route {
    private final Stop[] stops;
    private final int [] travelTime;
    private int totalTravelTime;

    public Route(Stop[] stops, int[] travelTime) {
        this.stops = stops;
        this.travelTime = travelTime;
        this.totalTravelTime = 0;
        for (int j : travelTime) {
            this.totalTravelTime +=  j;
        }
    }

    public Stop getStopAtIndex (int index) {
        return stops[index];
    }

    Stop [] getStops () {
        return stops;
    }

    public int getStopCount () {
        return stops.length;
    }

    int [] getTravelTime () {
        return travelTime;
    }

    int getTotalTravelTime () {
        return totalTravelTime;
    }

    int getLength () {
        return stops.length;
    }
}
