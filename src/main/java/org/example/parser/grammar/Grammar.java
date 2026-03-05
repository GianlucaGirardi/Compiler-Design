package org.example.parser.grammar;

import lombok.Getter;

import java.util.*;

@Getter
public class Grammar {

    public static final String EPS = "#";

    private static final String END = "eof";

    private final String startSymbol;

    private final Map<String, List<List<String>>> productions;

    private final Set<String> nonTerminals;

    private final Set<String> terminals;

    private final Map<String, Set<String>> firstSets = new HashMap<>();

    private final Map<String, Set<String>> followSets = new HashMap<>();

    public Grammar(String startSymbol, Map<String, List<List<String>>> productions) {
        this.startSymbol = startSymbol;
        this.productions = productions;
        this.nonTerminals = new HashSet<>(productions.keySet());
        this.terminals = computeTerminals(this.nonTerminals, productions);

        computeFirst();
        computeFollow();
    }

    public boolean isNonTerminal(String sym) {
        return nonTerminals.contains(sym);
    }

    public boolean isTerminal(String sym) {
        return !isNonTerminal(sym) && !Grammar.EPS.equals(sym);
    }

    private static Set<String> computeTerminals(Set<String> nonTerminals,
                                                Map<String, List<List<String>>> prods) {
        Set<String> ts = new HashSet<>();
        for (var entry : prods.entrySet()) {
            for (List<String> rhs : entry.getValue()) {
                for (String sym : rhs) {
                    if (!nonTerminals.contains(sym)) ts.add(sym);
                }
            }
        }
        return ts;
    }

    private void computeFirst() {

        for (String nt : nonTerminals) {
            firstSets.put(nt, new HashSet<>());
        }

        boolean changed = true;

        while (changed) {
            changed = false;

            for (String A : nonTerminals) {
                for (List<String> rhs : productions.get(A)) {

                    Set<String> firstOfSeq = firstOfSequence(rhs);

                    if (firstSets.get(A).addAll(firstOfSeq)) {
                        changed = true;
                    }
                }
            }
        }
    }

    public Set<String> firstOf(String symbol) {
        if (symbol.equals(EPS)) return Set.of(EPS);

        if (isTerminal(symbol)) return Set.of(symbol);

        return firstSets.get(symbol);
    }

    public Set<String> firstOfSequence(List<String> sequence) {

        if (sequence.isEmpty()) {
            return Set.of(EPS);
        }

        Set<String> result = new HashSet<>();
        boolean allNullable = true;

        for (String symbol : sequence) {

            Set<String> first = firstOf(symbol);

            for (String s : first) {
                if (!s.equals(EPS)) {
                    result.add(s);
                }
            }

            if (!first.contains(EPS)) {
                allNullable = false;
                break;
            }
        }

        if (allNullable) {
            result.add(EPS);
        }

        return result;
    }

    private void computeFollow() {

        for (String nt : nonTerminals) {
            followSets.put(nt, new HashSet<>());
        }
        followSets.get(startSymbol).add(END);

        boolean changed = true;

        while (changed) {
            changed = false;

            for (String A : nonTerminals) {

                for (List<String> rhs : productions.get(A)) {

                    for (int i = 0; i < rhs.size(); i++) {
                        String B = rhs.get(i);
                        if (!isNonTerminal(B)) continue;
                        List<String> beta = rhs.subList(i + 1, rhs.size());
                        Set<String> firstBeta = firstOfSequence(beta);
                        for (String s : firstBeta) {
                            if (!s.equals(EPS)) {
                                if (followSets.get(B).add(s)) {
                                    changed = true;
                                }
                            }
                        }

                        if (beta.isEmpty() || firstBeta.contains(EPS)) {
                            if (followSets.get(B).addAll(followSets.get(A))) {
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
    }
}
