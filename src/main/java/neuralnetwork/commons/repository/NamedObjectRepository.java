package neuralnetwork.commons.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for storing objects and their names, accessing objects by
 * their names and getting the name of an object.
 * @param <T> Type of objects to store in the repository.
 * @author Konstantin Zhdanov
 */
public class NamedObjectRepository<T> {
    private final Map<String, T> nameObjectMap;
    private final List<String> names;
    private final List<T> objects;
    
    /**
     * Functional interface describing an operation to be called when
     * there has been change of the name of one of the objects stored
     * in the {@link NamedObjectRepository} instance.
     * @param <E> Type of objects stored in the {@link NamedObjectRepository} 
     * instance.
     */
    public interface NameChangeListener<E> {
        /**
         * Function to be called when the name of the {@code object} changes
         * from {@code oldName} to {@code newName} in the {@link NamedObjectRepository}.
         * @param object Object of type {@code E} which name has changed.
         * @param oldName Old name of the {@code object}.
         * @param newName New name of the {@code object}.
         */
        void nameChanged(E object, String oldName, String newName);
    }
    
    private NameChangeListener<T> listener;
    
    /**
     * Create an empty repository.
     */
    public NamedObjectRepository() {
        nameObjectMap = new LinkedHashMap<>();
        names = new ArrayList<>();
        objects = new ArrayList<>();
    }
    
    /**
     * Add an object to this repository under the specific name. If another
     * object is already stored under the provided name, this object is replaced
     * with the provided object.
     * @param name {@link String} name that the {@code object} will be referred 
     * to as in this repository.
     * @param object Object to be stored in this repository under the name
     * {@code name}.
     * @throws NullPointerException if {@code name} or {@code object} is null.
     */
    public void add(String name, T object) {
        if (name == null || object == null) {
            throw new NullPointerException("Arguments cannot be null");
        }
        T prev = nameObjectMap.put(name, object);
        if (prev != null) {
            objects.set(objects.indexOf(prev), object);
        }
        else {
            names.add(name);
            objects.add(object);
        }
    }
    
    /**
     * Remove the object associated with provided name from this repository.
     * @param name
     * @return {@code true} if the removal is successful, {@code false} otherwise.
     * @throws NullPointerException if {@code name} is null.
     */
    public boolean remove(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        T obj = nameObjectMap.get(name);
        if (obj == null) {
            return false;
        }
        if (nameObjectMap.remove(name) == null) {
            return false;
        }
        names.remove(name);
        objects.remove(obj);
        return true;
    }
    
    /**
     * Get the object stored in this repository under the provided name.
     * @param name {@link String} name of the object to be returned.
     * @return The object of type {@code T} stored in this repository or
     * {@code null} if no object is stored under the name {@code name}.
     * @throws NullPointerException if {@code name} is null.
     */
    public T get(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        return nameObjectMap.get(name);
    }
    
    /**
     * Get the name the provided object is stored under in this repository.
     * @param object Object to get the name of from this repository.
     * @return {@link String} name of the object under which it's stored 
     * in this repository or {@code null} if the {@code object} is not 
     * stored in this repository.
     * @throws NullPointerException if {@code object} is null.
     */
    public String getNameForObject(T object) {
        if (object == null) {
            throw new NullPointerException("Object cannot be null");
        }
        for (String name : getNamesList()) {
            if (object.equals(get(name))) {
                return name;
            }
        }
        return null;
    }
    
    /**
     * Whether this repository contains an object under the specified name.
     * @param name {@link String} name to test the existence of in this repository.
     * @return {@code true} if this repository contains an object under the name
     * {@code name}, {@code false} otherwise.
     * @throws NullPointerException if {@code name} is null.
     */
    public boolean containsName(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        return nameObjectMap.containsKey(name);
    }
    
    /**
     * Whether this repository contains the specified object.
     * @param object Object to check the existence of in this repository.
     * @return {@code true} if this repository contains the {@code object},
     * {@code false} otherwise.
     * @throws NullPointerException if {@code object} is null.
     */
    public boolean containsObject(T object) {
        return getNameForObject(object) != null;
    }
    
    /**
     * Rename the object stored in this repository under the name {@code oldName}
     * to be known under the new name {@code newName}.
     * @param oldName The current name of an object in this repository to be renamed.
     * @param newName The new name to be used to refer to the object stored in this 
     * repository under the name {@code oldName}.
     * @throws IllegalArgumentException if this repository doesn't contain
     * an object under the name {@code oldName} or if this repository already
     * contains an object under the name {@code newName} in case it's different from
     * {@code oldName}.
     * @throws NullPointerException if {@code oldName} or {@code newName} is null.
     */
    public void rename(String oldName, String newName) {
        if (!containsName(oldName)) {
            throw new IllegalArgumentException("Object with name " + oldName +  
                    " doesn't exist");
        }
        if (newName == null) {
            throw new NullPointerException("New name cannot be null");
        }
        if (newName.equals(oldName)) {
            // same name => do nothing
            return;
        }
        if (containsName(newName)) {
            throw new IllegalArgumentException("Object with name " + newName + 
                    " already exists");
        }
        
        T object = nameObjectMap.remove(oldName);
        nameObjectMap.put(newName, object);
        
        // Preserving the order
        int nameIdx = names.indexOf(oldName);
        names.set(nameIdx, newName);
        
        onNameChange(object, oldName, newName);
    }
    
    /**
     * This method is called every time a rename of an object occurs.
     * @param object Object that has been renamed.
     * @param oldName The previous name of the {@code object}.
     * @param newName The newly set name of the {@code object}.
     */
    protected void onNameChange(T object, String oldName, String newName) {
        if (listener != null) {
            listener.nameChanged(object, oldName, newName);
        }
    }
    
    /**
     * Set {@link NameChangeListener} to be called
     * every time a rename of an object stored in this repository occurs.
     * @param listener Instance of {@link NameChangeListener} to be called
     * every time a rename of an object stored in this repository occurs.
     */
    public void setOnNameChangeListener(NameChangeListener<T> listener) {
        this.listener = listener;
    }
    
    /**
     * Remove the previously set {@link NameChangeListener} from this repository.
     * If no listener has been set, this method does nothing.
     */
    public void removeOnNameChangeListener() {
        this.listener = null;
    }
    
    /**
     * Get the number of objects stored in this repository.
     * @return {@code int} value of the number of objects in this repository.
     */
    public int size() {
        return nameObjectMap.size();
    }
    
    /**
     * Whether this repository is empty.
     * @return {@code true} if this repository is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return nameObjectMap.isEmpty();
    }
    
    /**
     * Get the list of the names of all objects stored in this repository.
     * @return {@link List} of the names of the objects stored in this repository 
     * or an empty {@link List} if the repository is empty. The returned list should
     * not be modified and may even be unmodifiable.
     */
    public List<String> getNamesList() {
        return Collections.unmodifiableList(names);
    }
    
    /**
     * Get the list of all objects stored in this repository.
     * @return {@link List} of objects stored in this repository or an empty
     * {@link List} if the repository is empty. The returned list should
     * not be modified and may even be unmodifiable.
     */
    public List<T> getObjectsList() {
        return Collections.unmodifiableList(objects);
    }
}

