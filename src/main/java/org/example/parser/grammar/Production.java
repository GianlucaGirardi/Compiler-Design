package org.example.parser.grammar;

import java.util.List;

public record Production(

        NonTerminal lhs,

        List<Symbol> rhs
) {
}
