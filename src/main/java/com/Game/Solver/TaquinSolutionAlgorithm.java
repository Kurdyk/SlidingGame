package com.Game.Solver;

import com.Game.Board.TaquinBoardState;
import com.Game.Cell.CellUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The base abstraction of a solution algorithm.
 * This class provides behavior universal to all of our solution algorithms.
 */
public abstract class TaquinSolutionAlgorithm {

    public abstract TaquinSolutionHolder solve(TaquinBoardState initialState, long maxRuntime, long maxFrontierSize);

    /**
     * @param terminalNode The end state of our algorithm
     * @return The sequence of States and Actions to move from our initial state to the terminalNode
     */
    protected List<SolutionStep> unwindSolutionTree(SolutionStep terminalNode) {

        if (terminalNode.parentState() == null) { // already solved
            return null;
        }

        var iterator = terminalNode;
        var instructions = new ArrayList<SolutionStep>();
        do {
            instructions.add(iterator);
            iterator = iterator.parentState();
        }
        while (iterator.parentState() != null);
        Collections.reverse(instructions);
        return instructions;
    }

    /**
     * Because our action can only ever swap the position of our empty cell and a target cell, there can be
     * versions of the puzzle that are unsolvable.
     * We want to eliminate these states and prevent the algorithm from wasting time on an unsolvable puzzle.
     *
     * @param state The state we are evaluating
     * @return true if the state is solvable, false otherwise
     */
    protected boolean stateIsSolvable(TaquinBoardState state) {
        // Get the dimension of the puzzle
        int n = state.getSize();

        // Get the linear representation of the puzzle
        int[] flatPuzzle = new int[n * n];
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                flatPuzzle[index++] = state.getAtPosition(j, i);
            }
        }

        // Count the number of inversions in the puzzle
        int inversions = 0;
        for (int i = 0; i < flatPuzzle.length; i++) {
            for (int j = i + 1; j < flatPuzzle.length; j++) {
                if (!CellUtilities.cellIsEmpty(flatPuzzle[i]) && !CellUtilities.cellIsEmpty(flatPuzzle[j])
                        && flatPuzzle[i] > flatPuzzle[j]) {
                    inversions++;
                }
            }
        }

        System.out.println("inversion : " + inversions);
        // Check if the puzzle is solvable
        if (n % 2 == 1) {
            // If n is odd, the puzzle is solvable if the number of inversions is even
            return inversions % 2 == 0;
        } else {
            // If n is even, the puzzle is solvable if the blank is on an odd row counting from the bottom
            // and the number of inversions is odd, or if the blank is on an even row counting from the bottom
            // and the number of inversions is even
            int blankRow = state.getEmptyPosition().getY();
            int rowNumberFromBottom = state.getSize() - blankRow;
            if (inversions % 2 == 0) {
                return rowNumberFromBottom % 2 != 0;
            } else {
                return rowNumberFromBottom % 2 != 1;
            }
        }
    }
}
