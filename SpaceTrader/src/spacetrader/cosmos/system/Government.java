package spacetrader.cosmos.system;

import java.util.Random;

/**
 *
 * @author Alex
 */
public enum Government {
    ANARCHY,
    CAPITALIST_STATE,
    COMUNIST_STATE,
    CONFEDERAY,
    CORPORATE_STATE,
    CYBERNETIC_STATE,
    DEMOCRACY,
    DICTATORSHIP,
    FASCIST_STATE,
    FEUDAL_STATE,
    MILITARY_STATE,
    MONARCHY,
    PACIFIST_STATE,
    SOCIALIST_STATE,
    TECHNOCRACY,
    THEOCRACY;
    
    public static Government random(Random rand) {
        Government[] values = Government.values();
        return values[rand.nextInt(values.length)];
    }
    
    public static Government random() {
        return random(new Random());
    }
}
