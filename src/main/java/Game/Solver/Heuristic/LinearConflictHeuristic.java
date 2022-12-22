package Game.Solver.Heuristic;

import Game.Board.TargetBoardState;
import Game.Solver.SolutionStep;

public class LinearConflictHeuristic extends Heuristic {
    private final TargetBoardState targetBoardState;
    private final ManhattanDistanceHeuristic manhattanDistanceHeuristic;

    public LinearConflictHeuristic(TargetBoardState targetBoardState) {
        this.targetBoardState = targetBoardState;
        manhattanDistanceHeuristic = new ManhattanDistanceHeuristic(targetBoardState);
    }

    @Override
    public int getResult(SolutionStep step) {
//        var evaluationState = step.state();
//        int n = evaluationState.getSize();
//        var targetPositions = new Position[n * n];
//
//        for (int y = 0; y < n; y++) {
//            for (int x = 0; x < n; x++) {
//                var element = targetBoardState.getAtPosition(x, y);
//                if (CellUtilities.cellIsEmpty(element)) {
//                    continue;
//                }
//                targetPositions[element.getCellId()] = element.getPosition();
//            }
//        }
//
//        int numConflicts = 0;
//
//        for (int y = 0; y < n; y++) {
//            for (int x = 0; x < n; x++) {
//                var currentEval = evaluationState.getAtPosition(x, y);
//                if (currentEval.isEmpty()) {
//                    continue;
//                }
//                var targetPos = targetPositions[currentEval.getCellId()];
//                if (targetPos.getX() == x) {
//                    // We are in the correct column
//                    for (int k = x + 1; k < n; k++) {
//                        var neighborInRow = evaluationState.getAtPosition(k, y);
//                        if (neighborInRow.isEmpty()) {
//                            continue;
//                        }
//                        var neighborTarget = targetPositions[neighborInRow.getCellId()];
//                        if (neighborTarget.getY() == targetPos.getY() && neighborInRow.getCellId() < currentEval.getCellId()) {
//                            numConflicts++;
//                        }
//                    }
//                }
//
//                if (targetPos.getY() == y) {
//                    // We are in the correct row
//                    for (int k = y + 1; k < n; k++) {
//                        var neighborInColumn = evaluationState.getAtPosition(x, k);
//                        if (neighborInColumn.isEmpty()) {
//                            continue;
//                        }
//                        var neighborTarget = targetPositions[neighborInColumn.getCellId()];
//                        if (neighborTarget.getX() == targetPos.getX() && neighborInColumn.getCellId() < currentEval.getCellId()) {
//                            numConflicts++;
//                        }
//                    }
//                }
//            }
//        }

        return manhattanDistanceHeuristic.getResult(step);
    }
}
