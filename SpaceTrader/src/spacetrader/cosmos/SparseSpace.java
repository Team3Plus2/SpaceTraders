package spacetrader.cosmos;

import spacetrader.cosmos.system.SolarSystem;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;



/**
 * Array is unbounded
 * 
 * Uses a embedded HashMap
 * 
 * @author Alex
 */
public class SparseSpace implements Iterable<SolarSystem> {
    private HashMap<Integer, HashMap<Integer, SolarSystem>> spaceMap;
    private int xMin, yMin, xMax, yMax;
    
    public SparseSpace() {
        spaceMap = new HashMap<>();
        xMin = 0;
        yMin = 0;
        xMax = 0;
        yMax = 0;
    }

    /**
     * Inserts a solar system at the given coordinates (null for deep space)
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param system The solar system to insert
     */
    public void insert(int x, int y, SolarSystem system) {
        if(spaceMap.containsKey(x)) {
            spaceMap.get(x).put(y, system);
        } else {
            HashMap<Integer, SolarSystem> ymap = new HashMap<Integer, SolarSystem>();
            ymap.put(y, system);
            spaceMap.put(x, ymap);
        }
        
        //at this point, insertion has been successful
        if(x < xMin)
            xMin = x;
        if(y < xMin)
            yMin = y;
        if(xMax < x)
            xMax = x;
        if(yMax < y)
            yMax = y;
    }
    
    /**
     * Returns the solar system at the given coordinate
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @return null if outer space, otherwise, the solar system at the given coordinates
     */
    public SolarSystem get(int x, int y) {
        HashMap<Integer, SolarSystem> ymap = spaceMap.get(x);
        if(ymap == null)
            return null;
        return ymap.get(y);
    }
    
    @Override
    public Iterator<SolarSystem> iterator() {
        return new SparseIterator(this, xMin, yMin, xMax, yMax);
    }
    
    public Iterator<SolarSystem> iterateFrom(int x0, int y0, int x1, int y1) {
        return new SparseIterator(this, x0, y0, x1, y1);
    }
    
    public int width() {
        return xMax - xMin;
    }
    
    public int height() {
        return yMax - yMin;
    }
    
    public int xMin() {
        return xMin;
    }
    
    public int yMin() {
        return xMin;
    }
    
    public int xMax() {
        return xMax;
    }
    
    public int yMax() {
        return yMax;
    }
}

class SparseIterator implements Iterator<SolarSystem> {
    private SparseSpace space;
    private int currX;
    private int currY;
    private int startX;
    private int toX;
    private int toY;
    
    public SparseIterator(SparseSpace toIterate, int fromX, int fromY, int toX, int toY) {
        this.space = toIterate;
        this.currX = fromX;
        this.currY = fromY;
        this.startX = fromX;
        this.toX = toX;
        this.toY = toY;
    }
    
    @Override
    public boolean hasNext() {
        return (currY <= toY);//note: x will never be larger than toX
    }
    
    @Override
    public SolarSystem next() {
        if(!hasNext())
            throw new NoSuchElementException();
        
        SolarSystem rit = space.get(currX, currY);
        currX++;
        
        if(currX == toX) {
            currX = startX;
            currY++;
        }
        
        return rit;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}