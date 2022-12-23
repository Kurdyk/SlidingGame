package com.Game.Solver;

import com.Game.Board.TaquinBoardAction;
import com.Game.Board.TaquinBoardState;

/**
 * An important class specific to our implementation.
 * The SolutionStep carries the information of a state in the context of solving the puzzle via an algorithm.
 * The SolutionStep maintains a pointer to the parent state, as well as the instruction that when acted upon the parent
 * results in the current state.
 * Thus, we can reconstruct the solution path.
 * The depth represents the total cost of actions, each having a flat cost of 1, to get to this state.
 * And finally the heuristic value is set independently of this class, allowing us to operate on this class
 * via any heuristic.
 */
public final class SolutionStep implements Comparable<SolutionStep> {
    private final TaquinBoardState state;
    private final SolutionStep parentState;
    private final TaquinBoardAction instruction;
    private final int depth;
    private int heuristicValue;

    public SolutionStep(TaquinBoardState state,
                        SolutionStep parentState,
                        TaquinBoardAction instruction,
                        int depth) {
        this.state = state;
        this.parentState = parentState;
        this.instruction = instruction;
        this.depth = depth;
        this.heuristicValue = 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolutionStep that = (SolutionStep) o;
        for (int y = 0; y < state.getSize(); y++) {
            for (int x = 0; x < state.getSize(); x++) {
                if (state.getAtPosition(x, y) != that.state().getAtPosition(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    public TaquinBoardState state() {
        return state;
    }

    public SolutionStep parentState() {
        return parentState;
    }

    public TaquinBoardAction instruction() {
        return instruction;
    }

    public int depth() {
        return depth;
    }

    public int getHeuristicValue() {
        return heuristicValue;
    }

    public void setHeuristicValue(int heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    @Override
    public String toString() {
        return "SolutionStep[" +
                "state=" + state + ", " +
                "parentState=" + parentState + ", " +
                "instruction=" + instruction + ", " +
                "depth=" + depth + ']';
    }

    @Override
    public int compareTo(SolutionStep o) {
        return Integer.compare(heuristicValue, o.heuristicValue);
    }
}
