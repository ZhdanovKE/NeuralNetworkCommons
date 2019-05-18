package neuralnetwork.commons.testutil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import neuralnetwork.NeuralNetwork;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Helper class for testing {@link NeuralNetwork}.
 * @author Konstantin Zhdanov
 */
public class TestUtils {
    
    /**
     * Constant precision for comparing double values
     */
    public static final double DELTA = 1e-10;
    
    /**
     * Assert if {@code actual} 3D array is equal to {@code expected} 3D array
     * @param expected expected array
     * @param actual actual array
     */
    public static void assertArraysEqual(double[][][] expected, double[][][] actual) {
        if ((expected == null) ^ (actual == null)) {
            Assert.fail("One of the arrays of null while the other is not");
        }
        if (expected == null && actual == null) {
            return;
        }
        Assert.assertEquals("Sizes of the array are different", expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertArraysEqual(expected[i], actual[i]);
        }
    }
    
    /**
     * Assert if {@code actual} 2D array is equal to {@code expected} 2D array
     * @param expected expected array
     * @param actual actual array
     */
    public static void assertArraysEqual(double[][] expected, double[][] actual) {
        if ((expected == null) ^ (actual == null)) {
            Assert.fail("One of the arrays of null while the other is not");
        }
        if (expected == null && actual == null) {
            return;
        }
        Assert.assertEquals("Sizes of the array are different", expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertArrayEquals(expected[i], actual[i], DELTA);
        }
    }
    
    /**
     * Check if {@code actual} 3D array is equal to {@code expected} 3D array
     * @param expected expected array
     * @param actual actual array
     * @return {@code true} if the array are equal and {@code false} otherwise
     */
    public static boolean arraysEqual(double[][][] expected, double[][][] actual) {
        if ((expected == null) ^ (actual == null)) {
            return false;
        }
        if (expected == null && actual == null) {
            return true;
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i].length != actual[i].length) {
               return false;
            }
            if (!arraysEqual(expected[i], actual[i])) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Check if {@code actual} 2D array is equal to {@code expected} 2D array
     * @param expected expected array
     * @param actual actual array
     * @return {@code true} if the array are equal and {@code false} otherwise
     */
    public static boolean arraysEqual(double[][] expected, double[][] actual) {
        if ((expected == null) ^ (actual == null)) {
            return false;
        }
        if (expected == null && actual == null) {
            return true;
        }
        for (int i = 0; i < expected.length; i++) {
           if (expected[i].length != actual[i].length) {
               return false;
           }
           for (int j = 0; j < expected[i].length; j++) {
               if (expected[i][j] != actual[i][j]) {
                   return false;
               }
           }
        }
        return true;
    }
    
    /**
     * Create a 3D array of {@code NeuralNetwork}'s weights and copy the weights of
     * {@code nn} into this array.
     * @param nn {@code NeuralNetwork} object to extract weights from
     * @return double 3D array containing the weights of {@code nn} neural network
     */
    public static double[][][] extractNNWeights(NeuralNetwork nn) {
        double[][][] extractedWeights = new double[nn.getNumberHiddenLayers() + 1][][];
        extractedWeights[0] = new double[nn.getHiddenLayerSize(0)][nn.getNumberInputs()];
        for (int i = 0; i < nn.getNumberInputs(); i++) {
            for (int j = 0; j < nn.getHiddenLayerSize(0); j++) {
                extractedWeights[0][j][i] = nn.getWeight(0, i, j);
            }
        }
        for (int i = 1; i < nn.getNumberHiddenLayers(); i++) {
            extractedWeights[i] = new double[nn.getHiddenLayerSize(i)][nn.getHiddenLayerSize(i-1)];
            for (int j = 0; j < nn.getHiddenLayerSize(i); j++) {
                for (int k = 0; k < nn.getHiddenLayerSize(i-1); k++) {
                    extractedWeights[i][j][k] = nn.getWeight(i, k, j);
                }
            }
        }
        extractedWeights[nn.getNumberHiddenLayers()] = 
                new double[nn.getNumberOutputs()]
                        [nn.getHiddenLayerSize(nn.getNumberHiddenLayers() - 1)];
        for (int i = 0; i < nn.getNumberOutputs(); i++) {
            
            for (int j = 0; j < nn.getHiddenLayerSize(nn.getNumberHiddenLayers() - 1); j++) {
                extractedWeights[nn.getNumberHiddenLayers()][i][j] = 
                        nn.getWeight(nn.getNumberHiddenLayers(), j, i);
            }
        }
        
        return extractedWeights;
    }
    
    /**
     * Create a 2D array of {@code NeuralNetwork}'s biases and copy the biases of
     * {@code nn} into this array.
     * @param nn {@code NeuralNetwork} object to extract weights from
     * @return double 2D array containing the biases of {@code nn} neural network
     */
    public static double[][] extractNNBiases(NeuralNetwork nn) {
        double[][] extractedBiases = new double[nn.getNumberHiddenLayers() + 1][];
        for (int i = 0; i < nn.getNumberHiddenLayers(); i++) {
            extractedBiases[i] = new double[nn.getHiddenLayerSize(i)];
            for (int j = 0; j < extractedBiases[i].length; j++) {
                extractedBiases[i][j] = nn.getBias(i, j);
            }
        }
        extractedBiases[nn.getNumberHiddenLayers()] = new double[nn.getNumberOutputs()];
        for (int i = 0; i < nn.getNumberOutputs(); i++) {
            extractedBiases[nn.getNumberHiddenLayers()][i] = 
                    nn.getBias(nn.getNumberHiddenLayers(), i);
        }
        return extractedBiases;
    }
    
