package main.orders.sellOrders;

import main.OrderBook;
import main.investors.Investor;
import main.orders.buyOrders.BuyOrder;

import java.util.SortedSet;

/**
 * Represents an Immediate Sell Order, which must be executed immediately or canceled.
 */
public class ImmediateSellOrder extends SellOrder {
    public ImmediateSellOrder(String stockId, int quantity, int price, Investor investor, OrderBook orderBook) {
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
    public boolean canBeExecuted(SortedSet<BuyOrder> buyOrders) {
        return true;
    }

    @Override
    public String toString() {
        return "ImmediateSellOrder " + super.toString();
    }
}
