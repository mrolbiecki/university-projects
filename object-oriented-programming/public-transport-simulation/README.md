# Public Transport Simulation

## Project Overview

This project is a simplified simulation of urban traffic, developed as part of a homework assignment for the Object-Oriented Programming course at the University of Warsaw. The main objective is to design a set of Java classes to simulate city traffic with a strong emphasis on object-oriented principles.

## Features

- **Custom Data Structures**: Implementation of custom Vector, Queue, and Priority Queue data structures.
- **Statistics**: Collection and display of various statistics at the end of the simulation.
- **User-Defined Routes**: Users can create their own set of stops and routes.
- **Scalability**: Easily extendable to include additional means of transport and event types.
- **OOP Principles**: Adheres to core object-oriented programming principles, resulting in a robust and scalable codebase.

### Simulation Details

- **Tram Operations**: Trams are dispatched from both ends of their routes, starting from 6 AM. They travel the entire route back and forth, stopping at each end before reversing direction. The frequency of tram dispatches is calculated based on the total travel time and the number of trams on the route.
- **Passenger Behavior**: Passengers attempt to board any tram at their nearest stop and travel to a random stop along the route. If a tram is full, they continue to wait for the next one.
- **Event Queue**: The simulation operates using an event queue, where events are processed in order of occurrence. This allows for a detailed tracking of all events during the simulation period.

## Program Output

The program outputs three key pieces of information:

1. **Loaded Parameters**: The simulation parameters loaded from the input data.
2. **Detailed Simulation Log**: A log of all events during the simulation, including the time, type of event, and details such as passenger actions and tram movements.
3. **Simulation Statistics**: Summary statistics for the entire simulation, including:
   - Total number of passenger trips.
   - Average waiting time at stops.
   - Daily statistics, including the total number of trips and total waiting time.

## How to Run the Simulation

The project can be compiled and run using JetBrains IntelliJ IDEA.
The simulation reads data from a text file`in.txt` in the following format:

1. Number of simulation days.
2. Stop capacity.
3. Number of stops, followed by:
   - Stop name and number of passengers.
4. Tram capacity and number of tram lines.
5. For each tram line:
   - Number of trams and route length.
   - Route details, including stop names and travel times.
