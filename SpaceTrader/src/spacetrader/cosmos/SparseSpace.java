package spacetrader.cosmos;

import java.io.Serializable;
import spacetrader.cosmos.system.SolarSystem;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;



/**
 * This is a simple sparse array implementation, shouldn't really need any
 * further modifications.
 * 
 * Array is unbounded and uses an embedded HashMap
 * 
 * 
 * @author Alex
 */
public class SparseSpace implements Iterable<SolarSystem>, Serializable {
    private HashMap<Integer, HashMap<Integer, SolarSystem>> spaceMap;
    private int xMin;
    private int yMin;
    private int xMax;
    private int yMax;
    
    public SparseSpace() {
        spaceMap = new HashMap<>();
        xMin = 0;
        yMin = 0;
        xMax = 0;
        yMax = 0;
    }

    /**
     * Inserts a solar system at the given coordinates (null for deep space).
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param system The solar system to insert
     */
    public void insert(int x, int y, SolarSystem system) {
        //System.out.println(x + " " + y + ": " + system.Name());
        system.setX(x);
        system.setY(y);
        if (spaceMap.containsKey(x)) {
            spaceMap.get(x).put(y, system);
        } else {
            HashMap<Integer, SolarSystem> ymap = new HashMap<Integer, SolarSystem>();
            ymap.put(y, system);
            spaceMap.put(x, ymap);
        }
        
        //at this point, insertion has been successful
        if (x < xMin) {
            xMin = x;
        }
        if (y < yMin) {
            yMin = y;
        }
        if (xMax <= x) {
            xMax = x;
        }
        if (yMax <= y) {
            yMax = y;
        }
    }
    
    /**
     * Returns the solar system at the given coordinate.
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @return null if outer space, otherwise, the solar system at the given coordinates
     */
    public SolarSystem get(int x, int y) {
        HashMap<Integer, SolarSystem> ymap = spaceMap.get(x);
        if (ymap == null) {
            return null;
        }
        return ymap.get(y);
    }
    
    /**
     * Returns an iterator that will go from the bottom leftmost generated solarsystem
     * to the upper rightmost solar system or, more concisely put, will iterate row by row
     * across the box defined by the corners (xMin, yMin), (xMax, yMax).
     * 
     * @return an iterator that will behave as described above
     */
    @Override
    public SparseIterator iterator() {
        return new SparseIterator(this, xMin, yMin, xMax, yMax);
    }
    
    /**
     * 
     * Iterate from x0 and y0 to x1 and y1.
     * 
     * Note: x0 is less than x1 and y0 is less than y1
     * 
     * @param x0 lower left x coordinate
     * @param y0 lower left y coordinate
     * @param x1 upper right x coordinate
     * @param y1 upper right y coordinate
     * @return Iterator which will iterate row by row from the bottom left point to the upper right point (as a box defined by the points)
     */
    public SparseIterator iterateFrom(int x0, int y0, int x1, int y1) {
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
    
    public class SparseIterator implements Iterator<SolarSystem> {
        private SparseSpace space;
        private int currX;
        private int currY;
        private int startX;
        private int toX;
        private int toY;

        public SparseIterator(SparseSpace toIterate, int fromX, int fromY, int toX2, int toY2) {
            this.space = toIterate;
            this.currX = fromX;
            this.currY = fromY;
            this.startX = fromX;
            this.toX = toX2;
            this.toY = toY2;
        }

        /**
         * 
         * @return the x coordinate of current SolarSystem
         */
        public int getX() {
            return currX;
        }

        /**
         * 
         * @return the y coordinate of the current SolarSystem
         */
        public int getY() {
            return currY;
        }

        @Override
        public boolean hasNext() {
            return (currY <= toY); //note: x will never be larger than toX
        }

        @Override
        public SolarSystem next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            SolarSystem rit = space.get(currX, currY);
            currX++;

            if (currX > toX) {
                currX = startX;
                currY++;
            }

            return rit;
        }
    }
}

