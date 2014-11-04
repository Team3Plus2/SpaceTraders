package spacetrader.xml;

import java.lang.reflect.Field;
import java.io.File;

/**
 * This exception is thrown whenever a required field for a particular object cannot be found in the
 * provided xml document.
 * 
 * @author Alex
 */
public class XMLObjectMismatchException extends RuntimeException {
    
    /**
     * the name of the field that errored out.
     */
    private String fieldName;
    
    /**
     * the name of the object that had the load error.
     */
    private String objectName;
    
    /**
     * the location of the file with the error in the machine.
     */
    private String fileMachineLocation;
    
    /**
     * Construct the exception.
     * 
     * @param field field that could not be read into
     * @param xmlFile file that was being read from
     */
    public XMLObjectMismatchException(Field field, File xmlFile) {
        super("Required field " + field.getName() + " for object " + field.getDeclaringClass().getName() + " not found in file:\n" + xmlFile.getAbsolutePath());
        fieldName = field.getName();
        objectName = field.getDeclaringClass().getName();
        fileMachineLocation = xmlFile.getAbsolutePath();
    }

    /**
     * @return the name of the field that could not be read into.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return the name of the object that was being read into.
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * @return the location of the file with the error in the machine.
     */
    public String getFileMachineLocation() {
        return fileMachineLocation;
    }

}
