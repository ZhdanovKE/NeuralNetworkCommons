package neuralnetwork.commons.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import neuralnetwork.commons.samples.SamplesRepository;

/**
 * Helper class for reading {@link SamplesRepository} objects
 * from files.
 * @author Konstantin Zhdanov
 */
public class SamplesFileUtils {

    private static final String DELIMETER = ",";
    /**
     * Load samples and create an instance of {@link SamplesRepository} 
     * from a CSV file with path {@code fileName}.
     * @param fileName {@link String} path to a CSV file containing samples values.
     * @return An instance of {@link SamplesRepository} parsed from file {@code fileName}.
     * @throws NullPointerException if {@code fileName} is {@code null}.
     * @throws IllegalArgumentException if there was an error while reading the
     * file.
     */
    public static SamplesRepository<Double> loadFromCSV(String fileName) {
        if (fileName == null) {
            throw new NullPointerException("Name cannot be null");
        }
        File file = new File(fileName);
        return loadSamplesFromFile(file);
    }
    
    // load a CSV file
    private static SamplesRepository<Double> loadSamplesFromFile(File file) {
        SamplesRepository<Double> repository = new SamplesRepository<>();
        try (FileReader fileReader = new FileReader(file)) {
            try (BufferedReader bufReader = new BufferedReader(fileReader)) {
                String line = bufReader.readLine();
                if (line == null) {
                    // file is empty
                    return repository;
                }
                List<Double> sample = parseLine(line);
                int sampleSize;
                if (sample == null) {
                    // the first line is the header
                    String[] headerTitles = extractHeaderTitles(line);
                    repository.setHeader(Arrays.asList(headerTitles));
                    sampleSize = headerTitles.length;
                }
                else {
                    // there's no header
                    repository.add(sample);
                    sampleSize = sample.size();
                }
                fillSamples(repository, bufReader, sampleSize);
            }
        }
        catch (IOException e) {
           throw new IllegalArgumentException("Error while reading the file", e);
        }
        return repository;
    }
    
    // extract header titles from a CSV line
    private static String[] extractHeaderTitles(String line) {
        String[] lineValues = line.split(DELIMETER);
        String[] headerValues = new String[lineValues.length];
        for (int headerColNum = 0; headerColNum < headerValues.length; headerColNum++) {
            headerValues[headerColNum] = lineValues[headerColNum].trim();
        }
        return headerValues;
    }
    
    // load samples one by one from the BufferedReader
    // invalid samples are discarded
    private static void fillSamples(
            SamplesRepository<Double> repository, 
            BufferedReader bufReader,
            int sampleSize) throws IOException {
        String line;
        List<Double> sample;
        while ((line = bufReader.readLine()) != null) {
            sample = parseLine(line);
            if (sample == null) {
                // skipping...
            }
            else if (sample.size() != sampleSize) {
                // wrong size => skipping...
            }
            else {
                repository.add(sample);
            }
        }
    }
    
    // Parse a CSV file line. 
    // Return null if cannot be parsed
    private static List<Double> parseLine(String line) {
        String[] lineValues = line.split(DELIMETER);
        List<Double> sample;
        try {
            sample = new ArrayList<>();
            for (String value : lineValues) {
                double doubleValue = Double.parseDouble(value.trim());
                sample.add(doubleValue);
            }
        }
        catch (NumberFormatException e) {
            // error parsing the line
            sample = null;
        }
        return sample;
    }
}
