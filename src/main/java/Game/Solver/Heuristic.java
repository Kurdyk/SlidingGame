package Game.Solver;

import Game.Cell.TaquinCell;

public abstract class Heuristic {
    public abstract int getHeuristicValue(TaquinCell cell);
}