    public static double[][][] copyWeights(double[][][] weights) {
        double[][][] extractedWeights = new double[weights.length][][];
        
        for (int i = 0; i < weights.length; i++) {
            extractedWeights[i] = new double[weights[i].length][];
            for (int j = 0; j < weights[i].length; j++) {
                extractedWeights[i][j] = weights[i][j].clone();
            }
        }
        
        return extractedWeights;
    }
    
    public static double[][] copyBiases(double[][] biases) {
        double[][] extractedBiases = new double[biases.length][];
        for (int i = 0; i < extractedBiases.length; i++) {
            extractedBiases[i] = biases[i].clone();
        }
        
        return extractedBiases;
    }
    
    public static boolean weightsSameSize(double[][][] arg1, double[][][] arg2) {
        if (arg1.length != arg2.length) {
            return false;
        }
        for (int i = 0; i < arg1.length; i++) {
            if (arg1[i].length != arg2[i].length) {
                return false;
            }
            for (int j = 0; j < arg1[i].length; j++) {
                if (arg1[i][j].length != arg2[i][j].length) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean biasesSameSize(double[][] arg1, double[][] arg2) {
        if (arg1.length != arg2.length) {
            return false;
        }
        for (int i = 0; i < arg1.length; i++) {
            if (arg1[i].length != arg2[i].length) {
                return false;
            }
        }
        return true;
    }
    
    public static void assertNNEquals(NeuralNetwork expected, NeuralNetwork actual) {
        assertEquals("Number of inputs changed", expected.getNumberInputs(), actual.getNumberInputs());
        assertEquals("Number of outputs changed", expected.getNumberOutputs(), actual.getNumberOutputs());
        assertEquals("Number of hidden layers changed", expected.getNumberHiddenLayers(), actual.getNumberHiddenLayers());
        assertArrayEquals("Hidden layer sizes changed", expected.getHiddenLayerSizes(), actual.getHiddenLayerSizes());
        assertSame("Activation function changed", expected.getActivationFunction(), actual.getActivationFunction());
        
        double[][][] expectedWeights = extractNNWeights(expected);
        double[][] expectedBiases = extractNNBiases(expected);
        
        double[][][] actualWeights = extractNNWeights(actual);
        double[][] actualBiases = extractNNBiases(actual);
        
        assertArraysEqual(expectedWeights, actualWeights);
        assertArraysEqual(expectedBiases, actualBiases);
    }
    
    public static void assertNNNotEquals(NeuralNetwork notExpected, NeuralNetwork actual) {
        if (!sameStructure(notExpected, actual)) {
            return;
        }
        
        double[][][] expectedWeights = extractNNWeights(notExpected);
        double[][] expectedBiases = extractNNBiases(notExpected);
        
        double[][][] actualWeights = extractNNWeights(actual);
        double[][] actualBiases = extractNNBiases(actual);
        
        if (arraysEqual(expectedWeights, actualWeights) &&
            arraysEqual(expectedBiases, actualBiases)) {
            Assert.fail("Neural networks are equal");
        }

    }
    
    public static boolean sameStructure(NeuralNetwork expected, NeuralNetwork actual) {
        return (expected.getNumberInputs() == actual.getNumberInputs() &&
                expected.getNumberOutputs() == actual.getNumberOutputs() &&
                expected.getNumberHiddenLayers() == actual.getNumberHiddenLayers() &&
                Arrays.equals(expected.getHiddenLayerSizes(), actual.getHiddenLayerSizes()) &&
                expected.getActivationFunction().equals(actual.getActivationFunction()));
    }
    
    public static ExecutorService getDirectExecutor() {
        ExecutorService serialExecutor = new ExecutorService() {
            private final Logger logger = Logger.getLogger("DirectExecutor");
            
            @Override
            public void shutdown() {
                logger.log(Level.INFO, "shutdown");
            }

            @Override
            public List<Runnable> shutdownNow() {
                logger.log(Level.INFO, "shutdownNow");
                return Collections.EMPTY_LIST;
            }

            @Override
            public boolean isShutdown() {
                return false;
            }

            @Override
            public boolean isTerminated() {
                return false;
            }

            @Override
            public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
                logger.log(Level.INFO, "awaitTermination({0}, {1})", new Object[] {String.valueOf(timeout), unit.toString()});
                return true;
            }

            @Override
            public <T> Future<T> submit(Callable<T> task) {
                FutureTask<T> result = new FutureTask<>(task);
                result.run();
                return result;
            }

            @Override
            public <T> Future<T> submit(Runnable task, T result) {
                FutureTask<T> resultFuture = new FutureTask<>(task, result);
                resultFuture.run();
                return resultFuture;
            }

            @Override
            public Future<?> submit(Runnable task) {
                FutureTask<?> resultFuture = new FutureTask<>(task, null);
                resultFuture.run();
                return resultFuture;
            }

            @Override
            public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void execute(Runnable command) {
                command.run();
            }

        };
        return serialExecutor;
    }
    
    
}
