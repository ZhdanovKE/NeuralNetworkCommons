package neuralnetwork.commons.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 * Test cases for NamedObjectRepository class
 * @author Konstantin Zhdanov
 */
public class NamedObjectRepositoryTest {
    
    public NamedObjectRepositoryTest() {
    }

    /**
     * Test of containsObject method, of class NamedObjectRepository.
     */
    @Test(expected = NullPointerException.class)
    public void testContainsObject_NullObjectPassed_Throw() {
        System.out.println("containsObject");
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Object 1 Name", "Object 1");
        instance.add("Object 2 Name", "Object 2");
        instance.add("Object 3 Name", "Object 3");
        Object object = null;

        boolean actual = instance.containsObject(object);
        
        fail("The test case must throw");
    }
    
    @Test
    public void testContainsObject_NonExistingObjectPassed_ReturnFalse() {
        System.out.println("containsObject");
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Object 1 Name", "Object 1");
        instance.add("Object 2 Name", "Object 2");
        instance.add("Object 3 Name", "Object 3");
        Object object = "Another object";
        boolean expected = false;
        
        boolean actual = instance.containsObject(object);
        
        assertThat(actual, CoreMatchers.is(expected));
    }
    
    @Test
    public void testContainsObject_ExistingObjectPassed_ReturnTrue() {
        System.out.println("containsObject");
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Object 1 Name", "Object 1");
        instance.add("Object 2 Name", "Object 2");
        instance.add("Object 3 Name", "Object 3");
        Object object = "Object 2";
        boolean expected = true;
        
        boolean actual = instance.containsObject(object);
        
        assertThat(actual, CoreMatchers.is(expected));
    }
    
    /**
     * Test of rename method, of class NamedObjectRepository.
     */
    @Test(expected = NullPointerException.class)
    public void testRename_OldNameNull_Throw() {
        System.out.println("rename");
        String oldName = null;
        String newName = "new Name";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.rename(oldName, newName);
        
        fail("The test case must throw");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRename_OldNameDoesntExist_Throw() {
        System.out.println("rename");
        String oldName = "No name";
        String newName = "New name";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Another name", new Object());
        
        instance.rename(oldName, newName);
        
        fail("The test case must throw");
    }
    
    @Test(expected = NullPointerException.class)
    public void testRename_OldNameExistAndNewNameNull_Throw() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = null;
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, new Object());
        
        instance.rename(oldName, newName);
        
