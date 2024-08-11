package main.orders.sellOrders;

import main.OrderBook;
import main.investors.Investor;
import main.orders.buyOrders.BuyOrder;

import java.util.SortedSet;

/**
 * Represents a Good Till Cancelled Sell Order, which remains active until canceled.
 */
public class GoodTillCancelledSellOrder extends SellOrder {
    public GoodTillCancelledSellOrder(String stockId, int quantity, int price, Investor investor, OrderBook orderBook) {
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
    public boolean canBeExecuted(SortedSet<BuyOrder> buyOrders) {
        return true;
    }

    @Override
    public String toString() {
        return "GoodTillCancelledSellOrder " + super.toString();
    }
}
