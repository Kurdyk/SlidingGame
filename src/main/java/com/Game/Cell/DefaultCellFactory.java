package com.Game.Cell;

public class DefaultCellFactory implements CellFactory {

    @Override
    public TaquinCell createTaquinCell(int id, int x, int y) {
        return new StringRepresentationCell(id, x, y);
    }

    @Override
    public TaquinCell createEmpty(int x, int y) {
        return new StringRepresentationCell(x, y);
    }
}
