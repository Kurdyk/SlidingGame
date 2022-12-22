package Game.Cell;

import Game.Board.TaquinBoardState;

public class CellUtilities {

    public static boolean cellIsEmpty(int value) {
        return value == TaquinBoardState.EMPTY_ID;
    }

    public static String getStrValueOfCell(int value) {
        return cellIsEmpty(value) ? " " : String.valueOf(value);
    }
}
