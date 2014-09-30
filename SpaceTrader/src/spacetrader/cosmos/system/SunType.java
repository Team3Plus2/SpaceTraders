package spacetrader.cosmos.system;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

import spacetrader.xml.FromXML;
import spacetrader.xml.LoadedType;

/**
 *
 * @author Alex
 */
public class SunType extends LoadedType {
    
    private static final String sunTypeFileLocation = "objects/SunTypes.xml";
    
    public static void Load() {
        SunType.Load(SunType.class, sunTypeFileLocation, null);
    }
    
    public static SunType Default() {
        return (SunType)SunType.Default(SunType.class);
    }
    
    public static SunType random(Random rand) {
        ArrayList<SunType> types = SunType.getList(SunType.class);
        float totalChance = 0.0f;
        
        for(SunType a : types) {
            totalChance += a.chance;
        }
        
        float diceRoll = rand.nextFloat() * totalChance;
        totalChance = 0;
        
        for(SunType a : types) {
            totalChance += a.chance;
            if(diceRoll < totalChance)
                return a;
        }
        
        return types.get(types.size() - 1);
    }
    
    public static SunType random() {
        return random(new Random());
    }
    
    @FromXML
    private float chance;
    
    @FromXML (required = false)
    private Color color;
    
    @FromXML (required = false)
    private Image image;
    
    public boolean usesColor() {
        return image == null;
    }
    
    public boolean usesImage() {
        return image != null;
    }
    
    public Color getColor() {
        return color;
    }
    
    public Image getImage() {
        return image;
    }
    
}
