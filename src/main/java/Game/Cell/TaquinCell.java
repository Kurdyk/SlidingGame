package Game.Cell;

public abstract class TaquinCell {

    public static int BLANK_ID = -1;
    public static int INVALID_POS = -1;

    private final int id;
    private Position position;

    public TaquinCell(int id, int startX, int startY) {
        this.id = id;
        position = new Position(startX, startY);
    }

    public TaquinCell(int startX, int startY) {
        id = BLANK_ID;
        position = new Position(startX, startY);
    }

    public abstract String getRepresentation();

    public int getCellId() {
        return this.id;
    }

    public boolean isEmpty() {
        return id == BLANK_ID;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
