package test;

import main.InputParser;
import main.Simulation;
import main.investors.Investor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/example_input.txt, 100000", // Example test case
            "src/test/resources/simple_test.txt, 100", // Two investors and 100 turns
            "src/test/resources/max_test.txt, 100000", // 20 investors and 1,000,000 turns
    })
    public void testSimulationInvariant(String path, int turns) {
        InputParser inputParser = new InputParser(path);

        try {
            Simulation simulation = inputParser.parse(turns);
            ArrayList<Investor> investors = simulation.getInvestors();

            // Record initial total cash and stock quantities across all portfolios
            int initialTotalCash = getTotalCash(investors);
            int initialTotalStocks = getTotalStocks(investors);

            // Run simulation
            simulation.startSimulation();

            // Record final total cash and stock quantities across all portfolios
            int finalTotalCash = getTotalCash(investors);
            int finalTotalStocks = getTotalStocks(investors);

            // Assert invariant: total cash and stocks should remain constant
            assertEquals(initialTotalCash, finalTotalCash, "Total cash invariant violated");
            assertEquals(initialTotalStocks, finalTotalStocks, "Total stocks invariant violated");

        } catch (IOException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/nonexistent_file.txt, 100", // Non-existent input file
            "src/test/resources/invalid_format.txt, 100", // Missing information in input
            "src/test/resources/invalid_portfolio.txt, 100", // Invalid portfolio
    })
    public void testInvalidInput(String path, int turns) {
        InputParser inputParser = new InputParser(path);

        assertThrows(IOException.class, () -> inputParser.parse(turns));
    }

    private int getTotalCash(ArrayList<Investor> investors) {
        int totalCash = 0;
        for (Investor investor : investors) {
            totalCash += investor.getPortfolio().getCash();
        }
        return totalCash;
    }

    private int getTotalStocks(ArrayList<Investor> investors) {
        int totalStocks = 0;
        for (Investor investor : investors) {
            Map<String, Integer> stocks = investor.getPortfolio().getStocks();
            for (int quantity : stocks.values()) {
                totalStocks += quantity;
            }
        }
        return totalStocks;
    }
}
