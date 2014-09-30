package spacetrader.xml;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Loadable Type superclass helps centralize a lot of the code that all the classes
 * read in by XMLReader will need in order to be accessed as types.
 * 
 * @author Alex
 */
public class TypeLoader implements Comparable{
    
    private static HashMap<String, TypeWrapper> types;
    private static HashMap<Integer, HashMap<Class, TypeLoader>> indexes;
    private static HashMap<Class, Integer> typeCount;
    private static HashMap<Class, Object> defaults;//default type value, may be null
    private static HashMap<Class, ArrayList<TypeLoader>> listByType;
    
    
    public static void Load(Class type, String fileLocation, Object _default) {
        if(types == null) {//since these are always initialized together, only one check is needed
            defaults = new HashMap<>();
            typeCount = new HashMap<>();
            indexes = new HashMap<>();
            types = new HashMap<>();
            listByType = new HashMap<>();
        }
        XMLReader reader = new XMLReader(type, fileLocation);
        ArrayList<TypeLoader> loadedTypes = reader.read();
        
        /*
         Put all types in HashMap types and update indexes as items are added
        */
        int index = 0;
        for(TypeLoader a : loadedTypes) {
            types.put(a.getName(), new TypeWrapper(index, a));
            HashMap<Class, TypeLoader> currentIndexObject = indexes.get(index);
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
     * @return null if the given type is not defined, otherwise an object of the given type.  Note: object will need to be casted back into type from TypeLoader.
     */
    public static TypeLoader get(String name) {
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
     * @return null if the given type is not defined, otherwise a TypeLoader object of the given type
     */
    protected static TypeLoader get(int index, Class type) {
        HashMap<Class, TypeLoader> atThisIndex = indexes.get(index);
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
    public static int getIndex(TypeLoader typeToGet) {
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
    
    @FromXML
    private String name;

    /**
     * Needed for XMLReader
     */
    public TypeLoader() {
        
    }
    
    public TypeLoader(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public int compareTo(Object other) {
        if(other instanceof TypeLoader) {
            TypeLoader loadableOther = (TypeLoader)other;
            if(loadableOther.equals(this))
                return 0;
            TypeWrapper otherWrap = types.get(loadableOther.name);
            TypeWrapper thisWrap = types.get(this.name);
            if(otherWrap == null)
                throw new IllegalArgumentException("Other is not a valid type");
            if(thisWrap == null)
                throw new IllegalStateException("This is not a valid type even though this is a loaded type");
            return otherWrap.index - thisWrap.index;    
        }
        throw new IllegalArgumentException();
    }
    
}

class TypeWrapper {
    int index;
    TypeLoader type;

    public TypeWrapper(int id, TypeLoader obj) {
        index = id;
        type = obj;
    }

    @Override
    public boolean equals(Object other) {
        if(other.getClass().equals(TypeWrapper.class)) {
            TypeWrapper otherType = (TypeWrapper)other;
            if(otherType.type.getClass().equals(this.type.getClass()) 
                && other.hashCode() == other.hashCode())
                return true;
        } else if(other instanceof TypeLoader) {
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
