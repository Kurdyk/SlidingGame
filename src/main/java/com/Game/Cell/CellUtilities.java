package com.Game.Cell;

import com.Game.Board.TaquinBoardState;

/**
 * Helper class to put common cell behavior in one place.
 * This utility allows us to easily modify the string value or
 */
public class CellUtilities {

    public static boolean cellIsEmpty(int value) {
        return value == TaquinBoardState.EMPTY_ID;
    }

    public static String getStrValueOfCell(int value) {
        return cellIsEmpty(value) ? " " : String.valueOf(value);
    }
}