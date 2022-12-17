package Game.Board;

import Game.Cell.CellFactory;
import Game.Cell.Position;
import Game.Cell.TaquinCell;

public abstract class TaquinBoardState {
    public abstract int getSize();

    public abstract TaquinCell getAtPosition(int x, int y);

    public abstract TaquinCell getAtPosition(Position position);

    public abstract void processInstruction(TaquinBoardInstruction instruction, TaquinCell target);

    public abstract TaquinCell getNeighbor(TaquinBoardDirection direction, TaquinCell target);

    public abstract boolean hasNeighbor(TaquinBoardDirection direction, TaquinCell target);

    public abstract void addCell(TaquinCell cell);

    public abstract TaquinCell getEmptyPosition();

    public abstract Position getPositionOfCell(int id);

    public abstract TaquinBoardState copy(CellFactory taquinCellFactory);

    public abstract boolean isGoalState();
}
