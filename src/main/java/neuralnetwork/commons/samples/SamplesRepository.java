package neuralnetwork.commons.samples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Repository for storing neural network's samples, containing numbers
 * and an optional header and preserving the insertion order of elements.
 * @param <T> A numerical type that the samples hold.
 * @author Konstantin Zhdanov
 */
public class SamplesRepository<T extends Number> {
    private final List<String> header;
    
    private final List<List<T>> items;
    
    /**
     * Create an empty repository of samples.
     */
    public SamplesRepository() {
        items = new ArrayList<>();
        header = new LinkedList<>();
    }
    
    /**
     * Add a sample into this repository.
     * @param sample {@code List} of numerical values to be add as a sample.
     * @throws NullPointerException if {@code sample} is null.
     */
    public void add(List<T> sample) {
        if (sample == null) {
            throw new NullPointerException("Sample cannot be null");
        }
        items.add(sample);
    }
    
    /**
     * Remove the sample with index {@code idx} from this repository.
     * @param idx Index of sample to be removed from this repository.
     * @throws IndexOutOfBoundsException if {@code idx} is out of range.
     */
    public void remove(int idx) {
        items.remove(idx);
    }
    
    /**
     * Set the {@code header} list as the headers (names) for the samples' variables.
     * @param header {@code List} of {@String} values of header for every variable
     * in each sample.
     * @throws NullPointerException if {@code header} is null.
     */
    public void setHeader(List<String> header) {
        this.header.addAll(header);
    }
    
    /**
     * Get the header list associated with the samples loaded in this
     * repository. If this repository is holding any samples and the header
     * hasn't been set explicitly, the returned list contains the default header
     * values: "Var 1", "Var 2", ..., "Var n". 
     * If no samples have ever been loaded in this repository and the header
     * hasn't been set explicitly, the returned list will be empty.
     * @return {@code List} of {@code String} header (names) values for the 
     * samples' variables. The returned list is unmodifiable.
     */
    public List<String> getHeader() {
        if (header.isEmpty() && !items.isEmpty()) {
            createDefaultHeader();
        }
        return Collections.unmodifiableList(header);
    }
    
    private void createDefaultHeader() {
        header.clear();
        int sampleSize = sampleSize();
        for (int sampleVar = 0; sampleVar < sampleSize; sampleVar++) {
            header.add(String.format("Var %d", sampleVar + 1));
        }
    }
    
    /**
     * Get the sample with index {@code idx} from this repository.
     * @param idx The index of the sample to be returned.
     * @return {@code List} of numerical values that this repository holds
     * under the index {@code idx}. Any changes to this list will change the
     * object this repository holds.
     * @throws IllegalArgumentException if {@code idx} is out of range.
     */
    public List<T> getSample(int idx) {
        return items.get(idx);
    }
    
    /**
     * Get all samples this repository holds.
     * @return {@code List} of all samples this repository holds. Any changes
     * to this list will change the samples this repository holds.
     */
    public List<List<T>> getAll() {
        return items;
    }
    
    /**
     * Number of samples this repository holds.
     * @return {@code int} value representing the number of samples in this
     * repository.
     */
    public int size() {
        return items.size();
    }
    
    /**
     * Size of the samples this repository holds. If the repository is empty, 
     * the size is zero.
     * @return {@code int} value holding the size of the samples in this 
     * repository. If the repository is empty, 0 is returned.
     */
    public int sampleSize() {
        if (items.isEmpty()) {
            return 0;
        }
        return items.get(0).size();
    }
    
    /**
     * Whether this repository is empty.
     * @return {@code boolean} value representing if this repository holds 
     * any samples or not.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}