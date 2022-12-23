package Parser;

import Game.Board.Board;
import Game.Board.DefaultBoardState;
import Game.Board.TaquinBoardState;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class NewLineParser implements Parser {

    @Override
    public Board parseFile(File file) throws FileNotFoundException {

        ArrayList<ArrayList<Short>> cellContent = new ArrayList<>();
        int lineCount = 0;

        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String currentLine = scanner.nextLine();
            if (currentLine.contains("#")) { // option to add comment to the file
                System.out.println("Comment found in file : " + currentLine);
                continue;
            }
            cellContent.add(new ArrayList<>());
            String[] lineContent = currentLine.split(",|;"); // split the line at each ; or ,
            for (String cell : lineContent) {
                String striped = cell.replaceAll(" ", "");
                short content;
                if (striped.length() == 0) { // empty cell
                    content = TaquinBoardState.EMPTY_ID;
                } else {
                    content = Short.parseShort(striped);
                }
                cellContent.get(lineCount).add(content);
            }
            lineCount++;
        }
        DefaultBoardState defaultBoardState = new DefaultBoardState(lineCount);
        return new Board(defaultBoardState, cellContent);
    }
}
