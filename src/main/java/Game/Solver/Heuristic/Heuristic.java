package Game.Solver.Heuristic;

import Game.Solver.SolutionStep;

public abstract class Heuristic {
    public abstract int getHeuristicValue(SolutionStep step);
}
