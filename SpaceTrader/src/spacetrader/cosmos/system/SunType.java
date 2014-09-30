package spacetrader.cosmos.system;

import javafx.scene.paint.Color;
import java.util.Random;

import spacetrader.xml.FromXML;
import spacetrader.xml.TypeLoader;

/**
 *
 * @author Alex
 */
public class SunType extends TypeLoader {
    
    private static final String sunTypeFileLocation = "objects/SunTypes.xml";
    
    public static void Load() {
        SunType.Load(SunType.class, sunTypeFileLocation, null);
    }
    
    public static SunType Default() {
        return (SunType)SunType.Default(SunType.class);
    }
    
    public static SunType random(Random rand) {
        return (SunType)SunType.get(rand.nextInt(SunType.size(SunType.class)), SunType.class);
    }
    
    public static SunType random() {
        return random(new Random());
    }
    
    @FromXML
    private Color color;
    
    public Color getColor() {
        return color;
    }
    
    /*BINARY,
    RED_GIANT,
    SOL,
    WHITE_DWARF,
    BLACK_HOLE,
    PROTO;*/
    

}
