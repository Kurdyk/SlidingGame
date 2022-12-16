package Game.Solver;

import Game.Board.TaquinBoardDirection;
import Game.Board.TaquinBoardInstruction;
import Game.Board.TaquinBoardState;
import Game.Cell.CellFactory;
import Game.Solver.Heuristic.Heuristic;

import java.util.*;

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

    private final PriorityQueue<SolutionStep> states;
    private final HashSet<Integer> seenStates;
    private final CellFactory cellFactory;
    private final boolean logProgress;

    public AStar(Heuristic heuristic, CellFactory cellFactory, boolean logProgress) {
        this.cellFactory = cellFactory;
        var stateComparator = new Comparator<SolutionStep>() {
            @Override
            public int compare(SolutionStep o1, SolutionStep o2) {
                return heuristic.getHeuristicValue(o1) - heuristic.getHeuristicValue(o2);
            }
        };
        states = new PriorityQueue<>(stateComparator);
        seenStates = new HashSet<>();
        this.logProgress = logProgress;
    }

    @Override
    public List<SolutionStep> solve(TaquinBoardState initialState) {
        var initialStep = new SolutionStep(initialState, null, null, 0);
        states.add(initialStep);

        if (logProgress) {
            System.out.println("Start Solve!");
        }

        while (!states.isEmpty()) {
            var currentState = states.remove();

            seenStates.add(currentState.state().hashCode());

            if (logProgress) {
                System.out.println("evaluating current state");
                System.out.println(currentState.state());
            }

            if (currentState.state().isGoalState()) {
                return unwindSolutionTree(currentState);
            }

            for (TaquinBoardDirection direction : TaquinBoardDirection.values()) {
                if (logProgress) {
                    System.out.println("Generating new state from direction: " + direction);
                }
                var newBoardState = currentState.state().copy(cellFactory);
                var emptyPosition = newBoardState.getEmptyPosition();
                var instruction = TaquinBoardInstruction.mapFromDirection(direction);
                try {
                    newBoardState.processInstruction(instruction, emptyPosition);
                    if (logProgress) {
                        System.out.println(newBoardState);
                    }
                } catch (IndexOutOfBoundsException exception) {
                    // It is normal to see this exception as we try each direction
                    // An optimization would be to not try invalid directions
                    if (logProgress) {
                        System.out.println("Direction is invalid");
                    }
                    continue;
                }

                if (seenStates.contains(newBoardState.hashCode())) {
                    if (logProgress) {
                        System.out.println("State has been seen, skipping");
                    }
                    continue;
                }

                var solutionStep = new SolutionStep(newBoardState, currentState, instruction, currentState.depth() + 1);
                states.add(solutionStep);
            }
        }

        return Collections.emptyList();
    }

}
