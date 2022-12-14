package Game.Solver;

import Game.Board.TaquinBoardDirection;
import Game.Board.TaquinBoardInstruction;
import Game.Board.TaquinBoardState;

import java.util.*;

public class AStar {

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
    private final HashSet<TaquinBoardState> seenStates;

    public AStar(Heuristic heuristic) {
        var stateComparator = new Comparator<SolutionStep>() {
            @Override
            public int compare(SolutionStep o1, SolutionStep o2) {
                return o1.getState().reportHeuristicValue(heuristic) - o2.getState().reportHeuristicValue(heuristic);
            }
        };
        states = new PriorityQueue<>(stateComparator);
        seenStates = new HashSet<>();
    }

    public List<SolutionStep> solve(TaquinBoardState initialState) {
        var initialStep = new SolutionStep(initialState, null, null);
        states.add(initialStep);

        while (!states.isEmpty()) {
            var currentState = states.remove();

            if (currentState.getState().isGoalState()) {
                var iterator = currentState;
                var instructions = new ArrayList<SolutionStep>();
                do {
                    instructions.add(iterator);
                    iterator = iterator.getParentState();
                }
                while (currentState.getParentState() != null);
                return instructions;
            }

            // For each possible move in the board, we need to have a new board state instance after the move
            // So we need a way to clone board states, and then apply moves to them
            // We also need a way to iterate over the possible moves
            var emptyPosition = currentState.getState().getEmptyPosition();
            for (TaquinBoardDirection direction : TaquinBoardDirection.values()) {
                var newBoardState = currentState.getState().copy();
                var instruction = TaquinBoardInstruction.mapFromDirection(direction);
                newBoardState.processInstruction(instruction, emptyPosition);

                if (seenStates.contains(newBoardState)) {
                    // What should we do here? Is this in the right spot?
                    continue;
                }

                var solutionStep = new SolutionStep(newBoardState, currentState, instruction);

                states.add(solutionStep);
            }

            seenStates.add(currentState.getState());
        }

        return Collections.emptyList();
    }

}
