package Game.Solver;

import Game.Board.TaquinBoardInstruction;
import Game.Board.TaquinBoardState;

public final class SolutionStep implements Comparable<SolutionStep> {
    private final TaquinBoardState state;
    private final SolutionStep parentState;
    private final TaquinBoardInstruction instruction;
    private final int depth;

    private int heuristicValue;

    public SolutionStep(TaquinBoardState state,
                        SolutionStep parentState,
                        TaquinBoardInstruction instruction,
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
                if (!state.getAtPosition(x, y).equals(that.state().getAtPosition(x, y))) {
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

    public TaquinBoardInstruction instruction() {
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
