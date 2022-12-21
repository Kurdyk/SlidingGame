package Game.Solver.Heuristic;

import Game.Board.TargetBoardState;
import Game.Solver.SolutionStep;

public class DisplacedTilesHeuristic extends Heuristic {

    private final TargetBoardState targetBoardState;

    public DisplacedTilesHeuristic(TargetBoardState targetBoardState) {
        this.targetBoardState = targetBoardState;
    }

    @Override
    public int getResult(SolutionStep step) {
        int count = 0;
        var state = step.state();
        for (int y = 0; y < state.getSize(); y++) {
            for (int x = 0; x < state.getSize(); x++) {
                var targetCell = targetBoardState.getAtPosition(x, y);
                var evaluationCell = state.getAtPosition(x, y);
                if (evaluationCell.isEmpty()) {
                    continue;
                }
                if (targetCell.getCellId() != evaluationCell.getCellId()) {
                    count++;
                }
            }
        }
        return count + step.depth();
    }
}
