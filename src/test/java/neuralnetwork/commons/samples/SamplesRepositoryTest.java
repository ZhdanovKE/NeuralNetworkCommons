package neuralnetwork.commons.samples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for SamplesRepository class
 * @author Konstantin Zhdanov
 */
public class SamplesRepositoryTest {
    
    public SamplesRepositoryTest() {
    }

    /**
     * Test of add method, of class SamplesRepository.
     */
    @Test(expected = NullPointerException.class)
    public void testAdd_NullArgument_Throw() {
        System.out.println("add");
        SamplesRepository instance = new SamplesRepository();
        instance.add(null);
        
        fail("The test case must throw");
    }
    
    @Test
    public void testAdd_Called_SizeIncreaseByOne() {
        System.out.println("add");
        SamplesRepository instance = new SamplesRepository();
        List sample = new ArrayList<>();
        sample.add(new Object());
        sample.add(new Object());
        instance.add(sample);
        int oldSize = instance.size();
        List sampleTwo = new ArrayList<>();
        sampleTwo.add(new Object());
        sampleTwo.add(new Object());
        instance.add(sampleTwo);
        int newSize = instance.size();
        
        assertEquals(oldSize + 1, newSize);
    }

    /**
     * Test of remove method, of class SamplesRepository.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemove_IndexNegative_Throw() {
        System.out.println("remove");
        int idx = -1;
        SamplesRepository instance = new SamplesRepository();

        instance.remove(idx);
        
        fail("The test case must throw");
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemove_IndexEqualSize_Throw() {
        System.out.println("remove");
        int idx = 1;
        SamplesRepository instance = new SamplesRepository();
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        instance.add(sampleOne);
        
        instance.remove(idx);
        
        fail("The test case must throw");
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemove_IndexGreaterSize_Throw() {
        System.out.println("remove");
        int idx = 2;
        SamplesRepository instance = new SamplesRepository();
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        instance.add(sampleOne);
        
        instance.remove(idx);
        
        fail("The test case must throw");
    }
    
    @Test
    public void testRemove_CorrectIndex_ElementRemoved() {
        System.out.println("remove");
        int idx = 1;
        SamplesRepository instance = new SamplesRepository();
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        instance.add(sampleOne);
        List sampleTwo = new ArrayList<>();
        sampleTwo.add(new Object());
        sampleTwo.add(new Object());
        sampleTwo.add(new Object());
        instance.add(sampleTwo);
        List sampleThree = new ArrayList<>();
        sampleThree.add(new Object());
        sampleThree.add(new Object());
        sampleThree.add(new Object());
        instance.add(sampleThree);
        
        instance.remove(idx);
        
        assertEquals(2, instance.size());
        List<List<?>> all = instance.getAll();
        assertThat(all, CoreMatchers.hasItems(sampleOne, sampleThree));
        assertThat(all, CoreMatchers.not(CoreMatchers.hasItem(sampleTwo)));
    }

    /**
     * Test of setHeader method, of class SamplesRepository.
     */
    @Test
    public void testSetHeader_CalledOnEmptyRepository_ReturnCorrectList() {
        System.out.println("setHeader");
        SamplesRepository instance = new SamplesRepository();
        List<String> header = Arrays.asList("Name", "Age", "Address");
        
        instance.setHeader(header);
        
        assertThat(instance.getHeader(), CoreMatchers.is(header));
    }
    
    @Test
    public void testSetHeader_ValidSizedHeaderPassed_GetHeaderReturnCorrectList() {
        System.out.println("setHeader");
        SamplesRepository instance = new SamplesRepository();
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        instance.add(sampleOne);

        List<String> expected = Arrays.asList("Name", "Age", "Address");
        instance.setHeader(expected);
        
        List<String> result = instance.getHeader();
        
        assertThat(result, CoreMatchers.is(expected));
    }

