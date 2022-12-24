package com.Parser;

import com.Game.Board.Board;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * An interface to read Boards from files.
 */
public interface Parser {

    /**
     * Read a Board from a file
     * @param file the file containing the wanted board
     * @return A game Board
     * @throws FileNotFoundException in case of a non-existing file
     */
    public Board parseFile(File file) throws FileNotFoundException;
}
