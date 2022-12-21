package Game.Solver.Heuristic;

import Game.Solver.SolutionStep;

/**
 * The base class of our Heuristics.
 * The Heuristics are implemented according to their formalization as:
 * DisplacedTilesHeuristic.java
 * ManhattanDistanceHeuristic.java
 */
public abstract class Heuristic {
    /**
     * @param step A SolutionStep, an instance of a board state with associated data during the runtime of an algorithm
     * @return The heuristic value of the state.
     */
    public abstract int getResult(SolutionStep step);
}
