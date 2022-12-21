package Game.Board;

import Game.Cell.CellFactory;
import Game.Cell.TaquinCell;
import Game.Solver.SolutionStep;
import Game.Solver.TaquinSolutionAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents the user interface of the board. When the user interface takes actions which modify
 * the game, they pass through this class.
 * Algorithms which solve the puzzle do not interact with this class, instead they directly access board states.
 */
public class Board {

    private final CellFactory cellFactory;
    private TaquinBoardState boardState;

    public Board(TaquinBoardState boardState, CellFactory cellFactory) {
        this.boardState = boardState;
        this.cellFactory = cellFactory;
        this.shuffle();

    }

    public Board(TaquinBoardState boardState, CellFactory cellFactory, int shuffleDepth) {
        this.boardState = boardState;
        this.cellFactory = cellFactory;
        this.shuffleInstruction(shuffleDepth);

    }

    public Board(TaquinBoardState boardState, CellFactory cellFactory, ArrayList<ArrayList<Integer>> cellContent) {
        this.boardState = boardState;
        this.cellFactory = cellFactory;

        for (int x = 0; x < this.boardState.getSize(); x++) {
            for (int y = 0; y < this.boardState.getSize(); y++) {
                Integer content = cellContent.get(x).get(y);
                if (content == null) {
                    this.boardState.addCell(this.cellFactory.createEmpty(y, x));
                } else {
                    this.boardState.addCell(this.cellFactory.createTaquinCell(content, y, x));
                }
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

    /**
     * Shuffle the board with a certain number of randomly chosen moves without direct contradiction between moves (like
     * up and directly after down).
     *
     * @param numberOfMoves the number of actions to perform
     */
    private void shuffleInstruction(int numberOfMoves) {

        // Creating the cells
        int count = 0;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (i == getSize() - 1 && j == getSize() - 1) {
                    // Skip the last position
                    break;
                }
                this.boardState.addCell(cellFactory.createTaquinCell(count++, j, i)); // add cell with number
            }
        }

        this.boardState.addCell(cellFactory.createEmpty(getSize() - 1, getSize() - 1)); // add empty cell

        // Shuffling
        Random random = new Random();
        int lastDirectionChoice = -1; // direction of the move of the empty tile
        for (int i = 0; i < numberOfMoves; i++) {
            //System.out.println("Shuffling step : " + i);
            int choice = random.nextInt(4);
            if (lastDirectionChoice != -1 && choice == ~lastDirectionChoice) { // direction
                choice = (choice + 1) % 4;
            }
            TaquinCell empty = boardState.getEmptyPosition();
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
        final TaquinCell currentCell = this.boardState.getAtPosition(x, y);
        for (TaquinBoardDirection direction : TaquinBoardDirection.values()) {
            try {
                var neighbor = this.boardState.getNeighbor(direction, currentCell);
                if (neighbor.isEmpty()) {
                    boardState.processAction(TaquinBoardAction.mapFromDirection(direction), currentCell);
                    return;
                }
            } catch (IndexOutOfBoundsException ignored) {
                // We will be checking all cardinal directions for a cell, so it is normal that some of those
                // directions will throw exceptions
            }
        }
    }

    public List<SolutionStep> solve(TaquinSolutionAlgorithm algorithm) {
        return algorithm.solve(getBoardState());
    }
}
