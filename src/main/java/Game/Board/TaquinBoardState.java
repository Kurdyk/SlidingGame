package Game.Board;

import Game.Cell.Position;
import Game.Cell.TaquinCell;

public interface TaquinBoardState {
    int getSize();

    TaquinCell getAtPosition(int x, int y);

    TaquinCell getAtPosition(Position position);

    void processInstruction(TaquinBoardInstruction instruction, TaquinCell target);

    TaquinCell getNeighbor(TaquinBoardDirection direction, TaquinCell target);

    void addCell(TaquinCell cell);
}
