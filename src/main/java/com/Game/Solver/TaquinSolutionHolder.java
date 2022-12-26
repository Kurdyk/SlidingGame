package com.Game.Solver;

import java.util.Collections;
import java.util.List;

public record TaquinSolutionHolder(
        List<SolutionStep> solutionSteps,
        long elapsedTime,
        long maxFrontierSize,
        long numberOfExpansions,
        boolean expiredRuntime,
        boolean expiredFrontierSize
) {

    static public TaquinSolutionHolder getEmpty() {
        return new TaquinSolutionHolder(
                Collections.emptyList(),
                0,
                0,
                0,
                false,
                false
        );
    }

    static public TaquinSolutionHolder getExpiredRuntime() {
        return new TaquinSolutionHolder(
                Collections.emptyList(),
                0,
                0,
                0,
                true,
                false
        );
    }

    static public TaquinSolutionHolder getExpiredFrontierSize() {
        return new TaquinSolutionHolder(
                Collections.emptyList(),
                0,
                0,
                0,
                false,
                true
        );
    }
}
