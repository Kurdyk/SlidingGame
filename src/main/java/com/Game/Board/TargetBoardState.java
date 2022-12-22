package com.Game.Board;

import com.Game.Cell.StringRepresentationCell;

public class TargetBoardState extends DefaultBoardState {

    public TargetBoardState(int size) {
        super(size);
        int id = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (y == size - 1 && x == size - 1) {
                    addCell(new StringRepresentationCell(x, y));
                } else {
                    addCell(new StringRepresentationCell(id, x, y));
                    id++;
                }
            }
        }
    }
}
