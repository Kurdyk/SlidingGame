package com.Game.Board;

import com.Game.Cell.Position;

/**
 * Abstraction of the state of the board.
 * The state represents an NxN grid of values. Each value is represented by a short.
 * The state of the board can be read via querying the position of a cell.
 * This abstraction allows us to hide the implementation details of a board, permitting rapid iteration on
 * implementations of the state, and reducing overhead when refactoring.
 */
public abstract class TaquinBoardState {
    public static short EMPTY_ID = 1013;

    public abstract int getSize();

    public abstract short getAtPosition(int x, int y);

    /**
     * This represents the transition function of our formalization
     *
     * @param action The action to operate on our current state
     * @param target The target cell of the action,
     *               For this action to be valid, it must be either the empty cell or a neighbor of the empty cell.
     */
    public abstract void processAction(TaquinBoardAction action, Position target);

    public abstract short getNeighbor(TaquinBoardDirection direction, Position target);

    public abstract boolean targetHasNeighbor(TaquinBoardDirection direction, Position target);

    public abstract void addCell(Position position, short value);

    public abstract Position getEmptyPosition();

    public abstract Position getPositionOfCell(short id);

    public abstract TaquinBoardState copy();

    public abstract boolean isGoalState();
}
