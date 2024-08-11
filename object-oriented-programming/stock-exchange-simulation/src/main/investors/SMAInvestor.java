package main.investors;

import main.OrderBook;
import main.Portfolio;
import main.Simulation;
import main.orders.buyOrders.GoodTillDateBuyOrder;
import main.orders.sellOrders.GoodTillDateSellOrder;

import java.util.*;

public class SMAInvestor extends Investor {
    Map<String, Queue<Integer>> SMA5Queues, SMA10Queues;
    Map<String, Integer> SMA5Sums, SMA10Sums;
    Map<String, Double> previousSMA5Values, previousSMA10Values;

    public SMAInvestor(Portfolio portfolio) {
        super(portfolio);
        SMA5Queues = new TreeMap<>();
        SMA5Sums = new TreeMap<>();
        SMA10Queues = new TreeMap<>();
        SMA10Sums = new TreeMap<>();
        previousSMA5Values = new TreeMap<>();
        previousSMA10Values = new TreeMap<>();
    }

    /**
     * Updates the SMA5 values based on the latest prices in the order book.
     * @param orderBook The current order book.
     */
    public void updateSMA5(OrderBook orderBook) {
        ArrayList<String> stocks = orderBook.getStocks();
        for (String stock : stocks) {
            int lastPrice = orderBook.getLastTransactionPrice(stock);
            Queue<Integer> queue = SMA5Queues.getOrDefault(stock, new LinkedList<>());
            int sum = SMA5Sums.getOrDefault(stock, 0) + lastPrice;

            if (queue.size() >= 5) {
                sum -= queue.poll();
            }

            queue.add(lastPrice);
            SMA5Queues.put(stock, queue);
            SMA5Sums.put(stock, sum);
        }
    }

    /**
     * Updates the SMA10 values based on the latest prices in the order book.
     * @param orderBook The current order book.
     */
    public void updateSMA10(OrderBook orderBook) {
        ArrayList<String> stocks = orderBook.getStocks();
        for (String stock : stocks) {
            int lastPrice = orderBook.getLastTransactionPrice(stock);
            Queue<Integer> queue = SMA10Queues.getOrDefault(stock, new LinkedList<>());
            int sum = SMA10Sums.getOrDefault(stock, 0) + lastPrice;

            if (queue.size() >= 10) {
                sum -= queue.poll();
            }

            queue.add(lastPrice);
            SMA10Queues.put(stock, queue);
            SMA10Sums.put(stock, sum);
        }
    }

    /**
     * Makes buy or sell decisions based on SMA values.
     * @param turn The current turn.
     * @param orderBook The current order book.
     */
    public void decide(int turn, OrderBook orderBook) {
        updateSMA5(orderBook);
        updateSMA10(orderBook);
        ArrayList<String> stocks = orderBook.getStocks();
        ArrayList<String> toBuy = new ArrayList<>();
        ArrayList<String> toSell = new ArrayList<>();

        for (String stock : stocks) {
            double sma5 = (double) SMA5Sums.get(stock) / Math.min(5, SMA5Queues.get(stock).size());
            double sma10 = (double) SMA10Sums.get(stock) / Math.min(10, SMA10Queues.get(stock).size());
            double previousSma5 = previousSMA5Values.getOrDefault(stock, sma5);
            double previousSma10 = previousSMA10Values.getOrDefault(stock, sma10);

            if (turn >= 10) {
                if (previousSma5 < previousSma10 && sma5 >= sma10) {
                    if (Simulation.displayData) {
                        System.out.println("Buy signal for stock: " + stock);
                    }
                    toBuy.add(stock);
                } else if (previousSma5 > previousSma10 && sma5 <= sma10) {
                    if (Simulation.displayData) {
                        System.out.println("Sell signal for stock: " + stock);
                    }
                    toSell.add(stock);
                }
            }

            previousSMA5Values.put(stock, sma5);
            previousSMA10Values.put(stock, sma10);
        }

        // alternate between buying and selling
        if (!toBuy.isEmpty() && turn % 2 == 0) {
            String stockID = toBuy.get(turn % toBuy.size());
            int lastPrice = orderBook.getLastTransactionPrice(stockID);
            int quantity = Math.max(1, getPortfolio().getCash() / lastPrice / 2);
            orderBook.newBuyOrder(new GoodTillDateBuyOrder(stockID, quantity, lastPrice, this, orderBook, turn + 10), turn);
        } else if (!toSell.isEmpty()) {
            String stockID = toSell.get(turn % toSell.size());
            int lastPrice = orderBook.getLastTransactionPrice(stockID);
            int quantity = Math.max(1, getPortfolio().getStockQuantity(stockID) / 2);
            orderBook.newSellOrder(new GoodTillDateSellOrder(stockID, quantity, lastPrice, this, orderBook, turn + 10), turn);
        }
    }

    @Override
    public String toString() {
        return "SMA Investor " + getNr();
    }
}
