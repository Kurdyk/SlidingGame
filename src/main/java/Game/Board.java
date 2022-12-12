package Game;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    
    private ArrayList<ArrayList<Cell>> boardState = new ArrayList<>();
    private int size;
    
    
    public Board(int size) {
        
        this.size = size;
        for (int i = 0; i < size; i++) {
            this.boardState.add(new ArrayList<>());
        }

        this.shuffle();

    }

    public ArrayList<ArrayList<Cell>> getBoardState() {
        return boardState;
    }

    public int getSize() {
        return size;
    }

    private void shuffle() {
        int nbOfCells = size * size - 1;

        ArrayList<Integer> availableIds = new ArrayList<>();
        for (int i = 0; i < nbOfCells; i++) {
            availableIds.add(i);
        }

        Random random = new Random();

        int lineNumber = -1;
        for (int i = 0; i < nbOfCells; i++) {
            if (i % size == 0) {
                lineNumber++;
            }
            int selected = random.nextInt(availableIds.size());
            this.boardState.get(lineNumber).add(new Cell(availableIds.get(selected))); // add cell with number
            availableIds.remove(selected);
        }

        this.boardState.get(lineNumber).add(new Cell()); // add empty cell
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                stringBuilder.append(this.boardState.get(i).get(j).getCellId() + ", ");
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public void move(int x, int y) {
        try {
            if (this.boardState.get(x).get(y - 1).getCellId() == null) {
                swap(this.boardState.get(x).get(y), this.boardState.get(x).get(y - 1));
                return;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            if (this.boardState.get(x).get(y + 1).getCellId() == null) {
                swap(this.boardState.get(x).get(y), this.boardState.get(x).get(y + 1));
                return;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            if (this.boardState.get(x - 1).get(y).getCellId() == null) {
                swap(this.boardState.get(x).get(y), this.boardState.get(x - 1).get(y));
                return;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            if (this.boardState.get(x + 1).get(y).getCellId() == null) {
                swap(this.boardState.get(x).get(y), this.boardState.get(x + 1).get(y));
                return;
            }
        } catch (IndexOutOfBoundsException ignored) {}
    }

    private void swap(Cell cell1, Cell cell2) {
        Integer id2 = cell2.getCellId();
        cell2.setCellId(cell1.getCellId());
        cell1.setCellId(id2);
    }
}
