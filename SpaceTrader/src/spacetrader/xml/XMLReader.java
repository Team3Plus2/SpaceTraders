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
        ArrayList<Field> fields = new ArrayList<Field>();
        for(Field field : type.getDeclaredFields()) {
            if(field.getAnnotation(FromXML.class) != null)
                fields.add(field);
        }
        
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
                
                for(Field field : fields) {
                    
                }
            }
        }
        
        return list;
    
    }
    
}
