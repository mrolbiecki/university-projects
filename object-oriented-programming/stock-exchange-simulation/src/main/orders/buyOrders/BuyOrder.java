package main.orders.buyOrders;

import main.OrderBook;
import main.Simulation;
import main.investors.Investor;
import main.orders.Order;
import main.orders.sellOrders.SellOrder;

import java.util.SortedSet;

public abstract class BuyOrder extends Order implements Comparable<BuyOrder> {
    public BuyOrder(String stockId, int quantity, int price, Investor investor, OrderBook orderBook) {
        super(stockId, quantity, price, investor, orderBook);
    }

    /**
     * Checks if the buy order can be executed given the set of sell orders.
     * @param sellOrders The set of sell orders.
     * @return True if the buy order can be executed, false otherwise.
     */
    public abstract boolean canBeExecuted(SortedSet<SellOrder> sellOrders);

    /**
     * Cancels the buy order and refunds the investor.
     */
    public void cancel() {
        getInvestor().getPortfolio().updateCash(getQuantity() * getPrice());
        setQuantity(0);
    }

    /**
     * Executes the buy order using the given set of sell orders.
     * @param sellOrders The set of sell orders.
     * @param turn The current turn.
     */
    public void execute(SortedSet<SellOrder> sellOrders, int turn) {
        for (SellOrder sellOrder : sellOrders) {
            int buyAmount = Math.min(getQuantity(), sellOrder.getQuantity());
            if (sellOrder.getPrice() <= this.getPrice()) {
                sellOrder.transaction(buyAmount, sellOrder.getPrice(), turn);
                transaction(buyAmount, getPrice() - sellOrder.getPrice(), getStockId());
                if (Simulation.displayData) {
                    System.out.println("Transaction between: " + this.getInvestor() + " and " + sellOrder.getInvestor());
                    System.out.println("Stock: " + getStockId() + " quantity: " + buyAmount + " price: " + sellOrder.getPrice());
                }
            }
            if (isFullyExecuted()) {
                break;
            }
        }
    }

    /**
     * Compares this buy order with another buy order for sorting purposes.
     * @param o The other buy order.
     * @return A negative integer, zero, or a positive integer as this buy order is less than, equal to, or greater than the specified buy order.
     */
    public int compareTo(BuyOrder o) {
        if (this.getPrice() == o.getPrice()) {
            return this.getId() - o.getId();
        }
        return o.getPrice() - this.getPrice(); // Higher prices have higher priority
    }

    /**
     * Updates the transaction details for the buy order.
     * @param quantity The quantity of stocks bought.
     * @param stockId The stock ID.
     */
    public void transaction(int quantity, int priceDifference, String stockId) {
        Investor investor = getInvestor();
        investor.incrementTransactionCounter();
        investor.getPortfolio().updateStockQuantity(stockId, quantity);
        investor.getPortfolio().updateCash(quantity * priceDifference);
        setQuantity(getQuantity() - quantity);
    }
}
