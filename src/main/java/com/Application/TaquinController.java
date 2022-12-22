package com.Application;

import com.Game.Board.Board;
import com.Game.Board.DefaultBoardState;
import com.Game.Board.TargetBoardState;
import com.Game.Cell.DefaultCellFactory;
import com.Game.Solver.AStar;
import com.Game.Solver.Heuristic.DisplacedTilesHeuristic;
import com.Game.Solver.Heuristic.LinearConflictHeuristic;
import com.Game.Solver.Heuristic.ManhattanDistanceHeuristic;
import com.Game.Solver.Heuristic.UniformCostHeuristic;
import com.Game.Solver.IDAStar;
import com.Game.Solver.TaquinSolutionAlgorithm;
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
        this.board = new Board(boardState, new DefaultCellFactory());
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

    @FXML
    private void onNewGameClick() {
        DefaultBoardState boardState = new DefaultBoardState(Integer.parseInt(this.sizeField.getText()));
        String shuffleDepth = this.shuffleDepthField.getText();
        if (shuffleDepth.equals("")) {
            this.board = new Board(boardState, new DefaultCellFactory());
        } else {
            this.board = new Board(boardState, new DefaultCellFactory(), Integer.parseInt(shuffleDepth));
        }
        resizeWindow();
    }

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
            case "IDA*" -> new IDAStar(heuristic, new DefaultCellFactory(), withLogs);
            default -> new AStar(heuristic, new DefaultCellFactory(), withLogs);
        };

        var solution = board.solve(algorithm);
        if (solution == null) {
            System.out.println("Already solved");
            return;
        }
        if (solution.isEmpty()) {
            System.out.println("Failed to find solution");
            return;
        }
        for (var step : solution) {
            System.out.println("Instruction: " + step.instruction());
        }
        board.setBoardState(solution.get(solution.size() - 1).state());
        updateBoard();
    }

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

    private void resizeWindow() {
        this.boardDisplay.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= Integer.parseInt(this.sizeField.getText()));
        this.boardDisplay.getChildren().removeIf(node -> GridPane.getColumnIndex(node) >= Integer.parseInt(this.sizeField.getText()));
        this.boardDisplay.resize(100 * this.board.getSize(), 100 * this.board.getSize());
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
                button.setText(String.valueOf(this.board.getBoardState().getAtPosition(x, y).getRepresentation()));
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