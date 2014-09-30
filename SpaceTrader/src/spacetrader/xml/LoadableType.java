/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.xml;

/**
 *
 * @author Alex
 */
public interface LoadableType {
    
    public void initialize(Class typ, String xmlFile, Object _default);
    
    public void Load();
    
    /**
     * 
     * Gets a Type with the given name
     * 
     * @param name the name of the type
     * @return null if the given type is not defined, otherwise an object of the given type.  Note: object will need to be casted back into type from TypeLoader.
     */
    public TypeLoader get(String name);
    
    /**
     * 
     * Gets a type with the given index
     * 
     * @param index the index of the type to get
     * @return null if the given type is not defined, otherwise a TypeLoader object of the given type
     */
    public TypeLoader get(int index);
    
    /**
     * 
     * Gets the index of the type with the given name
     * 
     * @param name the name of the type to get the index of
     * @return null if the given type is not defined, otherwise the index of the given type
     */
    public int getIndex(String name);
    
    /**
     * 
     * Gets the index of the given type
     * 
     * @param typeToGet the type to get
     * @return null if the given type is not defined, otherwise the index of the given type
     */
    public int getIndex(TypeLoader typeToGet);
    
    /**
     * 
     * @return the amount of types loaded
     */
    public int size();
    
    public Object Default();
    
}
