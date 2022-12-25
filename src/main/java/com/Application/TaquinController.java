package com.Application;

import com.Game.Board.Board;
import com.Game.Board.DefaultBoardState;
import com.Game.Board.TargetBoardState;
import com.Game.Cell.CellUtilities;
import com.Game.Solver.*;
import com.Game.Solver.Heuristic.DisplacedTilesHeuristic;
import com.Game.Solver.Heuristic.ManhattanDistanceHeuristic;
import com.Game.Solver.Heuristic.UniformCostHeuristic;
import com.Parser.NewLineParser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
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
import java.io.FileWriter;
import java.io.IOException;
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
    @FXML
    private TextField maxRuntimeField;
    @FXML
    private TextField maxFrontierSizeField;

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
            default -> new UniformCostHeuristic();
        };

        TaquinSolutionAlgorithm algorithm = switch (chosenAlgorithm) {
            case "IDA*" -> new IDAStar(heuristic, withLogs);
            case "GreedyA*" -> new GreedyAstar(heuristic, withLogs);
            default -> new AStar(heuristic, withLogs);
        };

        var maxRuntime = this.maxRuntimeField.getText().isEmpty() ? -1 :
                TimeUnit.MILLISECONDS.toNanos(Long.parseLong(this.maxRuntimeField.getText()));
        var maxFrontier = this.maxFrontierSizeField.getText().isEmpty() ? -1 :
                Integer.parseInt(this.maxFrontierSizeField.getText());

        try {
            var solution = board.solve(algorithm, maxRuntime, maxFrontier);
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
            if (solution.expiredRuntime()) {
                System.out.println("Failed to find solution");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Algorithm exceeded the given max runtime");
                alert.show();
                return;
            }
            if (solution.expiredFrontierSize()) {
                System.out.println("Failed to find solution");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Algorithm exceeded the given max frontier size");
                alert.show();
                return;
            }
            System.out.println("Failed to find solution");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Unable to solve puzzle");
            alert.show();
            return;
        }
        for (var step : solution.solutionSteps()) {
            System.out.println("Instruction: " + step.instruction());
        }
        System.out.println("---Summary of Algorithm---");
        System.out.println("Solved size " + this.sizeField.getText() + " with " + chosenAlgorithm + " and " + chosenHeuristic);
        System.out.println("Solution length: " + solution.solutionSteps().size());
        String shuffleDepth = this.shuffleDepthField.getText();
        if (shuffleDepth.equals("")) {
            System.out.println("Puzzle is totally randomized");
        } else {
            System.out.println("Puzzle is " + shuffleDepth + " moves from goal");
        }
        System.out.println("Elapsed runtime: " + TimeUnit.NANOSECONDS.toMillis(solution.elapsedTime()));
        System.out.println("Max Frontier Size: " + solution.maxFrontierSize());
        System.out.println("Number of Expansions: " + solution.numberOfExpansions());
        board.setBoardState(solution.solutionSteps().get(solution.solutionSteps().size() - 1).state());
        updateBoard();
        writeExperiment(solution);
    }

    private void writeExperiment(TaquinSolutionHolder solutionHolder) {
        try {

            var subDir = this.sizeField.getText() + "x" + this.sizeField.getText();
            var puzzleName = "solved_" + this.sizeField.getText() + "x_" + shuffleDepthField.getText() + "depth_" +
                    ((chosenAlgorithm.equals("")) ? "A*" : chosenAlgorithm) + "_" +
                    ((chosenHeuristic.equals("")) ? "Uniform Cost" : chosenHeuristic).replace(' ', '_');

            File directory = new File("experiments/" + subDir + "/");
            directory.mkdirs();

            File file = new File("experiments/" + subDir + "/" + puzzleName + ".txt");
            int cmpt = 0;
            while (!file.createNewFile()) { // file already exsists
                file = new File("experiments/" + subDir + "/" + puzzleName + "(" + cmpt++ + ")" + ".txt");
            }

            FileWriter writer = new FileWriter(file);

            // Write some text to the file
            writer.write("Size: " + sizeField.getText());
            writer.write("\n");
            if (!chosenAlgorithm.equals("")) {
                writer.write("Algorithm: " + chosenAlgorithm);
            } else {
                writer.write("Algorithm: A*");
            }
            writer.write("\n");
            if (!chosenHeuristic.equals("")) {
                writer.write("Heuristic: " + chosenHeuristic);
            } else {
                writer.write("Heuristic: Uniform Cost");
            }
            writer.write("\n");
            if (!shuffleDepthField.getText().equals("")) {
                writer.write("Shuffled Depth: " + shuffleDepthField.getText());
            } else {
                writer.write("Shuffled Depth: Random");
            }
            writer.write("\n");
            writer.write("Solution length: " + solutionHolder.solutionSteps().size());
            writer.write("\n");
            writer.write("Runtime (millis): " + TimeUnit.NANOSECONDS.toMillis(solutionHolder.elapsedTime()));
            writer.write("\n");
            writer.write("Max Frontier Size: " + solutionHolder.maxFrontierSize());
            writer.write("\n");
            writer.write("Number of Expansions: " + solutionHolder.numberOfExpansions());

            // Close the writer to save the file
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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