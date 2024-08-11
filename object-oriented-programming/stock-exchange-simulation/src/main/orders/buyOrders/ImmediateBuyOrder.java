package main.orders.buyOrders;

import main.OrderBook;
import main.investors.Investor;
import main.orders.sellOrders.SellOrder;

import java.util.SortedSet;

/**
 * Represents an Immediate Buy Order, which must be executed immediately or canceled.
 */
public class ImmediateBuyOrder extends BuyOrder {
    public ImmediateBuyOrder(String stockId, int quantity, int price, Investor investor, OrderBook orderBook) {
        super(stockId, quantity, price, investor, orderBook);
    }

    @Override
    public boolean canWait() {
        return false;
    }

    @Override
    public boolean isExpired(int turn) {
        return false;
    }

    @Override
    public boolean canBeExecuted(SortedSet<SellOrder> sellOrders) {
        return true;
    }

    @Override
    public String toString() {
        return "ImmediateBuyOrder: " + super.toString();
    }
}
