package Game.Cell;

import java.util.Objects;

public abstract class TaquinCell {

    public static int BLANK_ID = 1013;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaquinCell that = (TaquinCell) o;
        return id == that.id && position.equals(that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position);
    }
}
