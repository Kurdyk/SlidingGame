package com.Game.Board;

import com.Game.Cell.CellUtilities;
import com.Game.Cell.Position;

import java.util.Arrays;

/**
 * Default implementation of TaquinBoardState.
 * Stores the board in a two-dimensional array of TaquinCells.
 * In this implementation, we imagine the coordinates of the board as the bottom right hand quadrant
 * of a standard two-dimensional graph, ignoring the negative sign.
 * Thus the first array represents the rows of the grid, and the second array the columns.
 * Thus we use the naming x and y for the indices into these arrays.
 */
public class DefaultBoardState extends TaquinBoardState {

    private final int size;

    private final short[][] boardImplementation;

    public DefaultBoardState(int size) {
        this.size = size;
        boardImplementation = new short[size][size];
    }

    public DefaultBoardState(DefaultBoardState boardState) {
        this.size = boardState.size;
        boardImplementation = new short[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                var toCopy = boardState.getAtPosition(j, i);
                boardImplementation[i][j] = toCopy;
            }
        }
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void addCell(Position position, short value) {
        boardImplementation[position.getY()][position.getX()] = value;
    }

    @Override
    public short getAtPosition(int x, int y) {
        return boardImplementation[y][x];
    }

    /**
     * This method results in an updated board state representing the transition after an action.
     * The implementation is simple in that it readily throws index exceptions, so the caller must either
     * expect the exception or check independently that the exception will not occur.
     *
     * @param action The action we want to perform in our state transition
     * @param target The target of that action
     */
    @Override
    public void processAction(TaquinBoardAction action, Position target) {
        short neighbor;
        short targetValue = boardImplementation[target.getY()][target.getX()];
        switch (action) {
            case SWAP_UP -> {
                neighbor = boardImplementation[target.getY() - 1][target.getX()];
                boardImplementation[target.getY() - 1][target.getX()] = targetValue;
                boardImplementation[target.getY()][target.getX()] = neighbor;
            }
            case SWAP_RIGHT -> {
                neighbor = boardImplementation[target.getY()][target.getX() + 1];
                boardImplementation[target.getY()][target.getX() + 1] = targetValue;
                boardImplementation[target.getY()][target.getX()] = neighbor;
            }
            case SWAP_DOWN -> {
                neighbor = boardImplementation[target.getY() + 1][target.getX()];
                boardImplementation[target.getY() + 1][target.getX()] = targetValue;
                boardImplementation[target.getY()][target.getX()] = neighbor;
            }
            case SWAP_LEFT -> {
                neighbor = boardImplementation[target.getY()][target.getX() - 1];
                boardImplementation[target.getY()][target.getX() - 1] = targetValue;
                boardImplementation[target.getY()][target.getX()] = neighbor;
            }
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }
    }

    @Override
    public short getNeighbor(TaquinBoardDirection direction, Position target) {
        short neighbor;
        switch (direction) {
            case UP -> neighbor = getAtPosition(target.getX(), target.getY() - 1);
            case RIGHT -> neighbor = getAtPosition(target.getX() + 1, target.getY());
            case DOWN -> neighbor = getAtPosition(target.getX(), target.getY() + 1);
            case LEFT -> neighbor = getAtPosition(target.getX() - 1, target.getY());
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        return neighbor;
    }

    @Override
    public boolean targetHasNeighbor(TaquinBoardDirection direction, Position target) {
        try {
            getNeighbor(direction, target);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public TaquinBoardState copy() {
        return new DefaultBoardState(this);
    }

    @Override
    public Position getEmptyPosition() {
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                if (CellUtilities.cellIsEmpty(getAtPosition(x, y))) {
                    return new Position(x, y);
                }
            }
        }

        throw new IllegalStateException("Board has no empty cell");
    }

    @Override
    public Position getPositionOfCell(short id) {
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                if (getAtPosition(x, y) == id) {
                    return new Position(x, y);
                }
            }
        }

        throw new IllegalStateException("Board is missing cell of id " + id);
    }

    @Override
    public boolean isGoalState() {
        var lastSeenValue = getAtPosition(0, 0);
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                var evaluating = getAtPosition(x, y);
                if (evaluating < lastSeenValue) {
                    return false;
                } else if (x == getSize() - 1 && y == getSize() - 1) {
                    return CellUtilities.cellIsEmpty(evaluating);
                }
                lastSeenValue = evaluating;
            }
        }

        throw new IllegalStateException("Invalid configuration when checking goal test");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultBoardState that = (DefaultBoardState) o;
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                if (getAtPosition(x, y) != that.getAtPosition(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardImplementation);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                stringBuilder.append(CellUtilities.getStrValueOfCell(getAtPosition(j, i))).append(", ");
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}
