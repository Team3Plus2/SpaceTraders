package spacetrader.xml;

import java.lang.reflect.Field;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import spacetrader.cosmos.system.*;

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
    
    private Class type;
    private String file;
    
    public XMLReader(Class<? extends T> type, String file) {
        this.type = type;
        this.file = file;
    }
    
    /**
     * read the xml file into an ArrayList<T>
     * 
     * @return the arraylist built from the data in the file
     * @throws XMLObjectMismatchException if a required field is missing in the xml
     */
    public ArrayList<T> read() {
        ArrayList<Field> settableFields = new ArrayList<>();
        for(Field field : type.getDeclaredFields()) {
            if(field.getAnnotation(FromXML.class) != null)
                settableFields.add(field);
        }
        
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
                    item = (T)type.newInstance();
                } catch(InstantiationException | IllegalAccessException excep) {
                    System.err.println("Error creating item to return from xml read... see message:");
                    System.err.println(excep.getMessage());
                }
                
                //read constructor data
                /*for(Constructor a : constructors) {
                    for(Parameter param : a.getParameters()) {
                        param.
                    }
                }*/
                
                for(Field field : settableFields) {
                    if(elem.getElementsByTagName(field.getName()).getLength() == 0) { //if field is missing
                        if(field.getAnnotation(FromXML.class).required())
                            throw new XMLObjectMismatchException(field, localFile);
                        else
                            continue;
                    }
                    
                    try {
                        field.setAccessible(true);
                        String rawValue = elem.getElementsByTagName(field.getName()).item(0).getChildNodes().item(0).getNodeValue();
                        
                        /*
                        
                        If you want to handle custom datatypes or I forgot to handle some other datatype,
                        then add the code for that here (should be really straightforward).  Also, if you can come up
                        with a better way to do this feel free to either tell me to implement it or implement it yourself.
                        -Alex
                       
                        */
                        
                        if(field.getType().equals(Integer.TYPE)) {
                            field.set(item, Integer.valueOf(rawValue));
                        } else if(field.getType().equals(Double.TYPE)) {
                            field.set(item, Double.valueOf(rawValue));
                        } else if(field.getType().equals(Float.TYPE)) {
                            field.set(item, Float.valueOf(rawValue));
                        } else if(field.getType().equals(TechLevel.class)) {
                            try {
                                field.set(item, TechLevel.valueOf(rawValue));
                            } catch(IllegalArgumentException excep) {
                                field.set(item, TechLevel.values()[Integer.valueOf(rawValue)]);
                            }
                        } else if(field.getType().equals(Resources.class)) {
                            try {
                                field.set(item, Resources.valueOf(rawValue));
                            } catch(IllegalArgumentException excep) {
                                field.set(item, Resources.values()[Integer.valueOf(rawValue)]);
                            }
                        } else if(field.getType().equals(SunType.class)) {
                            try {
                                field.set(item, SunType.valueOf(rawValue));
                            } catch(IllegalArgumentException excep) {
                                field.set(item, SunType.values()[Integer.valueOf(rawValue)]);
                            }
                        } else if(field.getType().equals(Government.class)) {
                            try {
                                field.set(item, Government.valueOf(rawValue));
                            } catch(IllegalArgumentException excep) {
                                field.set(item, Government.values()[Integer.valueOf(rawValue)]);
                            }
                        } else {
                            field.set(item, rawValue);
                        }

                        
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

}