    /**
     * Test of getHeader method, of class SamplesRepository.
     */
    @Test
    public void testGetHeader_EmptyRepository_ReturnEmptyList() {
        System.out.println("getHeader");
        SamplesRepository instance = new SamplesRepository();

        List<String> result = instance.getHeader();
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testGetHeader_ThreeVarSampleAdded_ReturnDefaultList() {
        System.out.println("getHeader");
        SamplesRepository instance = new SamplesRepository();
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        instance.add(sampleOne);
        List<String> expected = Arrays.asList("Var 1", "Var 2", "Var 3");
        
        List<String> result = instance.getHeader();
        
        assertThat(result, CoreMatchers.is(expected));
    }

    /**
     * Test of getSample method, of class SamplesRepository.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetSample_IndexNegative_Throw() {
        System.out.println("getSample");
        int idx = -1;
        SamplesRepository instance = new SamplesRepository();
        
        List result = instance.getSample(idx);
        
        fail("The test case must throw");
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetSample_IndexEqualSize_Throw() {
        System.out.println("getSample");
        int idx = 1;
        SamplesRepository instance = new SamplesRepository();
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        instance.add(sampleOne);
        
        List result = instance.getSample(idx);
        
        fail("The test case must throw");
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetSample_IndexGreaterSize_Throw() {
        System.out.println("getSample");
        int idx = 2;
        SamplesRepository instance = new SamplesRepository();
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        instance.add(sampleOne);
        
        List result = instance.getSample(idx);
        
        fail("The test case must throw");
    }
    
    @Test
    public void testGetSample_CorrectIndex_ReturnCorrectSample() {
        System.out.println("getSample");
        int idx = 1;
        SamplesRepository instance = new SamplesRepository();
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        instance.add(sampleOne);
        List sampleTwo = new ArrayList<>();
        sampleTwo.add(new Object());
        sampleTwo.add(new Object());
        instance.add(sampleTwo);
        
        List result = instance.getSample(idx);
        
        assertEquals(result, sampleTwo);
        for (int i = 0; i < sampleTwo.size(); i++) {
            assertEquals(result.get(i), sampleTwo.get(i));
        }
    }
    
    /**
     * Test of getAll method, of class SamplesRepository.
     */
    @Test
    public void testGetAll_EmptyRepository_ReturnEmptyList() {
        System.out.println("getAll");
        SamplesRepository instance = new SamplesRepository();

        List<List<?>> result = instance.getAll();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testGetAll_AddTwoSamples_ReturnListOfTheseSamples() {
        System.out.println("getAll");
        SamplesRepository instance = new SamplesRepository();
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        List sampleTwo = new ArrayList<>();
        sampleTwo.add(new Object());
        sampleTwo.add(new Object());
        instance.add(sampleOne);
        instance.add(sampleTwo);
        
        List<List<?>> result = instance.getAll();
        assertThat(result, CoreMatchers.hasItems(sampleOne, sampleTwo));
        assertEquals(2, result.size());
    }
    
    @Test
    public void testGetAll_RemoveOneSample_ReturnListNotContainTheSample() {
        System.out.println("getAll");
        SamplesRepository instance = new SamplesRepository();
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        List sampleTwo = new ArrayList<>();
        sampleTwo.add(new Object());
        sampleTwo.add(new Object());
        instance.add(sampleOne);
        instance.add(sampleTwo);
        instance.remove(0);
        
        List<List<?>> result = instance.getAll();
        assertThat(result, CoreMatchers.hasItems(sampleTwo));
        assertThat(result, CoreMatchers.not(CoreMatchers.hasItem(sampleOne)));
        assertEquals(1, result.size());
    }

    /**
     * Test of size method, of class SamplesRepository.
     */
    @Test
    public void testSize_EmptyRepository_ReturnZero() {
        System.out.println("size");
        SamplesRepository instance = new SamplesRepository();
        int expResult = 0;
        
        int result = instance.size();
        
        assertEquals(expResult, result);
    }

    @Test
    public void testSize_AddTwoSamples_ReturnTwo() {
        System.out.println("size");
        SamplesRepository instance = new SamplesRepository();
        int expResult = 2;
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        List sampleTwo = new ArrayList<>();
        sampleTwo.add(new Object());
        sampleTwo.add(new Object());
        instance.add(sampleOne);
        instance.add(sampleTwo);
        
        int result = instance.size();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSize_RemoveSample_ReturnPrevSizeMinusOne() {
        System.out.println("size");
        SamplesRepository instance = new SamplesRepository();
        int expResult = 1;
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        List sampleTwo = new ArrayList<>();
        sampleTwo.add(new Object());
        sampleTwo.add(new Object());
        instance.add(sampleOne);
        instance.add(sampleTwo);
        instance.remove(0);
        
        int result = instance.size();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of sampleSize method, of class SamplesRepository.
     */
    @Test
    public void testSampleSize_EmptyRepository_ReturnZero() {
        System.out.println("sampleSize");
        SamplesRepository instance = new SamplesRepository();
        int expResult = 0;
        
        int result = instance.sampleSize();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSampleSize_AddTwoSamples_ReturnTheSamplesSize() {
        System.out.println("sampleSize");
        SamplesRepository instance = new SamplesRepository();
        int expResult = 2;
        List sampleOne = new ArrayList<>();
        sampleOne.add(new Object());
        sampleOne.add(new Object());
        List sampleTwo = new ArrayList<>();
        sampleTwo.add(new Object());
        sampleTwo.add(new Object());
        instance.add(sampleOne);
        instance.add(sampleTwo);
        
        int result = instance.sampleSize();
        
        assertEquals(expResult, result);
    }
    

    /**
     * Test of isEmpty method, of class SamplesRepository.
     */
    @Test
    public void testIsEmpty_EmptyRepository_ReturnTrue() {
        System.out.println("isEmpty");
        SamplesRepository instance = new SamplesRepository();
        boolean expResult = true;
        
        boolean result = instance.isEmpty();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIsEmpty_AddSample_ReturnFalse() {
        System.out.println("isEmpty");
        SamplesRepository instance = new SamplesRepository();
        boolean expResult = false;
        List sample = new ArrayList<>();
        sample.add(new Object());
        sample.add(new Object());
        instance.add(sample);
        
        boolean result = instance.isEmpty();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIsEmpty_RemoveLastSample_ReturnTrue() {
        System.out.println("isEmpty");
        SamplesRepository instance = new SamplesRepository();
        boolean expResult = true;
        List sample = new ArrayList<>();
        sample.add(new Object());
        sample.add(new Object());
        instance.add(sample);
        instance.remove(0);
        
        boolean result = instance.isEmpty();
        
        assertEquals(expResult, result);
    }
}
