package com.Application;

import com.Game.Board.Board;
import com.Game.Board.DefaultBoardState;
import com.Game.Board.TargetBoardState;
import com.Game.Cell.CellUtilities;
import com.Game.Solver.*;
import com.Game.Solver.Heuristic.DisplacedTilesHeuristic;
import com.Game.Solver.Heuristic.LinearConflictHeuristic;
import com.Game.Solver.Heuristic.ManhattanDistanceHeuristic;
import com.Game.Solver.Heuristic.UniformCostHeuristic;
import com.Parser.NewLineParser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * This class connects our user interface defined in hello-view.fxml to the Taquin functionality.
 * Our User interface is configurable along several parameters:
 * - Board size via the sizeField text input
 * - Chosen heuristic via the heuristic drop-down menu
 * - Whether to print detailed logging information via the log drop-down menu
 * - Chosen algorithm between A* and IDA* via the algorithm drop-down menu
 * - The number of randomized actions to take from the goal state to produce a shuffled board
 * <p>
 * We use these fields to initialize our board and algorithm when the Solve button is pressed.
 */
public class TaquinController implements Initializable {

    private Board board;

    @FXML
    private GridPane boardDisplay;

    @FXML
    private TextField sizeField;

    @FXML
    private ComboBox<String> heuristicCombo;
    @FXML
    private ComboBox<String> logCombo;
    @FXML
    private ComboBox<String> algorithmCombo;
    @FXML
    private TextField shuffleDepthField;

    private String chosenHeuristic = "";
    private String chosenAlgorithm = "";
    private boolean withLogs = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DefaultBoardState boardState = new DefaultBoardState(Integer.parseInt(this.sizeField.getText()));
        this.board = new Board(boardState, 0);
        this.boardDisplay.resize(100 * this.board.getSize(), 100 * this.board.getSize());

        this.heuristicCombo.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, newValue) -> chosenHeuristic = newValue
        );
        this.algorithmCombo.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, newValue) -> chosenAlgorithm = newValue
        );
        this.logCombo.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, newValue) -> withLogs = !newValue.equals("No")
        );

        this.updateBoard();
    }

    /**
     * Action behind the "New game" button. If a number of instructions is not mention, create a fully random board not
     * necessarily solvable.
     */
    @FXML
    private void onNewGameClick() {
        DefaultBoardState boardState = new DefaultBoardState(Integer.parseInt(this.sizeField.getText()));
        String shuffleDepth = this.shuffleDepthField.getText();
        if (shuffleDepth.equals("")) {
            this.board = new Board(boardState);
        } else {
            this.board = new Board(boardState, Integer.parseInt(shuffleDepth));
        }
        resizeWindow();
    }

    /**
     * Action behind the "Solve" button
     */
    @FXML
    private void onSolveClick() {
        System.out.println("Solving:");
        System.out.println(board.getBoardState().toString());

        var targetBoard = new TargetBoardState(Integer.parseInt(this.sizeField.getText()));
        System.out.println(targetBoard);

        var heuristic = switch (chosenHeuristic) {
            case "Manhattan Distance" -> new ManhattanDistanceHeuristic(targetBoard);
            case "Displacement" -> new DisplacedTilesHeuristic(targetBoard);
            case "Linear Conflict + M.D." -> new LinearConflictHeuristic(targetBoard);
            default -> new UniformCostHeuristic();
        };

        TaquinSolutionAlgorithm algorithm = switch (chosenAlgorithm) {
            case "IDA*" -> new IDAStar(heuristic, withLogs);
            case "GreedyA*" -> new GreedyAstar(heuristic, withLogs);
            default -> new AStar(heuristic, withLogs);
        };

        try {
            var solution = board.solve(algorithm, 0, 0);
            reportSolution(solution);
        } catch (OutOfMemoryError error) {
            System.out.println("---Summary of Algorithm---");
            System.out.println("Ran out of space on size " + this.sizeField.getText() + " with " + chosenAlgorithm + " and " + chosenHeuristic);
        }
    }

    private void reportSolution(TaquinSolutionHolder solution) {
        if (solution.solutionSteps() == null) {
            System.out.println("Already solved");
            return;
        }
        if (solution.solutionSteps().isEmpty()) {
            System.out.println("Failed to find solution");
            return;
        }
        for (var step : solution.solutionSteps()) {
            System.out.println("Instruction: " + step.instruction());
        }
        System.out.println("---Summary of Algorithm---");
        System.out.println("Solved size " + this.sizeField.getText() + " with " + chosenAlgorithm + " and " + chosenHeuristic);
        System.out.println("Solution length: " + solution.solutionSteps().size());
        System.out.println("Elapsed runtime: " + TimeUnit.NANOSECONDS.toMillis(solution.elapsedTime()));
        System.out.println("Max Frontier Size: " + solution.maxFrontierSize());
        System.out.println("Number of Expansions: " + solution.numberOfExpansions());
        board.setBoardState(solution.solutionSteps().get(solution.solutionSteps().size() - 1).state());
        updateBoard();
    }

    /**
     * Method behind the "From file" button
     */
    @FXML
    private void onFromFileClick() {

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Find your game file");
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }
        NewLineParser newLineParser = new NewLineParser();
        Board board;
        try {
            board = newLineParser.parseFile(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error while reading the file");
            return;
        }
        this.board = board;
        this.sizeField.setText(String.valueOf(this.board.getSize()));
        resizeWindow();
    }

    /**
     * Used to resize to window to have a clear display
     */
    private void resizeWindow() {
        this.boardDisplay.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= Integer.parseInt(this.sizeField.getText()));
        this.boardDisplay.getChildren().removeIf(node -> GridPane.getColumnIndex(node) >= Integer.parseInt(this.sizeField.getText()));
        this.boardDisplay.resize(100 * this.board.getSize() + 200, 100 * this.board.getSize() + 150);
        this.updateBoard();
        this.boardDisplay.getScene().getWindow().setHeight(this.boardDisplay.getHeight());
        this.boardDisplay.getScene().getWindow().setWidth(this.boardDisplay.getWidth() + 100);
    }

    /**
     * Helper method that takes the state of the board and renders the grid. Then attaches an action to each button
     * which swaps its place with the empty tile if it is available
     */
    private void updateBoard() {
        for (int x = 0; x < this.board.getSize(); x++) {
            for (int y = 0; y < this.board.getSize(); y++) {

                Button button = new Button();
                button.setAlignment(Pos.CENTER);
                button.setMaxSize(50, 50);
                button.setMinSize(50, 50);
                var value = this.board.getBoardState().getAtPosition(x, y);
                button.setText(CellUtilities.getStrValueOfCell(value));
                int finalX = x;
                int finalY = y;
                button.setOnAction(event -> {
                    this.board.move(finalX, finalY);
                    updateBoard();
                });

                GridPane.setRowIndex(button, y);
                GridPane.setColumnIndex(button, x);
                this.boardDisplay.getChildren().add(button);
            }

        }

        for (int j = 0; j < this.board.getSize(); j++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            this.boardDisplay.getColumnConstraints().add(cc);
        }

        for (int j = 0; j < this.board.getSize(); j++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            this.boardDisplay.getRowConstraints().add(rc);
        }
    }
}