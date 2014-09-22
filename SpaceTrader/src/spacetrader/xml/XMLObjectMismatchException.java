package spacetrader.xml;

import java.lang.reflect.Field;
import java.io.File;

/**
 * This exception is thrown whenever a required field for a particular object cannot be found in the
 * provided xml document
 * 
 * @author Alex
 */
public class XMLObjectMismatchException extends RuntimeException{
    
    private String fieldName;
    private String objectName;
    private String fileMachineLocation;
    
    public XMLObjectMismatchException(Field field, File xmlFile) {
        super("Required field " + field.getName() + " for object " + field.getDeclaringClass().getName() + " not found in file:\n" + xmlFile.getAbsolutePath());
        fieldName = field.getName();
        objectName = field.getDeclaringClass().getName();
        fileMachineLocation = xmlFile.getAbsolutePath();
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getFileMachineLocation() {
        return fileMachineLocation;
    }

}