        fail("The test case must throw");
    }
    
    @Test
    public void testRename_OldNameExistAndNewNameNotNull_GetOldReturnNull() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "New name";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, new Object());
        
        instance.rename(oldName, newName);
        
        assertNull(instance.get(oldName));
    }
    
    @Test
    public void testRename_OldNameExistAndNewNameNotNull_ContainsOldReturnFalse() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "New name";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, new Object());
        
        instance.rename(oldName, newName);
        
        assertTrue(!instance.containsName(oldName));
    }
    
    @Test
    public void testRename_OldNameExistAndNewNameNotNull_GetNewReturnOldObject() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "New name";
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, object);
        
        instance.rename(oldName, newName);
        
        assertSame(object, instance.get(newName));
    }
    
    @Test
    public void testRename_OldNameExistAndNewNameNotNull_ContainsNewReturnTrue() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "New name";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, new Object());
        
        instance.rename(oldName, newName);
        
        assertTrue(instance.containsName(newName));
    }
    
    @Test
    public void testRename_OldNameExistAndNewNameNotNull_SizeStaySame() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "New name";
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, object);
        int oldSize = instance.size();
        
        instance.rename(oldName, newName);
        
        assertEquals(oldSize, instance.size());
    }
    
    @Test
    public void testRename_OldNameExistAndNewNameNotNull_GetNameForObjectReturnNewName() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "New name";
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, object);
        
        instance.rename(oldName, newName);
        
        assertSame(newName, instance.getNameForObject(object));
    }
    
    @Test
    public void testRename_OldNameExistAndNewNameNotNull_GetNamesListNotContainOldName() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "New name";
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, object);
        
        instance.rename(oldName, newName);
        
        List<String> newNamesList = instance.getNamesList();
        
        assertThat(newNamesList, CoreMatchers.not(CoreMatchers.hasItem(oldName)));
    }
    
    @Test
    public void testRename_OldNameExistAndNewNameNotNull_GetNamesListContainNewName() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "New name";
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, object);
        
        instance.rename(oldName, newName);
        
        List<String> newNamesList = instance.getNamesList();
        
        assertThat(newNamesList, CoreMatchers.hasItem(newName));
    }
    
    @Test
    public void testRename_OldNameExistAndNewNameNotNull_GetObjectsListNotChanged() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "New name";
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, object);
        Collection<Object> oldObjectsList = instance.getObjectsList();
        
        instance.rename(oldName, newName);
        
        Collection<Object> newObjectsList = instance.getObjectsList();
        
        assertThat(newObjectsList, CoreMatchers.hasItems(oldObjectsList.toArray()));
        assertThat(oldObjectsList, CoreMatchers.hasItems(newObjectsList.toArray()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRename_OldNameExistAndNewNameAlreadyExist_Throw() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "Another name";
        Object object = new Object();
        Object anotherObject = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, object);
        instance.add(newName, anotherObject);
        
        instance.rename(oldName, newName);
        
        fail("The test case must fail");
    }
    
    @Test
    public void testRename_OldNameExistAndNewNameNotNull_OnNameChangeCalled() {
        System.out.println("rename");
        String oldName = "Old name";
        String newName = "New name";
        Object object = new Object();
        NamedObjectRepository instance = Mockito.spy(NamedObjectRepository.class);

        instance.add(oldName, object);
        
        instance.rename(oldName, newName);
        
        Mockito.verify(instance, Mockito.times(1)).onNameChange(object, 
                oldName, newName);
    }
    
    /**
     * Test of setOnNameChangeListener method, of class NamedObjectRepository.
     */
    @Test
    public void testSetOnNameChangeListener_RenameCalled_ListenerCalled() {
        System.out.println("setOnNameChangeListener");
        String oldName = "Old name";
        String newName = "New name";
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, object);
        NamedObjectRepository.NameChangeListener listener = Mockito.mock(
            NamedObjectRepository.NameChangeListener.class);
        
        instance.setOnNameChangeListener(listener);
        instance.rename(oldName, newName);
        
        Mockito.verify(listener, Mockito.times(1)).nameChanged(object, 
                oldName, newName);
    }
    
    @Test
    public void testSetOnNameChangeListener_RenameSameNameCalled_ListenerNotCalled() {
        System.out.println("setOnNameChangeListener");
        String oldName = "Old name";
        String newName = oldName;
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, object);
        NamedObjectRepository.NameChangeListener listener = Mockito.mock(
            NamedObjectRepository.NameChangeListener.class);
        
        instance.setOnNameChangeListener(listener);
        instance.rename(oldName, newName);
        
        Mockito.verifyZeroInteractions(listener);
    }
    
    /**
     * Test of removeOnNameChangeListener method, of class NamedObjectRepository.
     */
    @Test
    public void testRemoveOnNameChangeListener_RenameCalled_ListenerNotCalled() {
        System.out.println("removeOnNameChangeListener");
        String oldName = "Old name";
        String newName = "New name";
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(oldName, object);
        NamedObjectRepository.NameChangeListener listener = Mockito.mock(
            NamedObjectRepository.NameChangeListener.class);
        
        instance.setOnNameChangeListener(listener);
        instance.removeOnNameChangeListener();
        instance.rename(oldName, newName);
        
        Mockito.verify(listener, Mockito.never()).nameChanged(Mockito.anyObject(), 
                Mockito.anyString(), Mockito.anyString());
    }

    /**
     * Test of add method, of class NamedObjectRepository.
     */
    @Test(expected = NullPointerException.class)
    public void testAdd_NullName_Throw() {
        System.out.println("add");
        String name = null;
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(name, object);
        
        fail("The test case must throw");
    }
    
    @Test(expected = NullPointerException.class)
    public void testAdd_NullObject_Throw() {
        System.out.println("add");
        String name = "Name";
        Object object = null;
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add(name, object);
        
        fail("The test case must throw");
    }
    
    @Test
    public void testAdd_ExistingName_GetReturnNewObject() {
        System.out.println("add");
        String name = "Name 1";
        Object newObject = new Object();
        Object oldObject = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name 1", oldObject);
        
        instance.add(name, newObject);

        Object actual = instance.get(name);
        assertThat(actual, CoreMatchers.is(newObject));
    }
    
    /**
     * Test of remove method, of class NamedObjectRepository.
     */
    @Test(expected = NullPointerException.class)
    public void testRemove_NullName_Throw() {
        System.out.println("remove");
        String name = null;
        NamedObjectRepository instance = new NamedObjectRepository();
        
        instance.remove(name);
        
        fail("The test case must throw");
    }
    
    @Test
    public void testRemove_NameNotExist_ReturnFalse() {
        System.out.println("remove");
        String name = "Non-existing name";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name", new Object());
        boolean expResult = false;
        
        boolean result = instance.remove(name);
        
        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testRemove_NameExist_ReturnTrue() {
        System.out.println("remove");
        String name = "Name";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name", new Object());
        instance.add("Name 2", new Object());
        boolean expResult = true;
        
        boolean result = instance.remove(name);
        
        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testRemove_ExistingName_GetReturnNull() {
        System.out.println("remove");
        String name = "Name 1";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name 1", new Object());
        
        instance.remove(name);
        
        Object actual = instance.get(name);
        
        assertThat(actual, CoreMatchers.nullValue());
    }

    /**
     * Test of get method, of class NamedObjectRepository.
     */
    @Test(expected = NullPointerException.class)
    public void testGet_NullName_Throw() {
        System.out.println("get");
        String name = null;
        NamedObjectRepository instance = new NamedObjectRepository();

        instance.get(name);
        
        fail("The test case must throw");
    }
    
    @Test
    public void testGet_NonExistingName_ReturnNull() {
        System.out.println("get");
        String name = "Non-existing name";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name", new Object());

        Object result = instance.get(name);
        
        assertThat(result, CoreMatchers.nullValue());
    }
    
    @Test
    public void testGet_ExistingName_ReturnObjectWithThatName() {
        System.out.println("get");
        String name = "Name 2";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name 1", "Object 1");
        instance.add("Name 2", "Object 2");
        instance.add("Name 3", "Object 3");
        
        Object result = instance.get(name);
        
        assertThat(result, CoreMatchers.is("Object 2"));
    }

    /**
     * Test of getNameForObject method, of class NamedObjectRepository.
     */
    @Test(expected = NullPointerException.class)
    public void testGetNameForObject_NullObject_Throw() {
        System.out.println("getNameForObject");
        Object object = null;
        NamedObjectRepository instance = new NamedObjectRepository();
        
        instance.getNameForObject(object);
        
        fail("The test case must throw");
    }
    
    @Test
    public void testGetNameForObject_ExistingObject_ReturnName() {
        System.out.println("getNameForObject");
        Object object = "Object 2";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name 1", "Object 1");
        instance.add("Name 2", "Object 2");
        instance.add("Name 3", "Object 3");
        
        String result = instance.getNameForObject(object);
        
        assertThat(result, CoreMatchers.is("Name 2"));
    }
    
    @Test
    public void testGetNameForObject_NonExistingObject_ReturnNull() {
        System.out.println("getNameForObject");
        Object object = "Non-Existing Object";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name", "Object");
        
        String result = instance.getNameForObject(object);
        
        assertThat(result, CoreMatchers.nullValue());
    }

    /**
     * Test of containsName method, of class NamedObjectRepository.
     */
    @Test(expected = NullPointerException.class)
    public void testContainsName_NullName_Throw() {
        System.out.println("containsName");
        String name = null;
        NamedObjectRepository instance = new NamedObjectRepository();
        
        instance.containsName(name);
        
        fail("The test case must throw");
    }
    
    @Test
    public void testContainsName_NonExistingName_ReturnFalse() {
        System.out.println("containsName");
        String name = "Non-Existing Name";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name", new Object());
        boolean expResult = false;
        
        boolean result = instance.containsName(name);
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsName_ExistingName_ReturnTrue() {
        System.out.println("containsName");
        String name = "Name 2";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name 1", new Object());
        instance.add("Name 2", new Object());
        instance.add("Name 3", new Object());
        boolean expResult = true;
        
        boolean result = instance.containsName(name);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of size method, of class NamedObjectRepository.
     */
    @Test
    public void testSize_EmptyRepository_ReturnZero() {
        System.out.println("size");
        NamedObjectRepository instance = new NamedObjectRepository();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSize_AddNonExistingName_SizeIncreaseByOne() {
        System.out.println("size");
        String name = "Name New";
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name 1", new Object());
        int size = instance.size();
        int expSize = size + 1;
        instance.add(name, object);
        
        int actualSize = instance.size();
        
        assertThat(actualSize, CoreMatchers.is(expSize));
    }
    
    @Test
    public void testSize_AddExistingName_SizeStaySame() {
        System.out.println("size");
        String name = "Name 1";
        Object object = new Object();
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name 1", new Object());
        int size = instance.size();
        int expSize = size;
        instance.add(name, object);
        
        int actualSize = instance.size();
        
        assertThat(actualSize, CoreMatchers.is(expSize));
    }

    @Test
    public void testSize_RemoveExistingName_SizeDecreaseByOne() {
        System.out.println("size");
        String name = "Name 1";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name 1", new Object());
        int size = instance.size();
        int expSize = size - 1;
        instance.remove(name);
        
        int actualSize = instance.size();
        
        assertThat(actualSize, CoreMatchers.is(expSize));
    }
    
    @Test
    public void testSize_RemoveNonExistingName_SizeStaySame() {
        System.out.println("size");
        String name = "Name Non-Existing";
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name 1", new Object());
        int size = instance.size();
        int expSize = size;
        instance.remove(name);
        
        int actualSize = instance.size();
        
        assertThat(actualSize, CoreMatchers.is(expSize));
    }
    
    /**
     * Test of isEmpty method, of class NamedObjectRepository.
     */
    @Test
    public void testIsEmpty_EmptyRepository_ReturnTrue() {
        System.out.println("isEmpty");
        NamedObjectRepository instance = new NamedObjectRepository();
        boolean expResult = true;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIsEmpty_AddFirstElement_ReturnFalse() {
        System.out.println("isEmpty");
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name", new Object());
        boolean expResult = false;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIsEmpty_ReomveLastElement_ReturnTrue() {
        System.out.println("isEmpty");
        NamedObjectRepository instance = new NamedObjectRepository();
        instance.add("Name", new Object());
        instance.remove("Name");
        boolean expResult = true;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNamesList method, of class NamedObjectRepository.
     */
    @Test
    public void testGetNamesList_EmptyRepository_ReturnEmptyList() {
        System.out.println("getNamesList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List<String> expResult = new ArrayList<>();
        
        List<String> result = instance.getNamesList();
        
        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testGetNamesList_NonEmptyRepository_ReturnCorrectList() {
        System.out.println("getNamesList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List<String> expResult = new ArrayList<>();
        expResult.add("Name 1");
        expResult.add("Name 2");
        expResult.add("Name 3");
        expResult.stream().forEach(n -> instance.add(n, new Object()));
        
        List<String> result = instance.getNamesList();
        
        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testGetNamesList_AddNonExistingName_ReturnedListContainNewName() {
        System.out.println("getNamesList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List<String> expResult = new ArrayList<>();
        expResult.add("Name 1");
        expResult.add("Name 2");
        expResult.add("Name 3");
        expResult.stream().forEach(n -> instance.add(n, new Object()));
        
        List<String> result = instance.getNamesList();
        expResult.add("Name 4");
        instance.add("Name 4", new Object());

        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testGetNamesList_Rename_ReturnedListContainNewNameAtOldPlace() {
        System.out.println("getNamesList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List<String> expResult = new ArrayList<>();
        expResult.add("Name 1");
        expResult.add("Name 4");
        expResult.add("Name 3");
        instance.add("Name 1", new Object());
        instance.add("Name 2", new Object());
        instance.add("Name 3", new Object());
        
        List<String> result = instance.getNamesList();
        instance.rename("Name 2", "Name 4");

        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testGetNamesList_RemoveExistingName_ReturnedListNotContainTheName() {
        System.out.println("getNamesList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List<String> expResult = new ArrayList<>();
        expResult.add("Name 1");
        expResult.add("Name 3");
        instance.add("Name 1", new Object());
        instance.add("Name 2", new Object());
        instance.add("Name 3", new Object());
        List<String> result = instance.getNamesList();
        instance.remove("Name 2");

        assertThat(result, CoreMatchers.is(expResult));
    }

    /**
     * Test of getObjectsList method, of class NamedObjectRepository.
     */
    @Test
    public void testGetObjectsList_EmptyRepository_ReturnEmptyList() {
        System.out.println("getObjectsList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List expResult = new ArrayList<>();
        
        List result = instance.getObjectsList();
        
        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testGetObjectsList_NonEmptyRepository_ReturnCorrectList() {
        System.out.println("getObjectsList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List expResult = new ArrayList<>();
        expResult.add("Object 1");
        expResult.add("Object 2");
        expResult.add("Object 3");
        instance.add("Name 1", "Object 1");
        instance.add("Name 2", "Object 2");
        instance.add("Name 3", "Object 3");
        
        List result = instance.getObjectsList();
        
        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testGetObjectsList_AddNonExistingName_ReturnedListContainNewObject() {
        System.out.println("getObjectsList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List expResult = new ArrayList<>();
        expResult.add("Object 1");
        expResult.add("Object 2");
        expResult.add("Object 3");
        expResult.add("Object 4");
        instance.add("Name 1", "Object 1");
        instance.add("Name 2", "Object 2");
        instance.add("Name 3", "Object 3");
        
        List result = instance.getObjectsList();
        instance.add("Name 4", "Object 4");
        
        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testGetObjectsList_AddExistingName_ReturnedListContainNewObjectAtOldPlace() {
        System.out.println("getObjectsList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List expResult = new ArrayList<>();
        expResult.add("Object 1");
        expResult.add("Object 4");
        expResult.add("Object 3");
        instance.add("Name 1", "Object 1");
        instance.add("Name 2", "Object 2");
        instance.add("Name 3", "Object 3");
        
        List result = instance.getObjectsList();
        instance.add("Name 2", "Object 4");
        
        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testGetObjectsList_Rename_ReturnedListNotChanged() {
        System.out.println("getObjectsList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List expResult = new ArrayList<>();
        expResult.add("Object 1");
        expResult.add("Object 2");
        expResult.add("Object 3");
        instance.add("Name 1", "Object 1");
        instance.add("Name 2", "Object 2");
        instance.add("Name 3", "Object 3");
        
        List result = instance.getObjectsList();
        instance.rename("Name 2", "Name 4");
        
        assertThat(result, CoreMatchers.is(expResult));
    }
    
    @Test
    public void testGetObjectsList_RemoveExistingName_ReturnedListNotContainObject() {
        System.out.println("getObjectsList");
        NamedObjectRepository instance = new NamedObjectRepository();
        List expResult = new ArrayList<>();
        expResult.add("Object 1");
        expResult.add("Object 3");
        instance.add("Name 1", "Object 1");
        instance.add("Name 2", "Object 2");
        instance.add("Name 3", "Object 3");
        
        List result = instance.getObjectsList();
        instance.remove("Name 2");
        
        assertThat(result, CoreMatchers.is(expResult));
    }
}
