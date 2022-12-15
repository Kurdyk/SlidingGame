package Game.Board;

import Game.Cell.Position;
import Game.Cell.TaquinCell;
import Game.Solver.Heuristic;

public interface TaquinBoardState {
    int getSize();

    TaquinCell getAtPosition(int x, int y);

    TaquinCell getAtPosition(Position position);

    void processInstruction(TaquinBoardInstruction instruction, TaquinCell target);

    TaquinCell getNeighbor(TaquinBoardDirection direction, TaquinCell target);

    void addCell(TaquinCell cell);

    TaquinCell getEmptyPosition();

    int reportHeuristicValue(Heuristic heuristic);

    TaquinBoardState copy();

    boolean isGoalState();
}
