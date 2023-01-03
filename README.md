# Taquin - SlidingGame

Implementation of an arbitrarily sized Taquin/Sliding puzzle for the project Artificial Intelligence at Dauphine PSL.

To run the project:<br>
Requirements:
- `maven` (we are on version 3.8.7) Consider using an up-to-date version from the <a hlink=https://maven.apache.org/>official maven website</a>. 
- Java version: `17.0.5`
Running the project
- You can simply run the project with `mvn javafx:run` from the root project directory
- You can also build a jar with `mvn package`. A fat jar will be generated in the target folder named `target/Taquin-1.0-SNAPSHOT-jar-with-dependencies.jar`.
You can then run it with `java -jar`.



The User Interface includes an option to generate a puzzle from an input.
Sample inputs are located in the `BoardExamples` directory.

The code is commented with JavaDoc style comments describing the important classes and methods.
To start, one can look at:
- `src/main/java/Application/TaquinApp.java` to see the entry point of the user interface
- `src/main/java/Application/TaquinController.java` to see the setup of the user interface and the connection of
the various parts of the project
- `src/main/java/Game/Board/` includes the abstractions and implementations of the game and the board
- `src/main/java/Game/Cell/` includes the abstractions and implementations and utilities for individual cells
- `src/main/java/Game/Solver/` includes the abstractions and implementations of our algorithms
- `src/main/java/Game/Solver/Heuristic/` includes the abstractions and implementations of our heuristics
