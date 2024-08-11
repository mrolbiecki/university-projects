package main;

import main.investors.Investor;
import main.investors.RandomInvestor;
import main.investors.SMAInvestor;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class responsible for parsing input data to initialize a Simulation.
 */
public class InputParser {
    private final String path;

    /**
     * Constructor for InputParser class.
     * @param path Path to the input file.
     */
    public InputParser(String path) {
        this.path = path;
    }

    /**
     * Parses the input file and creates a Simulation object.
     * @param turns Number of turns for the simulation.
     * @return Initialized Simulation object.
     * @throws IOException If there's an error reading or parsing the input file.
     */
    public Simulation parse(int turns) throws IOException {
        Scanner scanner = new Scanner(new File(path));
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.charAt(0) != '#') {
                lines.add(line);
            }
        }
        if (lines.size() != 3) {
            throw new IOException("Invalid file format");
        }
        Map<String, Integer> stocks = parseStocks(lines.get(1));
        Portfolio startingPortfolio = parseStartingPortfolio(lines.get(2), stocks);
        ArrayList<Investor> investors = parseInvestors(lines.get(0), startingPortfolio);
        return new Simulation(investors, stocks, turns);
    }

    /**
     * Parses the investor line and creates Investor objects based on the tokens.
     * @param line Line containing investor tokens.
     * @param startingPortfolio Starting portfolio for each investor.
     * @return List of Investor objects.
     */
    private ArrayList<Investor> parseInvestors(String line, Portfolio startingPortfolio) {
        String[] tokens = line.split(" ");
        ArrayList<Investor> investors = new ArrayList<>();
        for (String token : tokens) {
            if (token.equals("R")) {
                investors.add(new RandomInvestor(new Portfolio(startingPortfolio)));
            } else if (token.equals("S")) {
                investors.add(new SMAInvestor(new Portfolio(startingPortfolio)));
            } else {
                throw new RuntimeException("Invalid investor: " + token);
            }
        }
        return investors;
    }

    /**
     * Parses the line containing initial stocks and quantities.
     * @param line Line containing stocks and their initial quantities.
     * @return Map of stocks and their initial quantities.
     */
    private Map<String, Integer> parseStocks(String line) {
        Map<String, Integer> stocks = new TreeMap<>();
        String[] tokens = line.split(" ");
        for (String token : tokens) {
            String[] params = token.split(":");
            stocks.put(params[0], Integer.parseInt(params[1]));
        }
        return stocks;
    }

    /**
     * Parses the line containing the starting portfolio data.
     * @param line Line containing cash and initial stocks with quantities.
     * @param stocks Map of stocks and their initial quantities.
     * @return Initialized Portfolio object representing the starting portfolio.
     * @throws IOException If a stock in the starting portfolio is not found in the stock list.
     */
    private Portfolio parseStartingPortfolio(String line, Map<String, Integer> stocks) throws IOException {
        String[] tokens = line.split(" ");
        int cash = Integer.parseInt(tokens[0]);
        String[] startingStocks = new String[tokens.length - 1];
        int[] quantities = new int[tokens.length - 1];
        for (int i = 0; i < tokens.length - 1; i++) {
            String[] params = tokens[i + 1].split(":");
            startingStocks[i] = params[0];
            if (!stocks.containsKey(startingStocks[i])) {
                throw new IOException("Stock " + startingStocks[i] + " in starting portfolio is not on stock list.");
            }
            quantities[i] = Integer.parseInt(params[1]);
        }
        return new Portfolio(cash, startingStocks, quantities);
    }

}
