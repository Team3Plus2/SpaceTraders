package spacetrader.xml;

import java.util.ArrayList;

/**
 *
 * Loadable Type superclass helps centralize a lot of the code that all the classes
 * read in by XMLReader will need in order to be accessed as types.
 * 
 * @param <T> the type of object being loaded
 * @author Alex
 */
public class LoadableType<T> implements Comparable{
    
    private static ArrayList<LoadableType> types;
    private static String fileLocation;
    
    //also the type of object being loaded, the generic is used for compiletime, but this is needed for runtime type operations
    private static Class type;
    private static Object def;//default type value, may be null
    
    protected static void initialize(Class typ, String xmlFile, Object _default) {
        fileLocation = xmlFile;
        type = typ;
        def = _default;
    }
    
    public static void Load() {
        XMLReader reader = new XMLReader(type, fileLocation);
        types = reader.read();
    }
    
    /**
     * 
     * Gets a Type with the given name
     * 
     * @param name the name of the type
     * @return null if the given type is not defined, otherwise an object of the given type.  Note: object will need to be casted back into type from LoadableType.
     */
    public static LoadableType get(String name) {
        /*for(LoadableType a : types) {
            if(a.name.equals(name))
                return a;
        }*/
        return null;
    }
    
    /**
     * 
     * Gets a type with the given index
     * 
     * @param index the index of the type to get
     * @return null if the given type is not defined, otherwise a LoadableType object of the given type
     */
    public static LoadableType get(int index) {
        if(index >= types.size())
            return null;
        return types.get(index);
    }
    
    /**
     * 
     * @return the amount of types loaded
     */
    public static int size() {
        return types.size();
    }
    
    public static Object Default() {
        return def;
    }
    
    @FromXML
    private String name;

    @Override
    public int compareTo(Object other) {
        if(other instanceof LoadableType) {
            LoadableType loadableOther = (LoadableType)other;
            if(loadableOther.name.equals(this.name))
                return 0;
            for(LoadableType a : types) {
                if(a.name.equals(loadableOther.name))
                    return -1;
                if(a.name.equals(this.name))
                    return 1;
            }
        }
        throw new IllegalArgumentException();
    }
}
