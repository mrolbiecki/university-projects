abstract public class Event implements Comparable<Event> {
    private final int time;
    private final Simulation simulation;

    public Event (int time, Simulation simulation){
        this.time = time;
        this.simulation = simulation;
    }

    public Simulation getSimulation(){
        return simulation;
    }

    public int getTime () {
        return this.time;
    }

    public String getTimeString () {
        int hours = this.time / 60;
        int minutes = this.time % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    abstract public void happen ();

    @Override //from Comparable<T>
    public int compareTo (Event other){
        if (this.time == other.time){
            return 0;
        } else {
            return this.time < other.time ? -1 : 1;
        }
    }

    @Override
    abstract public String toString();
}
