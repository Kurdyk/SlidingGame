package Game.Solver;

import Game.Board.TaquinBoardState;

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
}
