package main.orders;

import main.OrderBook;
import main.investors.Investor;

/**
 * Abstract class representing an order in the trading simulation.
 */
public abstract class Order {
    private static int orderCounter = 0;
    private final int id;
    private final String stockId;
    private int quantity;
    private final int price;
    private final Investor investor;
    private final OrderBook orderBook;

    /**
     * Constructor for creating an Order object.
     * @param stockId The ID of the stock associated with this order.
     * @param quantity The quantity of stock to be bought or sold.
     * @param price The price at which the order is placed.
     * @param investor The investor placing the order.
     * @param orderBook The order book where this order is placed.
     */
    public Order(String stockId, int quantity, int price, Investor investor, OrderBook orderBook) {
        this.id = orderCounter++;
        this.stockId = stockId;
        this.quantity = quantity;
        this.price = price;
        this.investor = investor;
        this.orderBook = orderBook;
    }

    /**
     * Abstract method to determine if the order can wait to be executed.
     * @return true if the order can wait, false otherwise.
     */
    public abstract boolean canWait();

    /**
     * Abstract method to determine if the order has expired.
     * @param turn The current turn of the simulation.
     * @return true if the order has expired, false otherwise.
     */
    public abstract boolean isExpired(int turn);

    /**
     * Checks if the order has been fully executed (quantity reduced to zero).
     * @return true if the order is fully executed, false otherwise.
     */
    public boolean isFullyExecuted() {
        return quantity == 0;
    }

    /**
     * Getter for the price of the order.
     * @return The price of the order.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Getter for the ID of the order.
     * @return The ID of the order.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the stock ID associated with the order.
     * @return The stock ID.
     */
    public String getStockId() {
        return stockId;
    }

    /**
     * Getter for the quantity of stock in the order.
     * @return The quantity of stock in the order.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Setter for the quantity of stock in the order.
     * @param quantity The new quantity of stock in the order.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Getter for the investor who placed the order.
     * @return The investor who placed the order.
     */
    public Investor getInvestor() {
        return investor;
    }

    /**
     * Getter for the order book where the order is placed.
     * @return The order book where the order is placed.
     */
    public OrderBook getOrderBook() {
        return orderBook;
    }

    /**
     * String representation of the order, primarily for debugging purposes.
     * @return A string describing the order.
     */
    @Override
    public String toString() {
        return "Id: " + id + ", Stock Id: " + stockId + ", Quantity: " + quantity + ", Price: " + price;
    }
}
