package com.Game.Cell;

public interface CellFactory {
    TaquinCell createTaquinCell(int id, int x, int y);

    TaquinCell createEmpty(int x, int y);
}
