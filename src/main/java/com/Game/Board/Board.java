package com.Game.Board;

import com.Game.Cell.CellUtilities;
import com.Game.Cell.Position;
import com.Game.Solver.TaquinSolutionAlgorithm;
import com.Game.Solver.TaquinSolutionHolder;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents the user interface of the board. When the user interface takes actions which modify
 * the game, they pass through this class.
 * Algorithms which solve the puzzle do not interact with this class, instead they directly access board states.
 */
public class Board {

    private TaquinBoardState boardState;

    public Board(TaquinBoardState boardState) {
        this.boardState = boardState;
        this.shuffle();

    }

    public Board(TaquinBoardState boardState, int shuffleDepth) {
        this.boardState = boardState;
        this.shuffleInstruction(shuffleDepth);

    }

    public Board(TaquinBoardState boardState, ArrayList<ArrayList<Short>> cellContent) {
        this.boardState = boardState;

        for (int y = 0; y < this.boardState.getSize(); y++) {
            for (int x = 0; x < this.boardState.getSize(); x++) {
                short content = cellContent.get(y).get(x);
                this.boardState.addCell(new Position(x, y), content);
            }
        }
    }

    public TaquinBoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(TaquinBoardState boardState) {
        this.boardState = boardState;
    }

    public int getSize() {
        return boardState.getSize();
    }

    /**
     * Fully randomize the board, can lead to unsolvable configuration
     */
    private void shuffle() {
        int nbOfCells = getSize() * getSize() - 1;

        ArrayList<Short> availableIds = new ArrayList<>();
        for (short i = 0; i < nbOfCells; i++) {
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
                this.boardState.addCell(new Position(j, i), availableIds.get(selected)); // add cell with number
                availableIds.remove(selected);
            }
        }

        this.boardState.addCell(new Position(getSize() - 1, getSize() - 1), TaquinBoardState.EMPTY_ID); // add empty cell
    }

    /**
     * Shuffle the board with a certain number of randomly chosen moves without direct contradiction between moves (like
     * up and directly after down).
     *
     * @param numberOfMoves the number of actions to perform
     */
    private void shuffleInstruction(int numberOfMoves) {

        // Creating the cells
        short count = 0;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (i == getSize() - 1 && j == getSize() - 1) {
                    // Skip the last position
                    break;
                }
                this.boardState.addCell(new Position(j, i), count++); // add cell with number
            }
        }

        this.boardState.addCell(new Position(getSize() - 1, getSize() - 1), TaquinBoardState.EMPTY_ID); // add empty cell

        // Shuffling
        Random random = new Random();
        int lastDirectionChoice = -1; // direction of the move of the empty tile
        for (int i = 0; i < numberOfMoves; i++) {
            //System.out.println("Shuffling step : " + i);
            int choice = random.nextInt(4);
            if (lastDirectionChoice != -1 && choice == lastDirectionChoice) { // direction
                choice = (choice + 1) % 4;
            }
            Position empty = boardState.getEmptyPosition();
            TaquinBoardDirection direction = switch (choice) {
                case 0 -> TaquinBoardDirection.UP;
                case 1 -> TaquinBoardDirection.LEFT;
                case 2 -> TaquinBoardDirection.RIGHT;
                default -> TaquinBoardDirection.DOWN;
            };

            if (!boardState.targetHasNeighbor(direction, empty)) { // illegal move
                i--;
                continue;
            }

            boardState.processAction(TaquinBoardAction.mapFromDirection(direction), empty);
            lastDirectionChoice = choice;
        }

    }

    @Override
    public String toString() {
        return boardState.toString();
    }

    /**
     * Takes a position of a cell and finds a neighbor which is the empty cell. If such a neighbor is found, the
     * position of the two cells are switched.
     *
     * @param x
     * @param y
     */
    public void move(int x, int y) {
        final Position currentCell = new Position(x, y);
        for (TaquinBoardDirection direction : TaquinBoardDirection.values()) {
            try {
                var neighbor = this.boardState.getNeighbor(direction, currentCell);
                if (CellUtilities.cellIsEmpty(neighbor)) {
                    boardState.processAction(TaquinBoardAction.mapFromDirection(direction), currentCell);
                    return;
                }
            } catch (IndexOutOfBoundsException ignored) {
                // We will be checking all cardinal directions for a cell, so it is normal that some of those
                // directions will throw exceptions
            }
        }
    }

    public TaquinSolutionHolder solve(TaquinSolutionAlgorithm algorithm, long maxRuntime, long maxFrontierSize) {
        return algorithm.solve(getBoardState(), maxRuntime, maxFrontierSize);
    }
}
