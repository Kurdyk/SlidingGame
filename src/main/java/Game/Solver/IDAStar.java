package Game.Solver;

import Game.Board.TaquinBoardAction;
import Game.Board.TaquinBoardDirection;
import Game.Board.TaquinBoardState;
import Game.Cell.CellFactory;
import Game.Solver.Heuristic.Heuristic;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IDAStar extends TaquinSolutionAlgorithm {

    private final CellFactory cellFactory;
    private final boolean logProgress;
    private final Heuristic heuristic;

    public IDAStar(Heuristic heuristic, CellFactory cellFactory, boolean logProgress) {
        this.heuristic = heuristic;
        this.cellFactory = cellFactory;
        this.logProgress = logProgress;
    }

    @Override
    public List<SolutionStep> solve(TaquinBoardState initialState) {

        if (stateIsSolvable(initialState)) {
            System.out.println("Cannot be solved");
            return Collections.emptyList();
        }

        if (logProgress) {
            System.out.println("Start Solve!");
        }

        var initialStep = new SolutionStep(initialState, null, null, 0);
        int bound = this.heuristic.getResult(initialStep);
        List<SolutionStep> path = new ArrayList<>();
        path.add(initialStep);

        while (true) {
            if (this.logProgress) System.out.println("Bound : " + bound);
            var pair = this.solveForBound(path, 0, bound);
            if (pair.getKey() < 0) {
                return pair.getValue();
            }
            if (pair.getKey() == Integer.MAX_VALUE) { // Should not happen since at this point all instance are solvable
                return Collections.emptyList();
            }
            bound = pair.getKey();
        }
    }


    private Pair<Integer, List<SolutionStep>> solveForBound(List<SolutionStep> path, int current_depth, int bound) {

        SolutionStep currentState = path.get(path.size() - 1);
        if (currentState.state().isGoalState()) { // found a solution
            return new Pair<>(-currentState.depth(), unwindSolutionTree(currentState));
        }

        if (current_depth + currentState.getHeuristicValue() > bound) { // depth limit reach with no solution
            return new Pair<>(current_depth + currentState.getHeuristicValue(), null);
        }

        Integer min = Integer.MAX_VALUE;
        for (TaquinBoardDirection direction : TaquinBoardDirection.values()) {
            if (!currentState.state().targetHasNeighbor(direction, currentState.state().getEmptyPosition())) {
                continue;
            }


            var newBoardState = currentState.state().copy(cellFactory);
            var emptyPosition = newBoardState.getEmptyPosition();
            var instruction = TaquinBoardAction.mapFromDirection(direction);
            newBoardState.processAction(instruction, emptyPosition);

            var newDistance = currentState.depth() + 1;
            var solutionStep = new SolutionStep(newBoardState, currentState, instruction, newDistance);
            solutionStep.setHeuristicValue(heuristic.getResult(solutionStep));

            if (path.contains(solutionStep))
                continue;

            path.add(solutionStep);
            var pair = solveForBound(path, newDistance, bound);

            if (pair.getKey() < 0) { // Solution found
                return pair;
            }

            if (pair.getKey() < min) min = pair.getKey(); // Solution not found in this branch

            path.remove(solutionStep);

        }

        return new Pair<>(min, null); // Not found in the bound
    }


}
