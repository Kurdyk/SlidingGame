package Game.Solver.Heuristic;

import Game.Board.TargetBoardState;
import Game.Solver.SolutionStep;

public class ManhattanDistanceHeuristic extends Heuristic {

    private final TargetBoardState targetBoardState;

    public ManhattanDistanceHeuristic(TargetBoardState targetBoardState) {
        this.targetBoardState = targetBoardState;
    }

    @Override
    public int getResult(SolutionStep step) {
        int totalDistance = 0;
        var state = step.state();
        for (int y = 0; y < state.getSize(); y++) {
            for (int x = 0; x < state.getSize(); x++) {
                var evaluationCell = state.getAtPosition(x, y);
                if (evaluationCell.isEmpty()) {
                    continue;
                }
                var targetPosition = targetBoardState.getPositionOfCell(evaluationCell.getCellId());
                if (!evaluationCell.getPosition().equals(targetPosition)) {
                    totalDistance += Math.abs(targetPosition.getX() - x)
                            + Math.abs(targetPosition.getY() - y);
                }
            }
        }
        return totalDistance + step.depth();
    }
}
