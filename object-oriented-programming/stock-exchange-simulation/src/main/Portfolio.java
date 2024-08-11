package main;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a portfolio containing stocks and cash.
 */
public class Portfolio {
    private final Map<String, Integer> stocks; // Map of stocks and their quantities
    private int cash; // Amount of cash in the portfolio

    /**
     * Constructor for Portfolio class.
     * @param cash Initial amount of cash in the portfolio.
     * @param stocks Array of stock IDs.
     * @param quantities Array of quantities corresponding to each stock ID.
     */
    public Portfolio(int cash, String[] stocks, int[] quantities) {
        this.cash = cash;
        this.stocks = new HashMap<>();
        for (int i = 0; i < stocks.length; i++) {
            this.stocks.put(stocks[i], quantities[i]);
        }
    }

    /**
     * Copy constructor for Portfolio class.
     * @param portfolio Another Portfolio object to copy.
     */
    public Portfolio(Portfolio portfolio) {
        this.cash = portfolio.cash;
        this.stocks = new HashMap<>();
        this.stocks.putAll(portfolio.stocks);
    }

    /**
     * Method to update the cash amount in the portfolio.
     * @param amount Amount of cash to add (can be negative for subtraction).
     */
    public void updateCash(int amount) {
        cash += amount;
    }

    /**
     * Getter for the current cash amount in the portfolio.
     * @return The current cash amount.
     */
    public int getCash() {
        return cash;
    }

    /**
     * Getter for the quantity of a specific stock in the portfolio.
     * @param stockId ID of the stock.
     * @return Quantity of the specified stock.
     */
    public int getStockQuantity(String stockId) {
        return stocks.getOrDefault(stockId, 0);
    }

    /**
     * Method to update the quantity of a specific stock in the portfolio.
     * @param stockId ID of the stock.
     * @param quantity Quantity to add (can be negative for subtraction).
     */
    public void updateStockQuantity(String stockId, int quantity) {
        int currentQuantity = stocks.getOrDefault(stockId, 0);
        stocks.put(stockId, currentQuantity + quantity);
    }

    /**
     * Method to calculate the estimated net worth of the portfolio.
     * It includes the current cash and the total value of all stocks based on the last transaction prices.
     * @param orderBook OrderBook object containing last transaction prices for stocks.
     * @return Estimated net worth of the portfolio.
     */
    public int getEstimatedNetWorth(OrderBook orderBook) {
        int sum = 0;
        for (Map.Entry<String, Integer> entry : stocks.entrySet()) {
            String stockId = entry.getKey();
            int quantity = entry.getValue();
            sum += quantity * orderBook.getLastTransactionPrice(stockId);
        }
        return sum + cash;
    }

    /**
     * Getter for the map of stocks.
     * @return Map of stocks.
     */
    public Map<String, Integer> getStocks() {
        return stocks;
    }

    /**
     * Method to provide a string representation of the portfolio.
     * @return String representation of the portfolio.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cash: ").append(cash).append("\n");
        for (Map.Entry<String, Integer> entry : stocks.entrySet()) {
            sb.append(entry.getKey()).append(" x ").append(entry.getValue()).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1); // Remove the last newline character
        return sb.toString();
    }
}
