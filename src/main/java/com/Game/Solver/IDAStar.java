package com.Game.Solver;

import com.Game.Board.TaquinBoardAction;
import com.Game.Board.TaquinBoardDirection;
import com.Game.Board.TaquinBoardState;
import com.Game.Solver.Heuristic.Heuristic;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This class corresponds to our implementation of the IDA* algorithm.
 * The execution can be represented as a tree rooted at the starting node.
 * Each iteration of the main while loop in the solve method will build a search tree whose size is bounded by the parameter bound.
 * If a tree finds a path leading to the solution, it will be signaled by a negative value.
 * Otherwise, the loop will try with a larger bound being the min of the depth and heuristic of the leaves of the previous tree.
 * This choice ensure to find an optimal solution if one is found
 * The algorithm allows a low memory consumption by deleting every subtree after it was explored.
 * The tradeoff is an increased running time by exploring nodes multiple times.
 */
public class IDAStar extends TaquinSolutionAlgorithm {

    private final boolean logProgress;
    private final Heuristic heuristic;

    private long numExpansions = 0;
    private long frontierSize = 0;

    public IDAStar(Heuristic heuristic, boolean logProgress) {
        this.heuristic = heuristic;
        this.logProgress = logProgress;
    }

    @Override
    public TaquinSolutionHolder solve(TaquinBoardState initialState, long maxRuntime, long maxFrontierSize) {

        if (!stateIsSolvable(initialState)) {
            System.out.println("Cannot be solved");
            return TaquinSolutionHolder.getEmpty();
        }

        if (logProgress) {
            System.out.println("Start Solve!");
        }

        var initialStep = new SolutionStep(initialState, null, null, 0);
        int bound = this.heuristic.getResult(initialStep); // set the initial bound to the minimum amount of moves required to solve the instance
        List<SolutionStep> path = new ArrayList<>();
        path.add(initialStep); // set the root of the tree to the initial board

        var startTime = System.nanoTime();

        while (true) {
            if (this.logProgress) System.out.println("Bound : " + bound);
            var pair = this.solveForBound(path, 0, bound);
            if (pair.getKey() < 0) { // a solution was found
                var elapsedTime = System.nanoTime() - startTime;
                return new TaquinSolutionHolder(pair.getValue(), elapsedTime, frontierSize, numExpansions);
            }
            if (pair.getKey() == Integer.MAX_VALUE) { // Should not happen since at this point all instance are solvable
                return TaquinSolutionHolder.getEmpty();
            }
            bound = pair.getKey(); // no solution was found, increasing the bound to depth + heuristic of the best board found
        }
    }

    /**
     * Procedure for IDA*
     *
     * @param path          the current path
     * @param current_depth the current depth in the tree
     * @param bound         the limit for the value depth + heuristic
     * @return a couple (x, y) where x is either negative to signal a solution or
     * current depth + heuristic of the last node of the path, used to set the bound for later iterations.
     * y is the list of solution steps required to solve the problem, y is null if the path doesn't end on a solution
     */
    private Pair<Integer, List<SolutionStep>> solveForBound(List<SolutionStep> path, int current_depth, int bound) {

        SolutionStep currentState = path.get(path.size() - 1);
        if (currentState.state().isGoalState()) { // found a solution, return a negative x and unwinding the solution steps
            return new Pair<>(-currentState.depth(), unwindSolutionTree(currentState));
        }

        if (current_depth + currentState.getHeuristicValue() > bound) { // depth limit reach with no solution
            return new Pair<>(current_depth + currentState.getHeuristicValue(), null);
        }

        Integer min = Integer.MAX_VALUE; // the value to return as x
        for (TaquinBoardDirection direction : TaquinBoardDirection.values()) { // creating a path per direction

            if (!currentState.state().targetHasNeighbor(direction, currentState.state().getEmptyPosition())) { // impossible move
                continue;
            }

            // creating the new board
            var newBoardState = currentState.state().copy();
            var emptyPosition = newBoardState.getEmptyPosition();
            var instruction = TaquinBoardAction.mapFromDirection(direction);
            newBoardState.processAction(instruction, emptyPosition);

            // creating tge new solution step
            var newDistance = currentState.depth() + 1;
            var solutionStep = new SolutionStep(newBoardState, currentState, instruction, newDistance);
            solutionStep.setHeuristicValue(heuristic.getResult(solutionStep));

            if (path.contains(solutionStep)) // if the solution step already exists (only comparing the boards cf equals in SolutionStep.java) in the path, ignore it to avoid cycle
                continue;

            path.add(solutionStep);
            if (path.size() > frontierSize) {
                frontierSize = path.size();
            }
            var pair = solveForBound(path, newDistance, bound); // solve recursively for the new path
            numExpansions++;
            if (pair.getKey() < 0) { // Solution found
                return pair;
            }

            if (pair.getKey() < min) min = pair.getKey(); // Solution not found in this branch

            path.remove(solutionStep); // remove the branch from the working tree

        }

        return new Pair<>(min, null); // Not found in the bound
    }


}
