package Game.Solver;

import Game.Board.TaquinBoardDirection;
import Game.Board.TaquinBoardInstruction;
import Game.Board.TaquinBoardState;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

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


    private final PriorityQueue<TaquinBoardState> states;

    public AStar(Heuristic heuristic) {
        var stateComparator = new Comparator<TaquinBoardState>() {
            @Override
            public int compare(TaquinBoardState o1, TaquinBoardState o2) {
                return o1.reportHeuristicValue(heuristic) - o2.reportHeuristicValue(heuristic);
            }
        };
        states = new PriorityQueue<>(stateComparator);
    }

    public void solve(TaquinBoardState initialState) {
        states.add(initialState);
        final var seenStates = new HashSet<TaquinBoardState>();

        while (!states.isEmpty()) {
            var currentState = states.remove();

            if (currentState.isGoalState()) {
                // TODO track the instructions that lead to the goal
                return;
            }

            // For each possible move in the board, we need to have a new board state instance after the move
            // So we need a way to clone board states, and then apply moves to them
            // We also need a way to iterate over the possible moves
            var emptyPosition = currentState.getEmptyPosition();
            for (TaquinBoardDirection direction : TaquinBoardDirection.values()) {
                var newBoardState = currentState.copy();
                newBoardState.processInstruction(TaquinBoardInstruction.mapFromDirection(direction), emptyPosition);
                states.add(newBoardState);
            }

            // When do we check if we have already seen a state?
        }
    }

}
