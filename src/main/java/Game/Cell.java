package Game;

public class Cell {
    
    private Integer cellId;

    public Cell(int cellId) {
        this.cellId = cellId;
    }

    public Cell() {
        this.cellId = null;
    }

    public Integer getCellId() {
        return cellId;
    }

    protected void setCellId(Integer cellId) {
        this.cellId = cellId;
    }
}