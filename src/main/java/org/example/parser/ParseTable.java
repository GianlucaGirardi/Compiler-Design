package org.example.parser;

import lombok.RequiredArgsConstructor;
import org.example.parser.grammar.NonTerminal;
import org.example.parser.grammar.Production;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public final class ParseTable {

    private final Map<NonTerminal, Map<String, Production>> table;

    public Production get(NonTerminal nt, String lookahead) {
        var row = table.get(nt);
        return row == null ? null : row.get(lookahead);
    }

    public Set<String> expected(NonTerminal nt) {
        var row = table.get(nt);
        return row == null ? Set.of() : row.keySet();
    }
}

