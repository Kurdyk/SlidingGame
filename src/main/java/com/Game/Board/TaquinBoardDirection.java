package com.Game.Board;

/**
 * Abstraction representing the cardinal directions on the board.
 */
public enum TaquinBoardDirection {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public TaquinBoardDirection mapFromOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case RIGHT -> LEFT;
            case DOWN -> UP;
            case LEFT -> RIGHT;
        };
    }
}
