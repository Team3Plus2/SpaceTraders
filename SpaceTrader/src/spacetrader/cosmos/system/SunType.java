package spacetrader.cosmos.system;

import java.util.Random;

/**
 *
 * @author Alex
 */
public enum SunType {
    BINARY,
    RED_GIANT,
    SOL,
    WHITE_DWARF,
    BLACK_HOLE,
    PROTO;
    
    public static SunType random(Random rand) {
        SunType[] values = SunType.values();
        return values[rand.nextInt(values.length)];
    }
    
    public static SunType random() {
        return random(new Random());
    }
}
