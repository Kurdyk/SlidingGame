package Game.Cell;

import java.util.Objects;

/**
 * Base implementation of a TaquinCell
 * Stores the current position and id of a cell.
 * We separate the concept of id from representation.
 * This allows us to decide with what label we want to display a cell.
 * Currently, we only have cells that represent their label as a string.
 * However, this abstraction would allow us to instead represent the board with an image or something of the sort
 * and our algorithms would work equally well.
 */
public abstract class TaquinCell {

    /*
        Value of the BLANK_ID is randomly chosen to be a relatively large prime number.
        Instances of a Taquin puzzle of size 1013x1013 and greater are totally unsolvable for reasons unrelated
        to the value of the BLANK_ID. So this value is more or less arbitrary.
     */
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
