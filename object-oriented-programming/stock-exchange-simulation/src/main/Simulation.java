package main;

import main.investors.Investor;

import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;

public class Simulation {
    public static final boolean displayData = false;

    private final ArrayList<Investor> investors;
    private final OrderBook orderBook;
    private final int turns;

    public Simulation(ArrayList<Investor> investors, Map<String, Integer> stocks, int turns) {
        if (displayData) {
            System.out.println("Simulation created!");
        }
        this.investors = investors;
        this.orderBook = new OrderBook(stocks);
        this.turns = turns;
    }

    /**
     * Starts the simulation, iterating through the specified number of turns.
     */
    public void startSimulation() {
        for (int turn = 1; turn <= turns; turn++) {
            startTurn(turn);
            if (displayData) {
                System.out.println("End of turn " + turn);
                System.out.println("OrderBook: \n" + orderBook);
                printInvestors();
            }
        }
        orderBook.cancelAllOrders(); // Cancel all remaining orders at the end of the simulation.
        System.out.println("End of simulation. These are the portfolios of investors: ");
        printInvestors();
    }

    /**
     * Prints the details of all investors including their portfolios and transaction count.
     */
    public void printInvestors() {
        System.out.println("Investors:\n----------");
        for (Investor investor : investors) {
            System.out.println(investor);
            System.out.println("Transaction counter: " + investor.getTransactionsCounter());
            Portfolio portfolio = investor.getPortfolio();
            System.out.println(portfolio);
            System.out.println("Estimated net worth: " + portfolio.getEstimatedNetWorth(orderBook));
            System.out.println("----------");
        }
    }

    /**
     * Executes the actions for a single turn of the simulation.
     * @param turn The current turn number.
     */
    public void startTurn(int turn) {
        Collections.shuffle(investors); // Randomize the order of investors each turn.
        for (Investor investor : investors) {
            investor.decide(turn, orderBook); // Each investor makes their decision for the turn.
        }
        orderBook.deleteExpiredAndEmptyOrders(turn);
    }

    /**
     * Getter for the ArrayList of investors.
     * @return the ArrayList of investors.
     */
    public ArrayList<Investor> getInvestors() {
        return investors;
    }
}
