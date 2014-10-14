package spacetrader.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import spacetrader.cosmos.system.*;
import spacetrader.player.ShipType;
import spacetrader.economy.TradeGood;

/**
 * This class reads data from an XML file into an array of Class type
 * 
 * Note that this reader does not read for non-default constructors, so it is highly recommended that
 * objects you wish to be initialized are able to operate without having a parameterized constructor call.
 * That said, this reader has been created with information holder type objects in mind, and for these kinds
 * of objects, this shouldn't be an issue.
 * 
 * @param <T> Type to read into
 * @author Alex
 */
public class XMLReader<T> {
    
    private static void DefaultWarning(String rawValue, String def) {
        System.err.println("Warning: Could not find image '" + rawValue + "', reverting to default value '" + def + "'");
    }
    
    private Class type;
    private String file;
    
    public XMLReader(Class<? extends T> type, String file) {
        this.type = type;
        this.file = file;
    }
    
    /**
     * read the xml file into an ArrayLis
     * 
     * @return the arraylist built from the data in the file
     * @throws XMLObjectMismatchException if a required field is missing in the xml
     */
    public ArrayList<T> read() {
        ArrayList<Field> settableFields = new ArrayList<>();
        Class tmpType = type;
        do {
            for(Field field : tmpType.getDeclaredFields()) {
                if(field.getAnnotation(FromXML.class) != null)
                    settableFields.add(field);
            }
            tmpType = tmpType.getSuperclass();
        } while (tmpType != null);
        
        /*ArrayList<Constructor> constructors = new ArrayList<Constructor>();
        for(Constructor a : type.getConstructors())
            constructors.add(a);*/
        
        ArrayList<T> list = new ArrayList<>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        File localFile = new File(file);
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(localFile);
        } catch(ParserConfigurationException except) {
            System.err.println("Parser configuration error, see message:");
            System.err.println(except.getMessage());
            return null;
        } catch (SAXException except) {
            System.err.println("SAX error, see message:");
            System.err.println(except.getMessage());
            return null;
        } catch (IOException except) {
            System.err.println("IO error, file may not be found, see message:");
            System.err.println(except.getMessage());
            return null;
        }
        
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                T item = null;
                try {
                    Constructor construct = type.getConstructor();
                    construct.setAccessible(true);
                    item = (T)construct.newInstance();
                    construct.setAccessible(false);
                    //item = (T)type.newInstance();
                } catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException excep) {
                    System.err.println("Error creating item to return from xml read... see message:");
                    System.err.println(excep.getMessage());
                    continue;
                }
                
                //read constructor data
                /*for(Constructor a : constructors) {
                    for(Parameter param : a.getParameters()) {
                        param.
                    }
                }*/
                
                for(Field field : settableFields) {
                    
                    if(field.getName().equals("name")) {
                        try {
                            field.setAccessible(true);
                            field.set(item, elem.getNodeName());
                            field.setAccessible(false);
                        } catch(IllegalAccessException excep) {
                            System.err.println("Error setting primary name field in created item to return from xml... see message:");
                            System.err.println(excep.getMessage());
                        }
                        continue;
                    }
                    
                    if(elem.getElementsByTagName(field.getName()).getLength() == 0) { //if field is missing
                        if(field.getAnnotation(FromXML.class).required())
                            throw new XMLObjectMismatchException(field, localFile);
                        else
                            continue;
                    }
                    
                    try {
                        field.setAccessible(true);
                        String rawValue = elem.getElementsByTagName(field.getName()).item(0).getChildNodes().item(0).getNodeValue();
                        
                        field.set(item, parse(field.getType(), rawValue));
                        
                        field.setAccessible(false);
                    } catch(IllegalAccessException excep) {
                        System.err.println("Error setting fields in created item to return from xml... see message:");
                        System.err.println(excep.getMessage());
                    } catch(IllegalArgumentException except) {
                        System.err.println("Error reading value from xml into object- type mismatch");
                        throw except;
                    }  catch(ClassCastException except) {
                        System.err.println("Error converting value from xml into field type");
                        throw except;
                    }
                }
                list.add(item);
            }
        }
        
        return list;
    
    }
    
    
    /**
     *  If you want to handle custom datatypes or I forgot to handle some other datatype,
     *  then add the code for that here
     *  @param type the type to parse to
     *  @param rawValue value to parse
     */
    private Object parse(Class type, String rawValue) {
        if(type.equals(Integer.TYPE)) {
            return Integer.valueOf(rawValue);
        } else if(type.equals(Double.TYPE)) {
            return Double.valueOf(rawValue);
        } else if(type.equals(Float.TYPE)) {
            return Float.valueOf(rawValue);
        } else if(type.equals(Boolean.TYPE)) {
          return Boolean.valueOf(rawValue);
        } else if(type.equals(TechLevel.class)) {
            try {
                TechLevel level = (TechLevel)TechLevel.get(rawValue);
                if(level == null)
                    level = (TechLevel)TechLevel.get(Integer.valueOf(rawValue), TechLevel.class);
                if(level == null)//if the level is still null, the xml value is invalid
                    System.err.println("The value " + rawValue + "is not a valid techLevel");
                return level;
            } catch(IllegalArgumentException excep) {
                DefaultWarning(rawValue, TechLevel.Default().toString());
                return TechLevel.Default();
            }
        } else if(type.equals(Resource.class)) {
            try {
                return Resource.get(rawValue);
            } catch(IllegalArgumentException excep) {
                DefaultWarning(rawValue, "NO_SPECIAL_RESOURCES");
                return Resource.get("NO_SPECIAL_RESOURCES");
            }
        } else if(type.equals(SunType.class)) {
            try {
                return SunType.get(rawValue);
            } catch(IllegalArgumentException excep) {
                DefaultWarning(rawValue, SunType.Default().toString());
                return SunType.Default();
            }
        } else if(type.equals(Government.class)) {
            try {
                return Government.get(rawValue);
            } catch(IllegalArgumentException excep) {
                DefaultWarning(rawValue, "");
                return Government.get("");
            }
        } else if(type.equals(ShipType.class)) {
            try {
                return ShipType.get(rawValue);
            } catch(IllegalArgumentException excep) {
                DefaultWarning(rawValue, "");
                return ShipType.Default();
            }
        } else if(type.equals(Color.class)) {
            try {
                return Color.valueOf(rawValue);
            } catch(IllegalArgumentException excep) {
                DefaultWarning(rawValue, Color.WHITE.toString());
                return Color.WHITE;
            }
        } else if(type.equals(TradeGood.class)) {
            try {
                return TradeGood.get(rawValue);
            } catch(IllegalArgumentException excep) {
                DefaultWarning(rawValue, null);
                return null;
            }
        } else if(type.equals(Image.class)) {
            try {
                return new Image(rawValue);
            } catch(IllegalArgumentException excep) {
                DefaultWarning(rawValue, null);
                return null;
            } 
        } else if(type.isArray()) {
            try {
                String[] items = rawValue.split(",");
                Class paramClass = type.getComponentType();
                
                Object typarr = Array.newInstance(paramClass, items.length);//needed to properly cast array
                for(int i = 0; i< items.length; i++) {
                    Array.set(typarr, i, parse(paramClass, items[i]));
                }
                
                Object[] standardArr = (Object[]) typarr;
                
                return type.cast(standardArr);
            } catch(IllegalArgumentException excep) {
                DefaultWarning(rawValue, null);
                return null;
            } /*catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }*/
        } else {
            return rawValue;
        }
    }
}