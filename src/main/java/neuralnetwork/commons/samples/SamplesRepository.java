package neuralnetwork.commons.samples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Repository for storing neural network's samples, containing numbers
 * and optional header.
 * @param <T> A numerical type that the samples hold.
 * @author Konstantin Zhdanov
 */
public class SamplesRepository<T extends Number> {
    private final List<String> header;
    
    private final List<List<T>> items;
    
    public SamplesRepository() {
        items = new ArrayList<>();
        header = new LinkedList<>();
    }
    
    public void add(List<T> sample) {
        if (sample == null) {
            throw new NullPointerException("Sample cannot be null");
        }
        items.add(sample);
    }
    
    public void remove(int idx) {
        items.remove(idx);
    }
    
    public void setHeader(List<String> header) {
        this.header.addAll(header);
    }
    
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
    
    public List<T> getSample(int idx) {
        return items.get(idx);
    }
    
    public List<List<T>> getAll() {
        return items;
    }
    
    public int size() {
        return items.size();
    }
    
    public int sampleSize() {
        if (items.isEmpty()) {
            return 0;
        }
        return items.get(0).size();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
