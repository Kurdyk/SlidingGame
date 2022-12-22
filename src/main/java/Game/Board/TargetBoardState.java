package Game.Board;

import Game.Cell.Position;

public class TargetBoardState extends DefaultBoardState {

    public TargetBoardState(int size) {
        super(size);
        int id = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (y == size - 1 && x == size - 1) {
                    addCell(new Position(x, y), EMPTY_ID);
                } else {
                    addCell(new Position(x, y), id);
                    id++;
                }
            }
        }
    }
}
