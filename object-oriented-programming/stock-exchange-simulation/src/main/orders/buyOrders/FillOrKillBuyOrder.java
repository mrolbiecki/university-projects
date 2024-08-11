package main.orders.buyOrders;

import main.OrderBook;
import main.investors.Investor;
import main.orders.sellOrders.SellOrder;

import java.util.SortedSet;

/**
 * Represents a Fill or Kill Buy Order, which must be executed immediately or canceled entirely.
 */
public class FillOrKillBuyOrder extends BuyOrder {
    public FillOrKillBuyOrder(String stockId, int quantity, int price, Investor investor, OrderBook orderBook) {
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
        int totalBought = 0;
        for (SellOrder sellOrder : sellOrders) {
            if (sellOrder.getPrice() <= this.getPrice()) {
                totalBought += sellOrder.getQuantity();
            }
            if (totalBought >= this.getQuantity()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "FillOrKillBuyOrder: " + super.toString();
    }
}
