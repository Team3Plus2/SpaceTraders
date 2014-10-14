package spacetrader.cosmos.system;

import java.io.Serializable;
//import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

import spacetrader.xml.FromXML;
import spacetrader.xml.LoadedType;

/**
 *
 * @author Alex
 */
public class SunType extends LoadedType implements Serializable {
    
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
    private float r, g, b;
    
    @FromXML (required = false)
    private String image;
    
    /*@FromXML (required = false)
    private int clearSpaceAround;*/
    
    public boolean usesColor() {
        return image == null;
    }
    
    public boolean usesImage() {
        return image != null;
    }
    
    public float getR() {
        return r;
    }
    
    public float getG() {
        return g;
    }
    
    public float getB() {
        return b;
    }
    
    public String getImage() {
        return image;
    }
    
    /*public int getSpaceAround() {
        return clearSpaceAround;
    }*/
    
}
