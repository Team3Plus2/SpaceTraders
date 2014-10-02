package spacetrader.xml;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Loadable Type superclass helps centralize a lot of the code that all the classes
 * read in by XMLReader will need in order to be accessed as types.
 * 
 * Everything is implemented with access effeciency valued over loading and
 * storage effeciency-- ALL access methods (size and default methods included)
 * are O(1).
 * 
 * @author Alex
 */
public class LoadedType implements Comparable{
    
    private static HashMap<String, TypeWrapper> types;
    private static HashMap<Integer, HashMap<Class, LoadedType>> indexes;
    private static HashMap<Class, Integer> typeCount;
    private static HashMap<Class, Object> defaults;//default type value, may be null
    private static HashMap<Class, ArrayList<LoadedType>> listByType;
    
    /**
     * 
     * The type specified will be loaded with objects specified in
     * the xml file.
     * 
     * Note: Shouldn't need to be called directly outside of a subclass, use this method
     * in the subclass to create a prettier Load() method.
     * 
     * @param type the type being loaded
     * @param fileLocation the location of the xml file
     * @param _default the default value to be used for the given type

     */
    public static void Load(Class type, String fileLocation, Object _default) {
        if(types == null) {//since these are always initialized together, only one check is needed
            defaults = new HashMap<>();
            typeCount = new HashMap<>();
            indexes = new HashMap<>();
            types = new HashMap<>();
            listByType = new HashMap<>();
        }
        XMLReader reader = new XMLReader(type, fileLocation);
        ArrayList<LoadedType> loadedTypes = reader.read();
        
        /*
         Put all types in HashMap types and update indexes as items are added
        */
        int index = 0;
        for(LoadedType a : loadedTypes) {
            types.put(a.getName(), new TypeWrapper(index, a));
            HashMap<Class, LoadedType> currentIndexObject = indexes.get(index);
            if(currentIndexObject == null)
                currentIndexObject =  new HashMap<>();
            currentIndexObject.put(type, a);
            indexes.put(index, currentIndexObject);
            index++;
        }
        
        /*
          Update type count, type default, and type list
        */
        typeCount.put(type, index);
        defaults.put(type, _default);
        listByType.put(type, loadedTypes);
    }
    
    /**
     * @param type type to get an arraylist of
     * @return an arraylist of the given type, or null if the type has not been loaded yet
     */
    public static ArrayList getList(Class type) {
        return listByType.get(type);
    }
    
    /**
     * 
     * Gets a Type with the given name
     * 
     * @param name the name of the type
     * @return null if the given type is not defined, otherwise an object of the given type.  Note: object will need to be casted back into type from LoadedType.
     */
    public static LoadedType get(String name) {
        TypeWrapper typeForName = types.get(name);
        if(typeForName == null)
            return null;
        return typeForName.type;
    }
    
    /**
     * 
     * Gets a type with the given index and class
     * 
     * @param type the class of the type to get
     * @param index the index of the type to get
     * @return null if the given type is not defined, otherwise a LoadedType object of the given type
     */
    protected static LoadedType get(int index, Class type) {
        HashMap<Class, LoadedType> atThisIndex = indexes.get(index);
        if(atThisIndex == null)
            return null;
        return atThisIndex.get(type);
    }
    
    /**
     * 
     * Gets the index of the type with the given name
     * 
     * @param name the name of the type to get the index of
     * @return null if the given type is not defined, otherwise the index of the given type
     */
    public static int getIndex(String name) {
        return types.get(name).index;
    }
    
    /**
     * 
     * Gets the index of the given type
     * 
     * @param typeToGet the type to get
     * @return null if the given type is not defined, otherwise the index of the given type
     */
    public static int getIndex(LoadedType typeToGet) {
        return types.get(typeToGet.name).index;
    }
    
    /**
     * @param type the type to get the size of
     * @return the amount of types loaded
     */
    protected static int size(Class type) {
        return typeCount.get(type);
    }
    
    /**
     * @param type the type to get the default value of
     * @return the default value of the given type
     */
    protected static Object Default(Class type) {
        return defaults.get(type);
    }
    
    /**
     * All loaded types must have a name-- this is a basic principle of enums which
     * this class tries to mimick
     */
    @FromXML
    private String name;

    /**
     * Needed for XMLReader
     */
    public LoadedType() {
        
    }
    
    /**
     * basic constructor
     * 
     * @param name the name of the type
     */
    public LoadedType(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @return the name of the given type
     */
    public String getName() {
        return name;
    }
    
    /**
     * toString() has been overriden to provide similar functionality to enums
     * in that it will return its name
     * @return a string representation of this type
     */
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * 
     * Compares this type to another object
     * 
     * Note: This will work between different types both loaded as a LoadedType
     * 
     * @throws IllegalArgumentException Other is not a valid type due to either not being a LoadableType or not being Loaded
     * @throws IllegalStateException This object has not been loaded
     * @param other object to compare to
     * @return -1 if this is less than other, 0 if equal, and 1 if this is greater than other.
     */
    @Override
    public int compareTo(Object other) {
        if(other instanceof LoadedType) {
            LoadedType loadableOther = (LoadedType)other;
            if(loadableOther.equals(this))
                return 0;
            TypeWrapper otherWrap = types.get(loadableOther.name);
            TypeWrapper thisWrap = types.get(this.name);
            if(otherWrap == null)
                throw new IllegalArgumentException("Other is not a valid type");
            if(thisWrap == null)
                throw new IllegalStateException("This is not a valid type even though this is a loaded type");
            return thisWrap.index - otherWrap.index;    
        }
        throw new IllegalArgumentException();
    }
    
}

/**
 * This wrapper class is used to help make references to indexes and types faster
 * @author Alex
 */
class TypeWrapper {
    int index;
    LoadedType type;

    public TypeWrapper(int id, LoadedType obj) {
        index = id;
        type = obj;
    }

    @Override
    public boolean equals(Object other) {
        if(other.getClass().equals(TypeWrapper.class)) {
            TypeWrapper otherType = (TypeWrapper)other;
            if(otherType.type.getClass().equals(this.type.getClass()) 
                && this.hashCode() == other.hashCode())
                return true;
        } else if(other instanceof LoadedType) {
            return this.type.equals(other);
        } else if(other instanceof String) {
            return this.type.getName().equals(other);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return type.getName().hashCode();
    }
}
