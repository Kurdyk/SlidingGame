package Game.Board;

/**
 * Abstraction of the possible actions from a given state on the board.
 * Each action implies that we are operating with a target cell and a neighbor of the cell in the indicated direction.
 */
public enum TaquinBoardAction {
    SWAP_UP,
    SWAP_RIGHT,
    SWAP_DOWN,
    SWAP_LEFT;

    /**
     * @param direction The direction in which we want to move the target cell
     * @return The ACTION that will result in the cell being moved in the target direction
     */
    public static TaquinBoardAction mapFromDirection(TaquinBoardDirection direction) {
        return switch (direction) {
            case UP -> SWAP_UP;
            case RIGHT -> SWAP_RIGHT;
            case DOWN -> SWAP_DOWN;
            case LEFT -> SWAP_LEFT;
        };
    }
}
