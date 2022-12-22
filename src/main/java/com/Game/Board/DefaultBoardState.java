package com.Game.Board;

import com.Game.Cell.CellFactory;
import com.Game.Cell.Position;
import com.Game.Cell.TaquinCell;

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

    private final TaquinCell[][] boardImplementation;

    public DefaultBoardState(int size) {
        this.size = size;
        boardImplementation = new TaquinCell[size][size];
    }

    public DefaultBoardState(DefaultBoardState boardState, CellFactory taquinCellFactory) {
        this.size = boardState.size;
        boardImplementation = new TaquinCell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                var toCopy = boardState.getAtPosition(j, i);
                boardImplementation[i][j] = taquinCellFactory.createTaquinCell(
                        toCopy.getCellId(),
                        toCopy.getPosition().getX(),
                        toCopy.getPosition().getY()
                );
            }
        }
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void addCell(TaquinCell cell) {
        Position targetPosition = cell.getPosition();
        boardImplementation[targetPosition.getY()][targetPosition.getX()] = cell;
    }

    @Override
    public TaquinCell getAtPosition(int x, int y) {
        return boardImplementation[y][x];
    }

    private void setAtPosition(Position position, TaquinCell target) {
        boardImplementation[position.getY()][position.getX()] = target;
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
    public void processAction(TaquinBoardAction action, TaquinCell target) {
        TaquinCell neighbor;
        switch (action) {
            case SWAP_UP -> neighbor = getNeighbor(TaquinBoardDirection.UP, target);
            case SWAP_RIGHT -> neighbor = getNeighbor(TaquinBoardDirection.RIGHT, target);
            case SWAP_DOWN -> neighbor = getNeighbor(TaquinBoardDirection.DOWN, target);
            case SWAP_LEFT -> neighbor = getNeighbor(TaquinBoardDirection.LEFT, target);
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }
        Position tempNewPosition = neighbor.getPosition();
        Position tempTargetPosition = target.getPosition();

        target.setPosition(tempNewPosition);
        setAtPosition(tempNewPosition, target);

        neighbor.setPosition(tempTargetPosition);
        setAtPosition(tempTargetPosition, neighbor);
    }

    @Override
    public TaquinCell getNeighbor(TaquinBoardDirection direction, TaquinCell target) {
        TaquinCell neighbor;
        Position currentPosition = target.getPosition();
        switch (direction) {
            case UP -> neighbor = getAtPosition(currentPosition.getX(), currentPosition.getY() - 1);
            case RIGHT -> neighbor = getAtPosition(currentPosition.getX() + 1, currentPosition.getY());
            case DOWN -> neighbor = getAtPosition(currentPosition.getX(), currentPosition.getY() + 1);
            case LEFT -> neighbor = getAtPosition(currentPosition.getX() - 1, currentPosition.getY());
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        return neighbor;
    }

    @Override
    public boolean targetHasNeighbor(TaquinBoardDirection direction, TaquinCell target) {
        try {
            getNeighbor(direction, target);
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    @Override
    public TaquinBoardState copy(CellFactory taquinCellFactory) {
        return new DefaultBoardState(this, taquinCellFactory);
    }

    @Override
    public TaquinCell getEmptyPosition() {
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                if (getAtPosition(x, y).isEmpty()) {
                    return getAtPosition(x, y);
                }
            }
        }

        throw new IllegalStateException("Board has no empty cell");
    }

    @Override
    public Position getPositionOfCell(int id) {
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                if (getAtPosition(x, y).getCellId() == id) {
                    return getAtPosition(x, y).getPosition();
                }
            }
        }

        throw new IllegalStateException("Board is missing cell of id " + id);
    }

    @Override
    public boolean isGoalState() {
        var lastSeenValue = getAtPosition(0, 0).getCellId();
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                var evaluating = getAtPosition(x, y);
                if (evaluating.getCellId() < lastSeenValue) {
                    return false;
                } else if (x == getSize() - 1 && y == getSize() - 1) {
                    return evaluating.isEmpty();
                }
                lastSeenValue = evaluating.getCellId();
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
                if (getAtPosition(x, y).getCellId() != that.getAtPosition(x, y).getCellId()) {
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
                stringBuilder.append(getAtPosition(j, i).getRepresentation()).append(", ");
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}
