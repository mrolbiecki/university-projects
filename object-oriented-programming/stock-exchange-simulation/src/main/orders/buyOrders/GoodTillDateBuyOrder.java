package main.orders.buyOrders;

import main.OrderBook;
import main.investors.Investor;
import main.orders.sellOrders.SellOrder;

import java.util.SortedSet;

/**
 * Represents a Good Till Date Buy Order, which remains active until a specified turn (expiryTurn).
 */
public class GoodTillDateBuyOrder extends BuyOrder {
    private final int expiryTurn;

    public GoodTillDateBuyOrder(String stockId, int quantity, int price, Investor investor, OrderBook orderBook, int expiryTurn) {
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
    public boolean canBeExecuted(SortedSet<SellOrder> sellOrders) {
        return true;
    }

    @Override
    public String toString() {
        return "GoodTillDateBuyOrder: " + super.toString();
    }
}
