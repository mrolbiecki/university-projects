package main;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int turns = Integer.parseInt(args[1]);
        InputParser parser = new InputParser(args[0]);
        try {
            Simulation simulation = parser.parse(turns);
            simulation.startSimulation();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
