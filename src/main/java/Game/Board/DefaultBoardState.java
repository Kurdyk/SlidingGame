package Game.Board;

import Game.Cell.Position;
import Game.Cell.TaquinCell;

import java.util.ArrayList;

public class DefaultBoardState implements TaquinBoardState {

    private final int size;

    private final ArrayList<ArrayList<TaquinCell>> boardImplementation = new ArrayList<>();

    public DefaultBoardState(int size) {
        this.size = size;
        for (int i = 0; i < size; i++) {
            boardImplementation.add(new ArrayList<>(size));
            for (int j = 0; j < size; j++) {
                // This ensures that we can set cells to specific positions when they are first inserted
                boardImplementation.get(i).add(null);
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
        boardImplementation.get(targetPosition.getY()).set(targetPosition.getX(), cell);
    }

    @Override
    public TaquinCell getAtPosition(int x, int y) {
        return boardImplementation.get(y).get(x);
    }

    @Override
    public TaquinCell getAtPosition(Position position) {
        return getAtPosition(position.getX(), position.getY());
    }

    private void setAtPosition(Position position, TaquinCell target) {
        boardImplementation.get(position.getY()).set(position.getX(), target);
    }

    @Override
    public void processInstruction(TaquinBoardInstruction instruction, TaquinCell target) {
        TaquinCell neighbor;
        switch (instruction) {
            case SWAP_UP -> neighbor = getNeighbor(TaquinBoardDirection.UP, target);
            case SWAP_RIGHT -> neighbor = getNeighbor(TaquinBoardDirection.RIGHT, target);
            case SWAP_DOWN -> neighbor = getNeighbor(TaquinBoardDirection.DOWN, target);
            case SWAP_LEFT -> neighbor = getNeighbor(TaquinBoardDirection.LEFT, target);
            default -> throw new IllegalStateException("Unexpected value: " + instruction);
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
        Position neighborPosition;
        Position currentPosition = target.getPosition();
        switch (direction) {
            case UP -> neighborPosition = new Position(currentPosition.getX(), currentPosition.getY() - 1);
            case RIGHT -> neighborPosition = new Position(currentPosition.getX() + 1, currentPosition.getY());
            case DOWN -> neighborPosition = new Position(currentPosition.getX(), currentPosition.getY() + 1);
            case LEFT -> neighborPosition = new Position(currentPosition.getX() - 1, currentPosition.getY());
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        return getAtPosition(neighborPosition);
    }
}
