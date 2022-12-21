# Taquin - SlidingGame

Implementation of an arbitrarily sized Taquin/Sliding puzzle for the project Artificial Intelligence at Dauphine PSL.

To run the project:
`Insert command to generate and or run the JAR`

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