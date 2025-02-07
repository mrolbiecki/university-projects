package cp2024.solution;

import cp2024.circuit.CircuitValue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Represents the value of a circuit computed in parallel.
 */
public class ParallelCircuitValue implements CircuitValue {

    private final Future<Boolean> future;

    /**
     * Constructs a ParallelCircuitValue with the provided future.
     *
     * @param future the future representing the circuit computation result
     */
    public ParallelCircuitValue(Future<Boolean> future) {
        this.future = future;
    }

    /**
     * Constructs an empty ParallelCircuitValue.
     */
    public ParallelCircuitValue() {
        this.future = null;
    }

    @Override
    public boolean getValue() throws InterruptedException {
        if (future == null) {
            throw new InterruptedException("No computation result available.");
        }

        try {
            Boolean result = future.get();
            if (result == null) {
                throw new InterruptedException("Computation was interrupted or failed.");
            }
            return result;
        } catch (ExecutionException e) {
            throw new InterruptedException("Error during computation: " + e.getMessage());
        }
    }
}
