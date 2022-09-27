package org.cis120.Pool2;

import java.awt.*;

/**
 * controls the turns and stores turn related data
 */
public class Turn {
    // control turns: ture is p1 and false is p2
    static boolean isPlayerOnesTurn = true;
    static Color p1Color = Color.BLACK;
    static Color p2Color = Color.BLACK;
    static boolean hasScoredThisTurn = false;

    public Turn() {
        isPlayerOnesTurn = true;
    }

    public static void setColors(Color p1, Color p2) {
        p1Color = p1;
        p2Color = p2;
    }

    public static Color getCurrentPlayerColor() {
        if (isPlayerOnesTurn) {
            return p1Color;
        }
        return p2Color;
    }

    public static void scoreStreak() {
        hasScoredThisTurn = true;
    }

    public static void changeTurn() {
        if (!hasScoredThisTurn) {
            isPlayerOnesTurn = !isPlayerOnesTurn;
        }
    }

    public static void reset() {
        p1Color = Color.BLACK;
        p2Color = Color.BLACK;
        hasScoredThisTurn = false;
        isPlayerOnesTurn = true;
    }
}
