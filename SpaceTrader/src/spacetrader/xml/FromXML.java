package spacetrader.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to denote a field to be read into by an XMLreader.
 * @author Alex
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FromXML {
    
    /**
     * Requires this field.
     */
    boolean required() default true;
    
}
