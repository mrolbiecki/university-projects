package main.orders.sellOrders;

import main.OrderBook;
import main.Simulation;
import main.investors.Investor;
import main.orders.Order;
import main.orders.buyOrders.BuyOrder;

import java.util.SortedSet;

public abstract class SellOrder extends Order implements Comparable<SellOrder> {
    public SellOrder(String stockId, int quantity, int price, Investor investor, OrderBook orderBook) {
        super(stockId, quantity, price, investor, orderBook);
    }

    /**
     * Checks if the sell order can be executed given the set of buy orders.
     * @param buyOrders The set of buy orders.
     * @return True if the sell order can be executed, false otherwise.
     */
    public abstract boolean canBeExecuted(SortedSet<BuyOrder> buyOrders);

    /**
     * Cancels the sell order and returns the stocks to the investor.
     */
    public void cancel() {
        getInvestor().getPortfolio().updateStockQuantity(getStockId(), getQuantity());
        setQuantity(0);
    }

    /**
     * Executes the sell order using the given set of buy orders.
     * @param buyOrders The set of buy orders.
     * @param turn The current turn.
     */
    public void execute(SortedSet<BuyOrder> buyOrders, int turn) {
        for (BuyOrder buyOrder : buyOrders) {
            int sellAmount = Math.min(getQuantity(), buyOrder.getQuantity());
            if (buyOrder.getPrice() >= this.getPrice()) {
                buyOrder.transaction(sellAmount, 0, getStockId());
                transaction(sellAmount, buyOrder.getPrice(), turn);
                if (Simulation.displayData) {
                    System.out.println("Transaction between: " + this.getInvestor() + " and " + buyOrder.getInvestor());
                    System.out.println("Stock: " + getStockId() + " quantity: " + sellAmount + " price: " + buyOrder.getPrice());
                }
            }
            if (isFullyExecuted()) {
                break;
            }
        }
    }

    /**
     * Compares this sell order with another sell order for sorting purposes.
     * @param o The other sell order.
     * @return A negative integer, zero, or a positive integer as this sell order is less than, equal to, or greater than the specified sell order.
     */
    public int compareTo(SellOrder o) {
        if (this.getPrice() == o.getPrice()) {
            return this.getId() - o.getId();
        }
        return this.getPrice() - o.getPrice(); // Lower prices have higher priority
    }

    /**
     * Updates the transaction details for the sell order.
     * @param quantity The quantity of stocks sold.
     * @param price The price at which the stocks were sold.
     * @param turn The current turn.
     */
    public void transaction(int quantity, int price, int turn) {
        this.getInvestor().incrementTransactionCounter();
        getInvestor().getPortfolio().updateCash(quantity * price);
        setQuantity(getQuantity() - quantity);
        getOrderBook().updateLastTransactionPrice(getStockId(), price);
        getOrderBook().updateLastTransactionTurn(getStockId(), turn);
    }
}
