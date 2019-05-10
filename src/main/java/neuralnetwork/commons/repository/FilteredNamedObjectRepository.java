package neuralnetwork.commons.repository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A wrapper around a {@code NamedObjectRepository} that creates a view of its
 * elements after filtering.
 * @param <T> Type of objects to store in the repository.
 * @author Konstantin Zhdanov
 */
public class FilteredNamedObjectRepository<T> extends NamedObjectRepository<T>{

    private Predicate<T> objectFilter;
    private final Predicate<String> nameFilter = name -> 
            get(name) != null;
    
    private NamedObjectRepository<T> repository;
            
    public FilteredNamedObjectRepository(NamedObjectRepository<T> repository) {
        this(repository, t -> true);
    }
    
    public FilteredNamedObjectRepository(NamedObjectRepository<T> repository, 
            Predicate<T> objectFilter) {
        if (repository == null) {
            throw new NullPointerException("Repository cannot be null");
        }
        if (objectFilter == null) {
            throw new NullPointerException("Filter cannot be null");
        }
        this.repository = repository;
        this.objectFilter = objectFilter;
    }

    // add to the underlying repository without filtering
    @Override
    public void add(String name, T object) {
        repository.add(name, object); 
    }

    // remove from the underlying repository without filtering
    @Override
    public boolean remove(String name) {
        return repository.remove(name); 
    }
    
    // return filtered list
    @Override
    public List<T> getObjectsList() {
        return repository.getObjectsList().stream().filter(objectFilter).
                collect(Collectors.toList());
    }

    // return filtered list
    @Override
    public List<String> getNamesList() {
        return repository.getNamesList().stream().filter(nameFilter).
                collect(Collectors.toList());
    }

    // if filtered view empty
    @Override
    public boolean isEmpty() {
        return getNamesList().isEmpty();
    }

    // size of the filtered view
    // O(n) time
    @Override
    public int size() {
        return getNamesList().size();
    }

    @Override
    public void rename(String oldName, String newName) {
        repository.rename(oldName, newName);
    }
    
    @Override
    public void removeOnNameChangeListener() {
        repository.removeOnNameChangeListener();
    }

    @Override
    public void setOnNameChangeListener(NameChangeListener<T> listener) {
        repository.setOnNameChangeListener(listener);
    }

    // false if filtered out
    @Override
    public boolean containsObject(T object) {
        if (repository.containsObject(object)) {
            return objectFilter.test(object);
        }
        return false;
    }

    // false if filtered out
    @Override
    public boolean containsName(String name) {
        if (repository.containsName(name)) {
            return nameFilter.test(name);
        }
        return false;
    }

    // null if filtered out
    @Override
    public String getNameForObject(T object) {
        if (object == null) {
            throw new NullPointerException("Agrument cannot be null");
        }
        if (objectFilter.test(object)) {
            return repository.getNameForObject(object); 
        }
        return null;
    }

    // null if filtered out
    @Override
    public T get(String name) {
        T object = repository.get(name);
        return object == null ? null : objectFilter.test(object) ? object : null;
    }
    
}
