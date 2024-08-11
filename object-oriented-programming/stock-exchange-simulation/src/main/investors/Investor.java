package main.investors;

import main.Portfolio;
import main.OrderBook;

/**
 * Abstract class representing an investor in the simulation.
 * Contains methods and properties common to all types of investors.
 */
public abstract class Investor {
    private static int investorCounter = 0;
    private final Portfolio portfolio;
    private final int nr; // Unique identifier for each investor instance
    private int transactionsCounter = 0; // Counter for transactions made by the investor

    /**
     * Constructor for Investor class.
     * @param portfolio The portfolio associated with the investor.
     */
    public Investor(Portfolio portfolio) {
        this.portfolio = portfolio;
        this.nr = investorCounter++;
    }

    /**
     * Abstract method that decides on trading actions in each turn of the simulation.
     * @param turn The current turn of the simulation.
     * @param orderBook The order book containing market data.
     */
    public abstract void decide(int turn, OrderBook orderBook);

    /**
     * Getter for the investor's portfolio.
     * @return The portfolio of the investor.
     */
    public Portfolio getPortfolio() {
        return portfolio;
    }

    /**
     * Getter for the unique identifier of the investor.
     * @return The identifier number of the investor.
     */
    public int getNr() {
        return nr;
    }

    /**
     * Method to increment the transaction counter.
     * Used to count the number of transactions made by the investor.
     */
    public void incrementTransactionCounter() {
        transactionsCounter++;
    }

    /**
     * Getter for the transaction counter.
     * @return The number of transactions made by the investor.
     */
    public int getTransactionsCounter() {
        return transactionsCounter;
    }
}
