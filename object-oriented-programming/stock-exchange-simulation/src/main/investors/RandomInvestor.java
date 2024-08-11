package main.investors;

import main.OrderBook;
import main.Portfolio;
import main.orders.buyOrders.*;
import main.orders.sellOrders.*;

import java.util.ArrayList;
import java.util.Random;

public class RandomInvestor extends Investor {

    public RandomInvestor(Portfolio portfolio) {
        super(portfolio);
    }

    /**
     * Makes a random decision to buy or sell stocks based on random criteria.
     * Uses different order types (Immediate, Fill or Kill, Good Till Date) with random parameters.
     *
     * @param turn      Current turn of the simulation.
     * @param orderBook The order book containing current market data.
     */
    public void decide(int turn, OrderBook orderBook) {
        Random rand = new Random();
        ArrayList<String> stocks = orderBook.getStocks();

        // Randomly selects a stock to trade
        String stockId = stocks.get(rand.nextInt(stocks.size()));
        int lastPrice = orderBook.getLastTransactionPrice(stockId);

        // Randomly determines the price of the order, within a range around the last price
        int price = Math.max(1, rand.nextInt(21) + lastPrice - 10);

        // Randomly determines an expiry turn for the order (within 30 turns from the current turn)
        int expiryTurn = rand.nextInt(30) + turn;

        // Randomly decides whether to buy or sell, and calculates the quantity
        int r = rand.nextInt(6);
        int quantity;
        if (r <= 2) {
            quantity = Math.max(1, rand.nextInt(getPortfolio().getCash() / price + 1));
        } else {
            quantity = Math.max(1, rand.nextInt(getPortfolio().getStockQuantity(stockId) + 1));
        }

        // Creates an order based on the random decision
        switch (r) {
            case 0:
                orderBook.newBuyOrder(new ImmediateBuyOrder(stockId, quantity, price, this, orderBook), turn);
                break;
            case 1:
                orderBook.newBuyOrder(new FillOrKillBuyOrder(stockId, quantity, price, this, orderBook), turn);
                break;
            case 2:
                orderBook.newBuyOrder(new GoodTillDateBuyOrder(stockId, quantity, price, this, orderBook, expiryTurn), turn);
                break;
            case 3:
                orderBook.newSellOrder(new ImmediateSellOrder(stockId, quantity, price, this, orderBook), turn);
                break;
            case 4:
                orderBook.newSellOrder(new FillOrKillSellOrder(stockId, quantity, price, this, orderBook), turn);
                break;
            case 5:
                orderBook.newSellOrder(new GoodTillDateSellOrder(stockId, quantity, price, this, orderBook, expiryTurn), turn);
                break;
        }
    }

    @Override
    public String toString() {
        return "Random Investor " + getNr();
    }
}
