import Queue.Queue;

public class Stop {
    private static int capacity;

    private final String name;
    //passengers are standing in Queue so it's more fair
    private final Queue<Passenger> passengers;

    public Stop(String name) {
        this.name = name;
        passengers = new Queue<Passenger>();
    }

    public boolean isFull () {
        return passengers.size() == capacity;
    }

    public boolean isEmpty () {
        return passengers.isEmpty();
    }

    public void addNewPassenger(Passenger passenger) {
        passengers.push(passenger);
    }

    public void removeFirstPassenger() {
        passengers.pop();
    }

    public void clear () {
        while (!passengers.isEmpty()) {
            passengers.pop();
        }
    }

    public String getName() {
        return this.name;
    }

    public Passenger getFirstPassenger() {
        assert !isEmpty();
        return passengers.front();
    }

    public static void setCapacity(int capacity) {
        Stop.capacity = capacity;
    }

    @Override
    public String toString () {
        return "przystanek " + this.name;
    }
}
