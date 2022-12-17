package Game.Solver;

import Game.Board.TaquinBoardDirection;
import Game.Board.TaquinBoardInstruction;
import Game.Board.TaquinBoardState;
import Game.Cell.CellFactory;
import Game.Solver.Heuristic.Heuristic;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class AStar extends TaquinSolutionAlgorithm {


    /*
     * So we need to implement a solution to the Taquin Puzzle using search algorithms
     *   A* with 2 different Heuristics
     *   BFS
     *
     * How would A* work?
     *   At each iteration we check the board state
     *   We find the list of actions of this state (this is all the possible directions we can swap the blank space)
     *   For each action we calculate the heuristic value of the result of that action
     *   (For Uniform Cost Search we just treat them all the same)
     *   Then we iterate
     *
     * How should we organize the Heuristics?
     *  So at the top level TaquinController we will invoke "solve" with a specific solution method
     *  Solve will call A*, which will ask each board state to report its Heuristic
     *  So each board state instance needs to take a Heuristic as an argument,
     *  then iterate through the cells and calculate the Heuristic for each cell, and return the sum
     * */
    private final CellFactory cellFactory;
    private final boolean logProgress;
    private final Heuristic heuristic;

    public AStar(Heuristic heuristic, CellFactory cellFactory, boolean logProgress) {
        this.heuristic = heuristic;
        this.cellFactory = cellFactory;
        this.logProgress = logProgress;
    }

    @Override
    public List<SolutionStep> solve(TaquinBoardState initialState) {
        if (!isSolvable(initialState)) {
            return Collections.emptyList();
        }

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

            for (TaquinBoardDirection direction : TaquinBoardDirection.values()) {
                if (!currentState.state().hasNeighbor(direction, currentState.state().getEmptyPosition())) {
                    continue;
                }
                if (logProgress) {
                    System.out.println("Generating new state from direction: " + direction);
                }
                var newBoardState = currentState.state().copy(cellFactory);
                var emptyPosition = newBoardState.getEmptyPosition();
                var instruction = TaquinBoardInstruction.mapFromDirection(direction);
                newBoardState.processInstruction(instruction, emptyPosition);
                if (logProgress) {
                    System.out.println("Instruction: " + direction);
                    System.out.println(newBoardState);
                }

                var newDistance = currentState.depth() + 1;
                var solutionStep = new SolutionStep(newBoardState, currentState, instruction, newDistance);
                solutionStep.setHeuristicValue(heuristic.getHeuristicValue(solutionStep));

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
