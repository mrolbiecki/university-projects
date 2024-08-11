package main;

import main.orders.buyOrders.BuyOrder;
import main.orders.sellOrders.SellOrder;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class OrderBook {
    private final Map<String, Integer> lastTransactionPrice; // Last transaction prices for each stock
    private final Map<String, Integer> lastTransactionTurn; // Last transaction turns for each stock
    private final Map<String, OrderBookEntry> orderBookEntries; // Order book entries for each stock

    /**
     * Constructor for OrderBook.
     * Initializes last transaction prices and turns for each stock.
     * @param stocks Map of stock IDs to their last transaction prices.
     */
    public OrderBook(Map<String, Integer> stocks) {
        lastTransactionPrice = stocks;
        lastTransactionTurn = new TreeMap<>();
        for (String stockId : stocks.keySet()) {
            lastTransactionTurn.put(stockId, 0);
        }
        orderBookEntries = new TreeMap<>();
        for (String stockId : stocks.keySet()) {
            orderBookEntries.put(stockId, new OrderBookEntry());
        }
    }

    /**
     * Adds a new buy order to the order book.
     * @param buyOrder The buy order to add.
     * @param turn The current turn.
     */
    public void newBuyOrder(BuyOrder buyOrder, int turn) {
        if (Simulation.displayData) {
            System.out.println("New buyOrder: " + buyOrder);
        }
        Portfolio portfolio = buyOrder.getInvestor().getPortfolio();
        if (portfolio.getCash() < buyOrder.getPrice() * buyOrder.getQuantity()) {
            if (Simulation.displayData) {
                System.out.println("Investor " + buyOrder.getInvestor() + " did not have enough cash, order cancelled.");
            }
            return;
        }
        portfolio.updateCash(-buyOrder.getPrice() * buyOrder.getQuantity());
        orderBookEntries.get(buyOrder.getStockId()).addBuyOrder(buyOrder, turn);
        if (Simulation.displayData) {
            System.out.println();
        }
    }

    /**
     * Adds a new sell order to the order book.
     * @param sellOrder The sell order to add.
     * @param turn The current turn.
     */
    public void newSellOrder(SellOrder sellOrder, int turn) {
        if (Simulation.displayData) {
            System.out.println("New sellOrder: " + sellOrder);
        }
        Portfolio portfolio = sellOrder.getInvestor().getPortfolio();
        if (portfolio.getStockQuantity(sellOrder.getStockId()) < sellOrder.getQuantity()) {
            if (Simulation.displayData) {
                System.out.println("Investor " + sellOrder.getInvestor() + " did not have enough stock, order cancelled.");
            }
            return;
        }
        portfolio.updateStockQuantity(sellOrder.getStockId(), -sellOrder.getQuantity());
        orderBookEntries.get(sellOrder.getStockId()).addSellOrder(sellOrder, turn);
        if (Simulation.displayData) {
            System.out.println();
        }
    }

    /**
     * Deletes expired and empty orders from the order book.
     * @param turn The current turn.
     */
    public void deleteExpiredAndEmptyOrders(int turn) {
        for (OrderBookEntry entry : orderBookEntries.values()) {
            entry.deleteExpiredAndEmptyOrders(turn);
        }
    }

    /**
     * Cancels all orders in the order book.
     */
    public void cancelAllOrders() {
        for (OrderBookEntry entry : orderBookEntries.values()) {
            entry.cancelAllOrders();
        }
    }

    /**
     * Gets the last transaction price for a stock.
     * @param stockId The stock ID.
     * @return The last transaction price.
     */
    public int getLastTransactionPrice(String stockId) {
        return lastTransactionPrice.get(stockId);
    }

    /**
     * Gets the last transaction turn for a stock.
     * @param stockId The stock ID.
     * @return The last transaction turn.
     */
    public int getLastTransactionTurn(String stockId) {
        return lastTransactionTurn.get(stockId);
    }

    /**
     * Updates the last transaction price for a stock.
     * @param stockId The stock ID.
     * @param price The new transaction price.
     */
    public void updateLastTransactionPrice(String stockId, int price) {
        lastTransactionPrice.put(stockId, price);
    }

    /**
     * Updates the last transaction turn for a stock.
     * @param stockId The stock ID.
     * @param turn The new transaction turn.
     */
    public void updateLastTransactionTurn(String stockId, int turn) {
        lastTransactionTurn.put(stockId, turn);
    }

    /**
     * Gets a list of all stock IDs in the order book.
     * @return A list of stock IDs.
     */
    public ArrayList<String> getStocks() {
        return new ArrayList<>(lastTransactionPrice.keySet());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String stockId : orderBookEntries.keySet()) {
            OrderBookEntry entry = orderBookEntries.get(stockId);
            sb.append(stockId);
            sb.append(": \n");
            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }
}
