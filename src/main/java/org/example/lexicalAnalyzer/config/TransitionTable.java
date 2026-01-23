package org.example.lexicalAnalyzer.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Set;


@Data
@AllArgsConstructor
public class TransitionTable {

    private String name;

    private Set<String> alphabet;

    private Set<String> acceptStates;

    private String startState;

    private String currentState;

    private Set<String> trapStates;

    private Map<String, Map<String, String>> transitions;

    private Map<String, String> stateToType;

    private final String DEFAULT = "DEFAULT";

    public void nextState(String state, String symbolType) {
        Map<String, String> row = transitions.get(state);
        if (row == null || !row.containsKey(symbolType)) {
            throw new IllegalStateException("No transition from " + state + " on " + symbolType);
        }
        String next = row.get(symbolType);
        setCurrentState(next);
    }


    public boolean isAcceptState(String state) {
        return acceptStates.contains(state);
    }

    public boolean isTrapState(String state) {
        return trapStates != null && trapStates.contains(state);
    }

    public void reset() {
        this.currentState = startState;
    }


    public boolean hasTransition(String state, String symbolType) {
        return state != null
                && symbolType != null
                && transitions.containsKey(state)
                && transitions.get(state).containsKey(symbolType);
    }
}