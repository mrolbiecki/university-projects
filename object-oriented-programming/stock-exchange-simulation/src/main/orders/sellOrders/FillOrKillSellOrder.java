package main.orders.sellOrders;

import main.OrderBook;
import main.investors.Investor;
import main.orders.buyOrders.BuyOrder;

import java.util.SortedSet;

/**
 * Represents a Fill or Kill Sell Order, which must be executed immediately or canceled entirely.
 */
public class FillOrKillSellOrder extends SellOrder {
    public FillOrKillSellOrder(String stockId, int quantity, int price, Investor investor, OrderBook orderBook) {
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
        int totalSold = 0;
        for (BuyOrder buyOrder : buyOrders) {
            if (buyOrder.getPrice() >= this.getPrice()) {
                totalSold += buyOrder.getQuantity();
            }
            if (totalSold >= this.getQuantity()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "FillOrKillSellOrder " + super.toString();
    }
}
