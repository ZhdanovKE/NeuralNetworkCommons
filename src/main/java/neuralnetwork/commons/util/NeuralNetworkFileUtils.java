package neuralnetwork.commons.util;

import java.io.BufferedReader;
import neuralnetwork.NamedNeuralNetwork;
import neuralnetwork.NeuralNetwork;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Helper class for serializing and de-serializing {@link NeuralNetwork} objects
 * to/from files.
 * @author Konstantin Zhdanov
 */
public class NeuralNetworkFileUtils {
    
    /**
     * Save the {@code nn} network with name {@code name} into a file with path 
     * {@code fileName}.
     * @param nn {@link NeuralNetwork} to be written in file {@code fileName} along
     * with the name {@code name}.
     * @param name {@code String} name to be written in file {@code fileName} along 
     * with the network {@code nn}.
     * @param fileName Path to the file where the {@code nn} will be saved.
     * @throws NullPointerException if {@code nn}, {@code name} or {@code fileName}
     * is null.
     * @throws IllegalArgumentException if there was an error while saving the 
     * network.
     */
    public static void saveWithName(NeuralNetwork nn, String name, String fileName) {
        if (nn == null || name == null || fileName == null) {
            throw new NullPointerException("Arguments cannot be null");
        }
        File file = new File(fileName);
        try (OutputStream out = new FileOutputStream(file)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(out)) {
                oos.writeObject(new NamedNeuralNetwork(nn, name));
            }
        }
        catch(IOException e) {
            throw new IllegalArgumentException("Cannot write into file", e);
        }
    }
    
    /**
     * Save the {@code nn} network with name {@code name} into a text file with 
     * path {@code fileName} as text.
     * @param nn {@link NeuralNetwork} to be written in file {@code fileName} along
     * with the name {@code name}.
     * @param name {@code String} name to be written in file {@code fileName} along 
     * with the network {@code nn}.
     * @param fileName Path to the file where the {@code nn} will be saved.
     * @throws NullPointerException if {@code nn}, {@code name} or {@code fileName}
     * is null.
     * @throws IllegalArgumentException if there was an error while saving the 
     * network.
     */
    public static void saveWithNameAsText(NeuralNetwork nn, String name, String fileName) {
        if (nn == null || name == null || fileName == null) {
            throw new NullPointerException("Arguments cannot be null");
        }
        File file = new File(fileName);
        try (Writer out = new FileWriter(file)) {
            // write name
            out.write(name);
            out.write("\n");
            
            // write signature
            String hiddenSizes = Arrays.stream(nn.getHiddenLayerSizes()).
                    mapToObj(v -> String.valueOf(v)).collect(
                            Collectors.joining(", "));
            String signature = String.format("%d, %s, %d", nn.getNumberInputs(),
                    hiddenSizes, nn.getNumberOutputs());
            out.write(signature);
            out.write("\n");

            int curLayerIdx = 0;
            int prevLayerSize = nn.getNumberInputs();
            int curLayerSize = nn.getHiddenLayerSize(curLayerIdx);

            // input <--> layer 1
            String layerStr = layerToString(nn, curLayerIdx, 
                    curLayerSize, prevLayerSize);
            out.write(layerStr);
            out.write("\n");

            // hidden layers
            for (curLayerIdx = 1; curLayerIdx < nn.getNumberHiddenLayers(); curLayerIdx++) {
                prevLayerSize = nn.getHiddenLayerSize(curLayerIdx - 1);
                curLayerSize = nn.getHiddenLayerSize(curLayerIdx);
                layerStr = layerToString(nn, curLayerIdx, 
                    curLayerSize, prevLayerSize);
                out.write(layerStr);
                out.write("\n");
            }
            
            // last layer <--> output
            prevLayerSize = nn.getHiddenLayerSize(nn.getNumberHiddenLayers() - 1);
            curLayerSize = nn.getNumberOutputs();
            curLayerIdx = nn.getNumberHiddenLayers();
            layerStr = layerToString(nn, curLayerIdx, 
                curLayerSize, prevLayerSize);
            out.write(layerStr);
            out.flush();
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Cannot write into file", e);
        }
    }
    
    private static String layerToString(NeuralNetwork nn, int layerNum, int layerSize, 
            int prevLayerSize) {
        StringBuilder sb = new StringBuilder();
        for (int prevNeuron = 0; prevNeuron < prevLayerSize - 1; prevNeuron++) {
            for (int curNeuron = 0; curNeuron < layerSize - 1; curNeuron++) {
                sb.append(nn.getWeight(layerNum, prevNeuron, curNeuron));
                sb.append(" ");
            }
            sb.append(nn.getWeight(layerNum, prevNeuron, layerSize - 1));
            sb.append("\n");
        }
        
        // last neuron of layerNum - 1
        for (int curNeuron = 0; curNeuron < layerSize - 1; curNeuron++) {
            sb.append(nn.getWeight(layerNum, prevLayerSize - 1, curNeuron));
            sb.append(" ");
        }
        sb.append(nn.getWeight(layerNum, prevLayerSize - 1, layerSize - 1));
        sb.append("\n");
        
        // biases
        for (int curNeuron = 0; curNeuron < layerSize - 1; curNeuron++) {
            sb.append(nn.getBias(layerNum, curNeuron));
            sb.append(" ");
        }
        sb.append(nn.getBias(layerNum, layerSize - 1));
        
        return sb.toString();
    }
    
    /**
     * Load instance of {@link NeuralNetwork} from a binary file with path 
     * {@code fileName}.
     * @param fileName {@code String} path to a binary file containing an
     * instance of {@code NeuralNetwork}.
     * @return An instance of {@link NeuralNetwork} loaded from file {@code fileName}.
     * @throws NullPointerException if {@code fileName} is {@code null}.
     * @throws IllegalArgumentException if there was an error while reading the
     * file.
     */
    public static NeuralNetwork load(String fileName) {
        if (fileName == null) {
            throw new NullPointerException("File name cannot be null");
        }
        NeuralNetwork nn;
        File file = new File(fileName);
        try (InputStream in = new FileInputStream(file)) {
            try (ObjectInputStream oos = new ObjectInputStream(in)) {
                nn = (NeuralNetwork)oos.readObject();
            }
        }
        catch(IOException e) {
            throw new IllegalArgumentException("Cannot read from file", e);
        }
        catch(ClassNotFoundException | ClassCastException e) {
            throw new IllegalArgumentException("Wrong file format", e);
        }
        return nn;
    }
    
    /**
     * Load instance of {@link NeuralNetwork} from a text file with path 
     * {@code fileName}.
     * @param fileName {@code String} path to a text file containing an
     * instance of {@code NeuralNetwork}.
     * @return An instance of {@link NeuralNetwork} loaded from file {@code fileName}.
     * @throws NullPointerException if {@code fileName} is {@code null}.
     * @throws IllegalArgumentException if there was an error while reading the
     * file.
     */
    public static NeuralNetwork loadFromTextFile(String fileName) {
        if (fileName == null) {
            throw new NullPointerException("File name cannot be null");
        }
        File file = new File(fileName);

        // Read name and signature
        String name;
        String[] signatureSplit;
        try (Reader in = new FileReader(file)) {
            try (BufferedReader bufIn = new BufferedReader(in)) {
                name = bufIn.readLine();
                String signature = bufIn.readLine();
                signatureSplit = signature.split(", ");
                if (signatureSplit.length < 3) {
                    // try split the name
                    signatureSplit = name.split(", ");
                    if (signatureSplit.length < 3) {
                        throw new IOException("Cannot read signature");
                    }
                    name = null;
                }
            }
        }
        catch (IOException | NumberFormatException e) {
            throw new IllegalArgumentException("Wrong file format: " + e.toString(), e);
        }
        
        NeuralNetwork nn = parseEmptyNetwork(name, signatureSplit);
        
        fillNetworkFromFile(nn, file, name != null);
        
        return nn;
    }

    private static NeuralNetwork parseEmptyNetwork(String name, String[] signatureSplit) {
        NeuralNetwork nn;
        int nInputs = Integer.parseInt(signatureSplit[0]);
        int nOutputs = Integer.parseInt(signatureSplit[signatureSplit.length - 1]);
        int[] hiddenSizes = new int[signatureSplit.length - 2];
        if (hiddenSizes.length < 1) {
            throw new IllegalArgumentException("There must be at least one hidden layer");
        }
        for (int i = 0; i < hiddenSizes.length; i++) {
            hiddenSizes[i] = Integer.parseInt(signatureSplit[i + 1]);
        }
        if (name == null) {
            // create NeuralNetwork
            nn = new NeuralNetwork(nInputs, hiddenSizes, nOutputs);
        }
        else {
            // create NamedNeuralNetwork
            nn = new NamedNeuralNetwork(nInputs, hiddenSizes, nOutputs,
                    name);
        }
        return nn;
    }

    private static void fillNetworkFromFile(NeuralNetwork nn, File file, boolean namePresent) {
        try (Reader in = new FileReader(file)) {
            try (BufferedReader bufIn = new BufferedReader(in)) {
                bufIn.readLine();
                if (namePresent) {
                    // There is a name in file => skip another line
                    bufIn.readLine();
                }

                // input <--> first hidden
                int curLayerIdx = 0;
                int prevLayerSize = nn.getNumberInputs();
                int curLayerSize = nn.getHiddenLayerSize(curLayerIdx);

                readLayerIntoNetwork(nn, curLayerIdx, curLayerSize, 
                        prevLayerSize, bufIn);

                // hidden layers
                for (curLayerIdx = 1; curLayerIdx < nn.getNumberHiddenLayers(); curLayerIdx++) {
                    prevLayerSize = nn.getHiddenLayerSize(curLayerIdx - 1);
                    curLayerSize = nn.getHiddenLayerSize(curLayerIdx);
                    readLayerIntoNetwork(nn, curLayerIdx, curLayerSize, 
                        prevLayerSize, bufIn);
                }

                // last layer <--> output
                prevLayerSize = nn.getHiddenLayerSize(nn.getNumberHiddenLayers() - 1);
                curLayerSize = nn.getNumberOutputs();
                curLayerIdx = nn.getNumberHiddenLayers();
                readLayerIntoNetwork(nn, curLayerIdx, curLayerSize, 
                        prevLayerSize, bufIn);
            }
        }
        catch (IOException | NumberFormatException e) {
            throw new IllegalArgumentException("Wrong file format: " + e.toString(), e);
        }
    }
    
    private static void readLayerIntoNetwork(NeuralNetwork nn, int layerNum, int layerSize, 
            int prevLayerSize, BufferedReader br) throws IOException, NumberFormatException {
        String line;
        String[] lineSplit;
        int expectedSize = layerSize;
        int curWeightIdx;
        for (int prevNeuron = 0; prevNeuron < prevLayerSize - 1; prevNeuron++) {
            line = br.readLine();
            lineSplit = line.split(" ");
            if (lineSplit.length != expectedSize) {
                throw new IOException("Wrong number of weights for layer");
            }
            curWeightIdx = 0;
            for (int curNeuron = 0; curNeuron < layerSize; curNeuron++) {
                nn.setWeight(layerNum, prevNeuron, curNeuron, 
                        Double.parseDouble(lineSplit[curWeightIdx++]));
            }
        }
        
        // last neuron of layerNum - 1
        line = br.readLine();
        lineSplit = line.split(" ");
        expectedSize = layerSize;
        if (lineSplit.length != expectedSize) {
            throw new IOException("Wrong number of weights for layer");
        }
        curWeightIdx = 0;
        for (int curNeuron = 0; curNeuron < layerSize; curNeuron++) {
            nn.setWeight(layerNum, prevLayerSize - 1, curNeuron, 
                    Double.parseDouble(lineSplit[curWeightIdx++]));
        }
        
        // biases
        line = br.readLine();
        lineSplit = line.split(" ");
        expectedSize = layerSize;
        if (lineSplit.length != expectedSize) {
            throw new IOException("Wrong number of weights for layer");
        }
        curWeightIdx = 0;
        for (int curNeuron = 0; curNeuron < layerSize; curNeuron++) {
            nn.setBias(layerNum, curNeuron, Double.parseDouble(lineSplit[curWeightIdx++]));
        }
    }
}

