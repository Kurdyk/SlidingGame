package Parser;

import Game.Board.Board;

import java.io.File;
import java.io.FileNotFoundException;

public interface Parser {

    public Board parseFile(File file) throws FileNotFoundException;
}
