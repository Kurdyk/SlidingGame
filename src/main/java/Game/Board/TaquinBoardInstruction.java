package Game.Board;

public enum TaquinBoardInstruction {
    SWAP_UP,
    SWAP_RIGHT,
    SWAP_DOWN,
    SWAP_LEFT;

    public static TaquinBoardInstruction mapFromDirection(TaquinBoardDirection direction) {
        return switch (direction) {
            case UP -> SWAP_UP;
            case RIGHT -> SWAP_RIGHT;
            case DOWN -> SWAP_DOWN;
            case LEFT -> SWAP_LEFT;
        };
    }
}
