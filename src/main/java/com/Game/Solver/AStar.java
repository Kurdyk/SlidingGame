package com.Game.Solver;

import com.Game.Board.TaquinBoardAction;
import com.Game.Board.TaquinBoardDirection;
import com.Game.Board.TaquinBoardState;
import com.Game.Solver.Heuristic.Heuristic;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Our implementation of AStar:
 * - At each iteration we check the board state
 * - We find the list of actions of this state (this is all the possible directions we can swap the blank space)
 * - For each action we calculate the heuristic value of the result of that action
 * - (For Uniform Cost Search we just treat them all the same)
 * Our implementation relies on the Java standard library PriorityQueue to reduce the running time.
 * The priority queue allows us to remove the node with the lowest combine heuristic and cost score in Log(n) time.
 * <p>
 * There is an additional step in AStar we have chosen to omit.
 * After checking if a node exists in the list of seen states, we could compare its Heuristic value with that of the
 * previously seen version in the frontier.
 * If our new heuristic is lower, we could replace the previously seen version with our new, more optimal version.
 * We chose to omit this step because it required iterating through all nodes in the frontier, which dramatically
 * slowed down our algorithm. Thus, our version of AStar may not find the optimal solution in all cases.
 **/
public class AStar extends TaquinSolutionAlgorithm {


    private final boolean logProgress;
    private final Heuristic heuristic;

    public AStar(Heuristic heuristic, boolean logProgress) {
        this.heuristic = heuristic;
        this.logProgress = logProgress;
    }

    @Override
    public TaquinSolutionHolder solve(TaquinBoardState initialState, long maxRuntime, long maxFrontierSize) {
        if (!stateIsSolvable(initialState)) {
            System.out.println("Cannot be solved");
            return TaquinSolutionHolder.getEmpty();
        }

        // The frontier, with capacity initialized to an arbitrary large value
        var states = new PriorityQueue<SolutionStep>(1000);
        var seenStates = new HashSet<SolutionStep>();

        var initialStep = new SolutionStep(initialState, null, null, 0);
        initialStep.setHeuristicValue(heuristic.getResult(initialStep));
        states.add(initialStep);
        seenStates.add(initialStep);

        if (logProgress) {
            System.out.println("Start Solve!");
        }

        long startTime = System.nanoTime();
        long numExpansions = 0;
        long frontierSize = 0;

        while (!states.isEmpty()) {
            var currentState = states.poll();

            if (logProgress) {
                System.out.println("Evaluating head of frontier with heuristic: " + currentState.getHeuristicValue());
                System.out.println("Took head state out of " + (states.size() + 1) + " states");
                System.out.println(currentState.state());
            }

            if (currentState.state().isGoalState()) {
                long elapsedTime = System.nanoTime() - startTime;
                var solutionSteps = unwindSolutionTree(currentState);
                return new TaquinSolutionHolder(solutionSteps, elapsedTime, frontierSize, numExpansions, false, false);
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

                var newDistance = currentState.depth() + 1;
                var solutionStep = new SolutionStep(newBoardState, currentState, instruction, newDistance);
                solutionStep.setHeuristicValue(heuristic.getResult(solutionStep));

                // Totally new node, just add it to the queue
                if (!seenStates.contains(solutionStep)) {
                    seenStates.add(solutionStep);
                    states.add(solutionStep);
                }
            }

            numExpansions++;

            if (states.size() % 10000 == 0) {
                System.out.println("Frontier is now at: " + states.size());
            }

            if (states.size() > frontierSize) {
                frontierSize = states.size();
            }

            if (maxFrontierSize > 0 && frontierSize > maxFrontierSize) {
                return TaquinSolutionHolder.getExpiredFrontierSize();
            }

            if (maxRuntime > 0 && System.nanoTime() - startTime > maxRuntime) {
                return TaquinSolutionHolder.getExpiredRuntime();
            }
        }

        return TaquinSolutionHolder.getEmpty();
    }
}
