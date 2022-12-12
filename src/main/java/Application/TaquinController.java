package Application;

import Game.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TaquinController implements Initializable {

    private Board board;

    @FXML
    private GridPane boardDisplay;

    @FXML
    private TextField sizeField;

    private void updateBoard() {


        for(int x = 0; x < this.board.getSize(); x++) {
            for (int y = 0; y < this.board.getSize(); y++) {

                Button button = new Button();
                button.setAlignment(Pos.CENTER);
                button.setMaxSize(50, 50);
                button.setMinSize(50, 50);
                button.setText(String.valueOf(this.board.getBoardState().get(x).get(y).getCellId()));
                int finalX = x;
                int finalY = y;
                button.setOnAction(event -> {
                    this.board.move(finalX, finalY);
                    updateBoard();
                });

                this.boardDisplay.setRowIndex(button, x);
                this.boardDisplay.setColumnIndex(button, y);
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
        this.board = new Board(Integer.parseInt(this.sizeField.getText()));
        this.boardDisplay.resize(100 * this.board.getSize(), 100 * this.board.getSize());
        this.updateBoard();
    }

    @FXML
    private void onNewGameClick() {
        this.board = new Board(Integer.parseInt(this.sizeField.getText()));
        this.boardDisplay.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= Integer.parseInt(this.sizeField.getText()));
        this.boardDisplay.getChildren().removeIf(node -> GridPane.getColumnIndex(node) >= Integer.parseInt(this.sizeField.getText()));
        this.boardDisplay.resize(100 * this.board.getSize(), 100 * this.board.getSize());
        this.updateBoard();
        this.boardDisplay.getScene().getWindow().setHeight(this.boardDisplay.getHeight());
        this.boardDisplay.getScene().getWindow().setWidth(this.boardDisplay.getWidth() + 100);
        System.out.println(this.board);
    }
}