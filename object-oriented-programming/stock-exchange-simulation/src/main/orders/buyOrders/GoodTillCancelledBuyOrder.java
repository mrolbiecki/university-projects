package main.orders.buyOrders;

import main.OrderBook;
import main.investors.Investor;
import main.orders.sellOrders.SellOrder;

import java.util.SortedSet;

/**
 * Represents a Good Till Cancelled Buy Order, which remains active until canceled.
 */
public class GoodTillCancelledBuyOrder extends BuyOrder {
    public GoodTillCancelledBuyOrder(String stockId, int quantity, int price, Investor investor, OrderBook orderBook) {
        super(stockId, quantity, price, investor, orderBook);
    }

    @Override
    public boolean canWait() {
        return true;
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
        return "GoodTillCancelledBuyOrder: " + super.toString();
    }
}
