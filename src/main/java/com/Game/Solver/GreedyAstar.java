package com.Game.Solver;

import com.Game.Board.TaquinBoardAction;
import com.Game.Board.TaquinBoardDirection;
import com.Game.Board.TaquinBoardState;
import com.Game.Solver.Heuristic.Heuristic;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * A greedy version of A*.
 * We don't consider the depth of the nodes but only the value of the heuristic
 * This reduces the amount of visited nodes by a tremendous amount.
 * DOES NOT GUARANTY AN OPTIMAL SOLUTION
 */
public class GreedyAstar extends TaquinSolutionAlgorithm {

    private final boolean logProgress;
    private final Heuristic heuristic;

    public GreedyAstar(Heuristic heuristic, boolean logProgress) {
        this.heuristic = heuristic;
        this.logProgress = logProgress;
    }

    @Override
    public List<SolutionStep> solve(TaquinBoardState initialState) {
        if (!stateIsSolvable(initialState)) {
            System.out.println("Cannot be solved");
            return Collections.emptyList();
        }

        // The frontier, with capacity initialized to an arbitrary large value
        var states = new PriorityQueue<SolutionStep>(1000);
        var seenStates = new HashSet<SolutionStep>();

        var initialStep = new SolutionStep(initialState, null, null, 0);
        states.add(initialStep);
        seenStates.add(initialStep);

        if (logProgress) {
            System.out.println("Start Solve!");
        }

        while (!states.isEmpty()) {
            var currentState = states.poll();

            if (logProgress) {
                System.out.println("evaluating current state");
                System.out.println(currentState.state());
            }

            if (currentState.state().isGoalState()) {
                return unwindSolutionTree(currentState);
            }

            // We generate the possible successor states that occur when we pass ACTION into the Transition Function
            for (TaquinBoardDirection direction : TaquinBoardDirection.values()) {
                // We check if this ACTION is valid to prevent unnecessary creation of states
                if (!currentState.state().targetHasNeighbor(direction, currentState.state().getEmptyPosition())) {
                    continue;
                }
                if (logProgress) {
                    System.out.println("Generating new state from direction: " + direction);
                }
                // Copy the current state to a new object
                var newBoardState = currentState.state().copy();
                var emptyPosition = newBoardState.getEmptyPosition();
                var instruction = TaquinBoardAction.mapFromDirection(direction);
                // Run the transition function, producing a new state
                newBoardState.processAction(instruction, emptyPosition);
                if (logProgress) {
                    System.out.println("Instruction: " + direction);
                    System.out.println(newBoardState);
                }

                var newDistance = currentState.depth() + 1;
                var solutionStep = new SolutionStep(newBoardState, currentState, instruction, newDistance);
                solutionStep.setHeuristicValue(heuristic.getResult(solutionStep) - solutionStep.depth()); // We remove the depth to only consider "better" nodes.

                // Totally new node, just add it to the queue
                if (!seenStates.contains(solutionStep)) {
                    seenStates.add(solutionStep);
                    states.add(solutionStep);
                }
            }

            if (states.size() % 10000 == 0) {
                System.out.println("Frontier is now at: " + states.size());
            }
        }

        return Collections.emptyList();
    }
}
