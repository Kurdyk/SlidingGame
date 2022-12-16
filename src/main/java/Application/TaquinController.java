package Application;

import Game.Board.Board;
import Game.Board.DefaultBoardState;
import Game.Board.TargetBoardState;
import Game.Cell.DefaultCellFactory;
import Game.Solver.AStar;
import Game.Solver.Heuristic.ManhattanDistanceHeuristic;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.ResourceBundle;

public class TaquinController implements Initializable {

    private Board board;

    @FXML
    private GridPane boardDisplay;

    @FXML
    private TextField sizeField;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DefaultBoardState boardState = new DefaultBoardState(Integer.parseInt(this.sizeField.getText()));
        this.board = new Board(boardState, new DefaultCellFactory());
        this.boardDisplay.resize(100 * this.board.getSize(), 100 * this.board.getSize());
        this.updateBoard();
    }

    @FXML
    private void onNewGameClick() {
        DefaultBoardState boardState = new DefaultBoardState(Integer.parseInt(this.sizeField.getText()));
        this.board = new Board(boardState, new DefaultCellFactory());
        this.boardDisplay.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= Integer.parseInt(this.sizeField.getText()));
        this.boardDisplay.getChildren().removeIf(node -> GridPane.getColumnIndex(node) >= Integer.parseInt(this.sizeField.getText()));
        this.boardDisplay.resize(100 * this.board.getSize(), 100 * this.board.getSize());
        this.updateBoard();
        this.boardDisplay.getScene().getWindow().setHeight(this.boardDisplay.getHeight());
        this.boardDisplay.getScene().getWindow().setWidth(this.boardDisplay.getWidth() + 100);
        System.out.println(this.board);
    }

    @FXML
    private void onSolveClick() {
        var heuristic = new ManhattanDistanceHeuristic(new TargetBoardState(Integer.parseInt(this.sizeField.getText())));
        var solution = board.solve(new AStar(heuristic, new DefaultCellFactory()));
        for (var step : solution) {
            System.out.println("Instruction: " + step.instruction());
        }
        board.setBoardState(solution.get(solution.size() - 1).state());
        updateBoard();
    }
}