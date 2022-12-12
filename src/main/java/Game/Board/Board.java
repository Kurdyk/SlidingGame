package Game.Board;

import Game.Cell.CellFactory;
import Game.Cell.TaquinCell;

import java.util.ArrayList;
import java.util.Random;

public class Board {

    private final TaquinBoardState boardState;
    private final CellFactory cellFactory;


    public Board(TaquinBoardState boardState, CellFactory cellFactory) {
        this.boardState = boardState;
        this.cellFactory = cellFactory;
        this.shuffle();

    }

    public TaquinBoardState getBoardState() {
        return boardState;
    }

    public int getSize() {
        return boardState.getSize();
    }

    private void shuffle() {
        int nbOfCells = getSize() * getSize() - 1;

        ArrayList<Integer> availableIds = new ArrayList<>();
        for (int i = 0; i < nbOfCells; i++) {
            availableIds.add(i);
        }

        Random random = new Random();

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (i == getSize() - 1 && j == getSize() - 1) {
                    // Skip the last position
                    break;
                }
                int selected = random.nextInt(availableIds.size());
                this.boardState.addCell(cellFactory.createTaquinCell(availableIds.get(selected), j, i)); // add cell with number
                availableIds.remove(selected);
            }
        }

        this.boardState.addCell(cellFactory.createEmpty(getSize() - 1, getSize() - 1)); // add empty cell
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                stringBuilder.append(this.boardState.getAtPosition(j, i).getCellId()).append(", ");
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public void move(int x, int y) {
        final TaquinCell currentCell = this.boardState.getAtPosition(x, y);
        for (TaquinBoardDirection direction : TaquinBoardDirection.values()) {
            try {
                var neighbor = this.boardState.getNeighbor(direction, currentCell);
                if (neighbor.isEmpty()) {
                    boardState.processInstruction(TaquinBoardInstruction.mapFromDirection(direction), currentCell);
                    return;
                }
            } catch (IndexOutOfBoundsException ignored) {
                // We will be checking all cardinal directions for a cell, so it is normal that some of those
                // directions will throw exceptions
            }
        }
    }
}
