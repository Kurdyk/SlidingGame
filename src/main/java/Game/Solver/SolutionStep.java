package Game.Solver;

import Game.Board.TaquinBoardInstruction;
import Game.Board.TaquinBoardState;

import java.util.Objects;

public record SolutionStep(TaquinBoardState state,
                           Game.Solver.SolutionStep parentState,
                           TaquinBoardInstruction instruction,
                           int depth) {


    /*
     * Note: Ellington
     * I am purposefully not including the parent state when evaluating the equals of two steps.
     * I am also not sure if we need to include the insturction
     *
     * */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolutionStep that = (SolutionStep) o;
        return Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}
