package cp2024.solution;

import cp2024.circuit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
/**
 * A parallel solver for boolean circuits using a CachedThreadPool.
 */
public class ParallelCircuitSolver implements CircuitSolver {

    private final ExecutorService executor;

    /**
     * A generic pair record.
     */
    public record Pair<U, V>(U first, V second) {}

    /**
     * A task for solving parts of the circuit in parallel.
     */
    private static class SolvingTask implements Callable<Boolean> {

        private final CircuitNode node;
        private final ExecutorService executor;
        private final LinkedBlockingQueue<Pair<Boolean, Integer>> parentQueue;
        private final LinkedBlockingQueue<Pair<Boolean, Integer>> childQueue;
        private final int id;
        private List<Future<Boolean>> futures;

        SolvingTask(ExecutorService executor, CircuitNode node,
                    LinkedBlockingQueue<Pair<Boolean, Integer>> parentQueue, int id) {
            this.executor = executor;
            this.node = node;
            this.parentQueue = parentQueue;
            this.childQueue = new LinkedBlockingQueue<>();
            this.id = id;
        }

        @Override
        public Boolean call() {
            try {
                boolean result = recursiveSolve(node);
                parentQueue.put(new Pair<>(result, id));
                return result;
            } catch (InterruptedException | ExecutionException e) {
                cancelAllTasks();
                return null;
            }
        }

        private boolean recursiveSolve(CircuitNode node) throws InterruptedException, ExecutionException {
            if (node.getType() == NodeType.LEAF) {
                return ((LeafNode) node).getValue();
            }

            CircuitNode[] args = node.getArgs();
            checkInterrupted();

            futures = new ArrayList<>(args.length);
            for (int i = 0; i < args.length; i++) {
                futures.add(executor.submit(new SolvingTask(executor, args[i], childQueue, i)));
            }

            return switch (node.getType()) {
                case IF -> solveIF();
                case AND -> countTrueToThreshold(args.length, args.length);
                case OR -> countTrueToThreshold(1, args.length);
                case GT -> countTrueToThreshold(((ThresholdNode) node).getThreshold() + 1, args.length);
                case LT -> !countTrueToThreshold(((ThresholdNode) node).getThreshold(), args.length);
                case NOT -> !countTrueToThreshold(1, args.length);
                default -> throw new IllegalArgumentException("Unsupported node type: " + node.getType());
            };
        }

        private void checkInterrupted() throws InterruptedException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
        }

        private void cancelAllTasks() {
            if (futures != null) {
                for (Future<Boolean> future : futures) {
                    future.cancel(true);
                }
            }
        }

        private boolean countTrueToThreshold(int threshold, int length) throws InterruptedException {
            int trueCount = 0;
            for (int i = 0; i < length && trueCount < threshold; i++) {
                checkInterrupted();
                if (childQueue.take().first) {
                    trueCount++;
                }
                if (trueCount + (length - i - 1) < threshold) {
                    break;
                }
            }
            cancelAllTasks();
            return trueCount >= threshold;
        }

        private boolean solveIF() throws InterruptedException, ExecutionException {
            boolean[] isComputed = new boolean[3];
            boolean[] values = new boolean[3];

            for (int i = 0; i < 3; i++) {
                Pair<Boolean, Integer> element = childQueue.take();
                isComputed[element.second] = true;
                values[element.second] = element.first;

                if (isComputed[0]) {
                    break;
                }

                if (isComputed[1] && isComputed[2] && values[1] == values[2]) {
                    futures.get(0).cancel(true);
                    return values[1];
                }
            }

            if (values[0]) {
                futures.get(2).cancel(true);
                return futures.get(1).get();
            } else {
                futures.get(1).cancel(true);
                return futures.get(2).get();
            }
        }
    }

    public ParallelCircuitSolver() {
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public synchronized CircuitValue solve(Circuit circuit) {
        if (executor.isShutdown()) {
            return new ParallelCircuitValue();
        }
        return new ParallelCircuitValue(executor.submit(
                new SolvingTask(executor, circuit.getRoot(), new LinkedBlockingQueue<>(), 1)
        ));
    }

    @Override
    public synchronized void stop() {
        executor.shutdownNow();
    }
}
