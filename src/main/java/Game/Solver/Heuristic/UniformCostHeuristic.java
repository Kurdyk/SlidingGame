package Game.Solver.Heuristic;

import Game.Solver.SolutionStep;

/**
 * We implement the Uniform Cost Search with this special heuristic. It simply returns the number of actions needed
 * to get to the current state.
 * Thus our algorithms will evaluate nodes with this heuristic by taking the minimal cost to reach that state first.
 */
public class UniformCostHeuristic extends Heuristic {
    @Override
    public int getResult(SolutionStep step) {
        return step.depth();
    }
}
