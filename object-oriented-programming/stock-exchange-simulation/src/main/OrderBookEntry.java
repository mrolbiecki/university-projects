package main;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import main.orders.buyOrders.BuyOrder;
import main.orders.sellOrders.SellOrder;

public class OrderBookEntry {
    private final SortedSet<BuyOrder> buyOrders; // Set of buy orders sorted by price
    private final SortedSet<SellOrder> sellOrders; // Set of sell orders sorted by price

    /**
     * Constructor for OrderBookEntry.
     * Initializes the sorted sets for buy and sell orders.
     */
    public OrderBookEntry() {
        buyOrders = new TreeSet<>();
        sellOrders = new TreeSet<>();
    }

    /**
     * Adds a new buy order to the order book entry.
     * If the order can be executed immediately, it is executed.
     * If not, it is added to the set of buy orders.
     * @param buyOrder The buy order to add.
     * @param turn The current turn.
     */
    public void addBuyOrder(BuyOrder buyOrder, int turn) {
        if (buyOrder.canBeExecuted(sellOrders)) {
            if (Simulation.displayData) {
                System.out.println("Executing order...");
            }
            buyOrder.execute(sellOrders, turn);
        }
        if (!buyOrder.isFullyExecuted() && buyOrder.canWait()) {
            if (Simulation.displayData) {
                System.out.println("Order added to the list.");
            }
            buyOrders.add(buyOrder);
        } else {
            buyOrder.cancel();
        }
    }

    /**
     * Adds a new sell order to the order book entry.
     * If the order can be executed immediately, it is executed.
     * If not, it is added to the set of sell orders.
     * @param sellOrder The sell order to add.
     * @param turn The current turn.
     */
    public void addSellOrder(SellOrder sellOrder, int turn) {
        if (sellOrder.canBeExecuted(buyOrders)) {
            if (Simulation.displayData) {
                System.out.println("Executing order...");
            }
            sellOrder.execute(buyOrders, turn);
        }
        if (!sellOrder.isFullyExecuted() && sellOrder.canWait()) {
            if (Simulation.displayData) {
                System.out.println("Order added to the list.");
            }
            sellOrders.add(sellOrder);
        } else {
            sellOrder.cancel();
        }
    }

    /**
     * Deletes expired and empty orders from the order book entry.
     * @param turn The current turn.
     */
    public void deleteExpiredAndEmptyOrders(int turn) {
        Iterator<BuyOrder> buyOrderIterator = buyOrders.iterator();
        while (buyOrderIterator.hasNext()) {
            BuyOrder buyOrder = buyOrderIterator.next();
            if (buyOrder.isFullyExecuted() || buyOrder.isExpired(turn)) {
                if (Simulation.displayData) {
                    System.out.println("Order " + buyOrder + " is expired or empty and was deleted.");
                }
                buyOrder.cancel();
                buyOrderIterator.remove();
            }
        }
        Iterator<SellOrder> sellOrderIterator = sellOrders.iterator();
        while (sellOrderIterator.hasNext()) {
            SellOrder sellOrder = sellOrderIterator.next();
            if (sellOrder.isFullyExecuted() || sellOrder.isExpired(turn)) {
                if (Simulation.displayData) {
                    System.out.println("Order " + sellOrder + " is expired or empty and was deleted.");
                }
                sellOrder.cancel();
                sellOrderIterator.remove();
            }
        }
    }

    /**
     * Cancels all orders in the order book entry.
     */
    public void cancelAllOrders() {
        Iterator<BuyOrder> buyOrderIterator = buyOrders.iterator();
        while (buyOrderIterator.hasNext()) {
            BuyOrder buyOrder = buyOrderIterator.next();
            buyOrder.cancel();
            buyOrderIterator.remove();
        }
        Iterator<SellOrder> sellOrderIterator = sellOrders.iterator();
        while (sellOrderIterator.hasNext()) {
            SellOrder sellOrder = sellOrderIterator.next();
            sellOrder.cancel();
            sellOrderIterator.remove();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("buyOrders: \n");
        for (BuyOrder buyOrder : buyOrders) {
            sb.append(buyOrder.toString()).append("\n");
        }
        sb.append("sellOrders: \n");
        for (SellOrder sellOrder : sellOrders) {
            sb.append(sellOrder.toString()).append("\n");
        }
        return sb.toString();
    }
}
