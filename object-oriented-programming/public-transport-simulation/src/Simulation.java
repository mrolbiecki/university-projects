import MinQueue.MinQueue;
import MinQueue.SortedVector;

public class Simulation {
    public static final int NOT_WAITING = 24 * 60;
    public static final int END_OF_DAY = 24 * 60;
    public static final int START_OF_DAY = 6 * 60;
    public static final int LAST_PASSENGER_WAKEUP = 12 * 60;
    public static final int LAST_TRAM_DEPARTURE = 23 * 60;
    //parameters
    private final int dayCount;
    private final Line [] lines;
    private final Passenger [] passengers;

    private final MinQueue<Event> Q;
    //for statistics
    private final int [] timeWaitedDayArray;
    private final int [] ridesCountDayArray;
    private int ridesTotalCount = 0;
    private int ridesCountDay;
    private int timeWaitedDay;
    private int totalTimeWaited = 0;
    private int passengersStillWaitingTotalCount = 0;

    public Simulation(int dayCount, Line[] lines, Passenger[] passengers) {
        this.dayCount = dayCount;
        this.lines = lines;
        this.passengers = passengers;
        timeWaitedDayArray = new int[dayCount];
        ridesCountDayArray = new int[dayCount];
        Q = new SortedVector<Event>();
    }

    public void addToTimeWaitedDay (int time) {
        timeWaitedDay += time;
    }

    public void increaseNumberOfRidesDay () {
        ridesCountDay++;
    }

    public void startSimulation() {
        initialization();
        System.out.println("START!!");
        for (int i = 0; i < dayCount; i++) {
            startDay(i);
        }
        displayStats();
    }

    //all objects need to know which simulation they're in so that statistics can be collected
    private void initialization () {
        for (Passenger passenger : passengers) {
            passenger.setSimulation(this);
        }
        for (Line line : lines) {
            line.setSimulation(this);
        }
    }

    private void startDay (int day) {
        //resetting day statistics
        timeWaitedDay = 0;
        ridesCountDay = 0;
        //filling the event queue
        addStopEvents();
        addPassengerEvents();

        System.out.println("STARTING DAY " + day);
        while (!Q.isEmpty()) {
            Q.top().happen();
            Q.pop();
        }
        //collecting statistics
        for (Passenger passenger : passengers) {
            if (passenger.getStopArrivalTime() != NOT_WAITING) {
                passengersStillWaitingTotalCount++;
                timeWaitedDay += (END_OF_DAY - passenger.getStopArrivalTime());
            }
        }
        timeWaitedDayArray[day] = timeWaitedDay;
        ridesCountDayArray[day] = ridesCountDay;
        totalTimeWaited += timeWaitedDay;
        ridesTotalCount += ridesCountDay;
        //clearing up objects
        for (Line line : lines) {
            line.clearStops();
            line.clearVehicles();
        }
    }

    private void addStopEvents () {
        for (Line line : lines) {
            line.addStopEvents(Q);
        }
    }

    private void addPassengerEvents () {
        //for every passenger stop arrival time is chosen randomly each day, but stop stays the same
        for (Passenger passenger : passengers) {
        int time = RandomGeneration.generate(START_OF_DAY, LAST_PASSENGER_WAKEUP);
            Q.insert(new PassengerStopArrivalEvent(time, passenger, passenger.getNearHomeStop(), this));
        }
    }

    private void displayStats () {
        for (int i = 0; i < dayCount; i++) {
            displayDayStats(i);
        }
        displayTotalStats();
    }

    private void displayDayStats(int day) {
        System.out.println("Statystyki dla dnia " + day + ":");
        System.out.println("  Łącznia liczba przejazdów: " + ridesCountDayArray[day]);
        System.out.println("  Łączny czas oczekiwania: " + timeWaitedDayArray[day]);
    }

    private void displayTotalStats () {
        System.out.println("Statystyki dla całej symulacji: ");
        System.out.println("  Łączna liczba przejazdów: " + ridesTotalCount);
        System.out.println("  Łączny czas oczekiwania: " + totalTimeWaited);
        System.out.print("  Średni czas oczekiwania: ");
        if (ridesTotalCount + passengersStillWaitingTotalCount == 0) {
            System.out.println(0);
        } else {
            System.out.println((float)totalTimeWaited / (ridesTotalCount + passengersStillWaitingTotalCount));
        }
    }
}
