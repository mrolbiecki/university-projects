package main.orders.sellOrders;

import main.OrderBook;
import main.investors.Investor;
import main.orders.buyOrders.BuyOrder;

import java.util.SortedSet;

/**
 * Represents a Good Till Date Sell Order, which remains active until a specified turn (expiryTurn).
 */
public class GoodTillDateSellOrder extends SellOrder {
    private final int expiryTurn;

    public GoodTillDateSellOrder(String stockId, int quantity, int price, Investor investor, OrderBook orderBook, int expiryTurn) {
        super(stockId, quantity, price, investor, orderBook);
        this.expiryTurn = expiryTurn;
    }

    @Override
    public boolean canWait() {
        return true;
    }

    @Override
    public boolean isExpired(int turn) {
        return turn >= expiryTurn;
    }

    @Override
    public boolean canBeExecuted(SortedSet<BuyOrder> buyOrders) {
        return true;
    }

    @Override
    public String toString() {
        return "GoodTillDateSellOrder " + super.toString();
    }
}
