import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    private static void printInput(int dayCount, int stopCapacity, int stopCount,
                                   int passengerCount, int tramCapacity,
                                  int lineCount, Stop[] stops, Line[] lines) {
        System.out.println("Liczba dni symulacji: " + dayCount);
        System.out.println("Pojemność przystanku: " + stopCapacity);
        System.out.println("Liczba przystanków: " + stopCount);
        System.out.println("Nazwy przystanków: ");
        for (Stop s : stops) {
            System.out.println(" " + s.getName());
        }
        System.out.println("Liczba pasażerów: " + passengerCount);
        System.out.println("Pojemność tramwaju: " + tramCapacity);
        System.out.println("Liczba linii tramwajowych: " + lineCount);
        System.out.println("Informacje o liniach: ");
        for (Line l : lines) {
            System.out.println(" Liczba tramwajów: " + l.getVehicleCount());
            int length = l.getRoute().getLength();
            System.out.println(" Długość trasy: " + length);
            Stop[] routeStops = l.getRoute().getStops();
            int[] travelTime = l.getRoute().getTravelTime();
            System.out.println(" Nazwa przystanku i czas dojazdu:");
            for (int i = 0; i < length; i++) {
                System.out.println("  " + routeStops[i].getName() + " " + travelTime[i]);
            }
        }
    }

    private static Simulation readInput() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("in.txt"));
        int dayCount = sc.nextInt();

        //reading stops
        int stopCapacity = sc.nextInt();
        Stop.setCapacity(stopCapacity);
        int stopCount = sc.nextInt();
        Stop[] stops = new Stop[stopCount];
        for (int i = 0; i < stopCount; i++) {
            String name = sc.next();
            stops[i] = new Stop(name);
        }

        //creating passengers
        int passengerCount = sc.nextInt();
        Passenger[] passengers = new Passenger[passengerCount];
        for (int i = 0; i < passengerCount; i++) {
            int randomStop = RandomGeneration.generate(0, stopCount - 1);
            passengers[i] = new Passenger(i, stops[randomStop]);
        }

        //reading trams
        int tramCapacity = sc.nextInt();
        Tram.setCapacity(tramCapacity);

        //reading lines
        int lineCount = sc.nextInt();
        Line[] lines = new Line[lineCount];
        for (int i = 0; i < lineCount; i++) {
            int tramCount = sc.nextInt();
            //reading routes
            int routeLength = sc.nextInt();
            Stop[] routeStops = new Stop[routeLength];
            int[] travelTime = new int[routeLength];
            for (int j = 0; j < routeLength; j++) {
                String name = sc.next();
                routeStops[j] = null;
                for (Stop s : stops) {
                    if (s.getName().equals(name)) {
                        routeStops[j] = s;
                    }
                }
                assert routeStops[j] != null : "Route stop not found";
                travelTime[j] = sc.nextInt();
            }
            lines[i] = new TramLine(i, tramCount, new Route(routeStops, travelTime));
        }
        printInput(dayCount, stopCapacity, stopCount, passengerCount,
                tramCapacity, lineCount, stops, lines);
        return new Simulation(dayCount, lines, passengers);
    }

    public static void main(String[] args) {
        Simulation simulation;
        try {
            simulation = Main.readInput();
        } catch (FileNotFoundException e) {
            System.out.println("in.txt not found");
            return;
        }

        simulation.startSimulation();
    }
}