package Game.Solver;

import Game.Board.TaquinBoardState;
import Game.Cell.TaquinCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TaquinSolutionAlgorithm {
    public abstract List<SolutionStep> solve(TaquinBoardState initialState);

    protected List<SolutionStep> unwindSolutionTree(SolutionStep terminalNode) {
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

    protected boolean isSolvable(TaquinBoardState state) {
        // Get the dimension of the puzzle
        int n = state.getSize();

        // Get the linear representation of the puzzle
        TaquinCell[] flatPuzzle = new TaquinCell[n * n];
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
                if (!flatPuzzle[i].isEmpty() && !flatPuzzle[j].isEmpty()
                        && flatPuzzle[i].getCellId() > flatPuzzle[j].getCellId()) {
                    inversions++;
                }
            }
        }

        // Check if the puzzle is solvable
        if (n % 2 == 1) {
            // If n is odd, the puzzle is solvable if the number of inversions is even
            return inversions % 2 == 0;
        } else {
            // If n is even, the puzzle is solvable if the blank is on an odd row counting from the bottom
            // and the number of inversions is odd, or if the blank is on an even row counting from the bottom
            // and the number of inversions is even
            int blankRow = state.getEmptyPosition().getPosition().getY();
            return (blankRow % 2 == 1 && inversions % 2 == 1) || (blankRow % 2 == 0 && inversions % 2 == 0);
        }
    }
}
