package Game.Cell;

public class StringRepresentationCell extends TaquinCell {

    public StringRepresentationCell(int cellId, int x, int y) {
        super(cellId, x, y);
    }

    public StringRepresentationCell(int x, int y) {
        super(x, y);
    }


    @Override
    public String getRepresentation() {
        return getCellId() == BLANK_ID ? "" : String.valueOf(getCellId());
    }
}