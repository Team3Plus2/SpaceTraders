package spacetrader.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

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

/**
 * This class reads data from an XML file into an array of Class type
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
    
    public ArrayList<T> read() {
        ArrayList<Field> settableFields = new ArrayList<Field>();
        for(Field field : type.getDeclaredFields()) {
            if(field.getAnnotation(FromXML.class) != null)
                settableFields.add(field);
        }
        
        /*ArrayList<Constructor> constructors = new ArrayList<Constructor>();
        for(Constructor a : type.getConstructors())
            constructors.add(a);*/
        
        ArrayList<T> list = new ArrayList<T>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(file));
        } catch(ParserConfigurationException except) {
            System.err.println("Parser configuration error, see stack trace:");
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
                } catch(InstantiationException excep) {
                    System.err.println("Error creating item to return from xml read... see message:");
                    System.err.println(excep.getMessage());
                } catch(IllegalAccessException excep) {
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
                    try {
                        field.setAccessible(true);
                        String rawValue = elem.getElementsByTagName(field.getName()).item(0).getChildNodes().item(0).getNodeValue();
                        if(field.getType() == Integer.TYPE) {
                            field.set(item, Integer.valueOf(rawValue));
                        } else if(field.getType() == Double.TYPE) {
                            field.set(item, Double.valueOf(rawValue));
                        } else {
                            field.set(item, rawValue);
                        }
                        //field.set(item, value);
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
