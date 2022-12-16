package Game.Solver.Heuristic;

import Game.Solver.SolutionStep;

public class UniformCostHeuristic extends Heuristic {

    @Override
    public int getHeuristicValue(SolutionStep step) {
        return step.depth();
    }
}
