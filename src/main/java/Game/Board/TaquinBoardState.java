package Game.Board;

import Game.Cell.CellFactory;
import Game.Cell.Position;
import Game.Cell.TaquinCell;

/**
 * Abstraction of the state of the board.
 * The state represents an NxN grid of values. Each value is represented by a TaquinCell.
 * The state of the board can be read via querying the position of a cell.
 * This abstraction allows us to hide the implementation details of a board, permitting rapid iteration on
 * implementations of the state, and reducing overhead when refactoring.
 */
public abstract class TaquinBoardState {
    public abstract int getSize();

    public abstract TaquinCell getAtPosition(int x, int y);

    /**
     * This represents the transition function of our formalization
     *
     * @param action The action to operate on our current state
     * @param target The target cell of the action,
     *               For this action to be valid, it must be either the empty cell or a neighbor of the empty cell.
     */
    public abstract void processAction(TaquinBoardAction action, TaquinCell target);

    public abstract TaquinCell getNeighbor(TaquinBoardDirection direction, TaquinCell target);

    public abstract boolean targetHasNeighbor(TaquinBoardDirection direction, TaquinCell target);

    public abstract void addCell(TaquinCell cell);

    public abstract TaquinCell getEmptyPosition();

    public abstract Position getPositionOfCell(int id);

    public abstract TaquinBoardState copy(CellFactory taquinCellFactory);

    public abstract boolean isGoalState();
}
