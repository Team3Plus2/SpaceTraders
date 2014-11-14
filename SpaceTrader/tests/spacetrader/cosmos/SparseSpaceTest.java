package spacetrader.cosmos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.xml.ObjectLoader;

/**
 *
 * @author William
 */
public class SparseSpaceTest {
    
    int numSolarSystems = 10;
    SolarSystem[] sys;

    public SparseSpaceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        ObjectLoader.loadAllObjects();
        sys = new SolarSystem[numSolarSystems];
        for(int i = 0; i < numSolarSystems; i++) {
            sys[i] = new SolarSystem();
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertCentral() {
        int x = 0;
        int y = 0;
        SparseSpace instance = new SparseSpace();
        instance.insert(x, y, sys[0]);
        assertEquals(instance.get(x, y),sys[0]);
        assertEquals(0, instance.xMax());
        assertEquals(0, instance.yMax());
        assertEquals(0, instance.xMin());
        assertEquals(0, instance.yMin());
    }
    
    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertQuad1() {
        int x = 1;
        int y = 1;
        SparseSpace instance = new SparseSpace();
        instance.insert(x, y, sys[0]);
        assertNull(instance.get(0,0));
        assertEquals(instance.get(x, y),sys[0]);
        assertEquals(1, instance.xMax());
        assertEquals(1, instance.yMax());
        assertEquals(1, instance.xMin());
        assertEquals(1, instance.yMin());
    }
    
    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertQuad2() {
        int x = -1;
        int y = 1;
        SparseSpace instance = new SparseSpace();
        instance.insert(x, y, sys[0]);
        assertNull(instance.get(0,0));
        assertEquals(instance.get(x, y),sys[0]);
        assertEquals(-1, instance.xMax());
        assertEquals(1, instance.yMax());
        assertEquals(-1, instance.xMin());
        assertEquals(1, instance.yMin());
    }
    
    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertQuad3() {
        int x = -1;
        int y = -1;
        SparseSpace instance = new SparseSpace();
        instance.insert(x, y, sys[0]);
        assertNull(instance.get(0,0));
        assertEquals(instance.get(x, y),sys[0]);
        assertEquals(-1, instance.xMax());
        assertEquals(-1, instance.yMax());
        assertEquals(-1, instance.xMin());
        assertEquals(-1, instance.yMin());
    }
    
    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertQuad4() {
        int x = 1;
        int y = -1;
        SparseSpace instance = new SparseSpace();
        instance.insert(x, y, sys[0]);
        assertNull(instance.get(0,0));
        assertEquals(instance.get(x, y),sys[0]);
        assertEquals(1, instance.xMax());
        assertEquals(-1, instance.yMax());
        assertEquals(1, instance.xMin());
        assertEquals(-1, instance.yMin());
    }
    
    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertLeftUp() {
        SparseSpace instance = new SparseSpace();
        instance.insert(0, 0, sys[0]);        
        instance.insert(1, 1, sys[1]);

        assertEquals(instance.get(0, 0),sys[0]);
        assertEquals(instance.get(1, 1),sys[1]);
        assertEquals(1, instance.xMax());
        assertEquals(1, instance.yMax());
        assertEquals(0, instance.xMin());
        assertEquals(0, instance.yMin());

    }
    
    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertLeftDown() {
        SparseSpace instance = new SparseSpace();
        instance.insert(0, 0, sys[0]);        
        instance.insert(1, -1, sys[1]);

        assertEquals(instance.get(0, 0),sys[0]);
        assertEquals(instance.get(1, -1),sys[1]);
        assertEquals(1, instance.xMax());
        assertEquals(0, instance.yMax());
        assertEquals(0, instance.xMin());
        assertEquals(-1, instance.yMin());
    }
    
    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertRightUp() {
        SparseSpace instance = new SparseSpace();
        instance.insert(0, 0, sys[0]);        
        instance.insert(-1, 1, sys[1]);

        assertEquals(instance.get(0, 0),sys[0]);
        assertEquals(instance.get(-1, 1),sys[1]);
        assertEquals(0, instance.xMax());
        assertEquals(1, instance.yMax());
        assertEquals(-1, instance.xMin());
        assertEquals(0, instance.yMin());
    }
    
    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertRightDown() {
        SparseSpace instance = new SparseSpace();
        instance.insert(0, 0, sys[0]);        
        instance.insert(-1, -1, sys[1]);

        assertEquals(instance.get(0, 0),sys[0]);
        assertEquals(instance.get(-1, -1),sys[1]);
        assertEquals(0, instance.xMax());
        assertEquals(0, instance.yMax());
        assertEquals(-1, instance.xMin());
        assertEquals(-1, instance.yMin());
    }
    
    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertDuplicate() {
        SparseSpace instance = new SparseSpace();
        instance.insert(0, 0, sys[0]);
        instance.insert(0, 0, sys[1]);        
        instance.insert(-1, -1, sys[2]);
        instance.insert(-1, -1, sys[3]);


        assertEquals(instance.get(0, 0),sys[1]);
        assertEquals(instance.get(-1, -1),sys[3]);
        assertEquals(0, instance.xMax());
        assertEquals(0, instance.yMax());
        assertEquals(-1, instance.xMin());
        assertEquals(-1, instance.yMin());

    }
    
    /**
     * Test of insert method, of class SparseSpace.
     */
    @Test
    public void testInsertSingleLarge() {
        SparseSpace instance = new SparseSpace();
        instance.insert(8953, 23452, sys[0]);


        assertEquals(instance.get(8953, 23452),sys[0]);
        assertEquals(8953, instance.xMax());
        assertEquals(23452, instance.yMax());
        assertEquals(8953, instance.xMin());
        assertEquals(23452, instance.yMin());

    }

}
