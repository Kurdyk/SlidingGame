package Game.Solver;

import Game.Board.TaquinBoardInstruction;
import Game.Board.TaquinBoardState;

public class SolutionStep {
    private TaquinBoardState state;
    private SolutionStep parentState;
    private TaquinBoardInstruction instruction;


    public SolutionStep(TaquinBoardState state, SolutionStep parentState, TaquinBoardInstruction instruction) {
        this.state = state;
        this.parentState = parentState;
        this.instruction = instruction;
    }

    public TaquinBoardState getState() {
        return state;
    }

    public SolutionStep getParentState() {
        return parentState;
    }

    public TaquinBoardInstruction getInstruction() {
        return instruction;
    }
}
