/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.turns;

import spacetrader.player.Player;

/**
 *
 * @author Alex
 */
public interface TurnListener {
    
    /**
     * This method should handle everything that must occur on an occurrence of a new turn.
     * @param player player that is the subject of the turn
     */
    void handleNextTurn(Player player);
    
}
