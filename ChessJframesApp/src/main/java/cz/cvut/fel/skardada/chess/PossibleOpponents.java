
package cz.cvut.fel.skardada.chess;

/**
 * PossibleOpponents is an enumeration of possible opponets.
 * @author Adam Škarda
 */
public enum PossibleOpponents {

    /**
     * local Hotseat opponet - that means a human on the same computer (and program instance)
     * @see Player_Human
     */
    local,

    /**
     * Computer opponent
     * @see Player_Computer
     */
    computer,

    /**
     * Human opponent on different machine. Not implemented!
     * 
     */
    internet,
}
