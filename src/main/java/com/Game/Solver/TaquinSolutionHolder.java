package com.Game.Solver;

import java.util.Collections;
import java.util.List;

public record TaquinSolutionHolder(
        List<SolutionStep> solutionSteps,
        long elapsedTime,
        long maxFrontierSize,
        long numberOfExpansions
) {

    static public TaquinSolutionHolder getEmpty() {
        return new TaquinSolutionHolder(Collections.emptyList(), 0, 0, 0);
    }
}
