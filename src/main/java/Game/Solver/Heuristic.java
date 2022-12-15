package Game.Solver;

import Game.Board.TaquinBoardState;

public abstract class Heuristic {
    public abstract int getHeuristicValue(TaquinBoardState cell);
}
