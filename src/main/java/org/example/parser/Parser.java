package org.example.parser;

import lombok.RequiredArgsConstructor;
import org.example.parser.grammar.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public final class Parser {

    private final ParseTable table;

    private final TokenStream tokenStream;

    private final Grammar ll1;

    private final BufferedWriter derivationWriter;

    private final BufferedWriter syntaxErrorWriter;

    private boolean hadError = false;

    private final List<Symbol> sentential = new ArrayList<>();

    public boolean parse() {
        Deque<Symbol> stack = initStack();
        initDerivationTrace();
        writeSententialForm();

        while (!stack.isEmpty()) {
            Symbol top = stack.peek();
            String lookahead = lookaheadType();

            if (top instanceof Terminal t) {
                matchTerminalWithRecovery(stack, t, lookahead);
            } else if (top instanceof NonTerminal nt) {
                expandNonTerminalWithRecovery(stack, nt, lookahead);
            } else {
                throw new IllegalStateException("Unknown symbol on stack: " + top);
            }
        }

        return isEofLookahead() && !hadError;
    }

    private Deque<Symbol> initStack() {
        Deque<Symbol> stack = new ArrayDeque<>();
        List.of(new Terminal("eof"), NonTerminal.START).forEach(stack::push);
        return stack;
    }

    private void initDerivationTrace() {
        sentential.clear();
        sentential.add(NonTerminal.START);
    }

    private String lookaheadType() {
        return tokenStream.peek().getType();
    }

    private boolean isEofLookahead() {
        return "eof".equals(lookaheadType());
    }

    private void matchTerminalWithRecovery(Deque<Symbol> stack, Terminal expected, String lookahead) {
        if (expected.raw().equals(lookahead)) {
            stack.pop();
            tokenStream.consume();
            return;
        }

        hadError = true;
        syntaxError("expected " + expected.raw() + " but found " + lookahead + " on line " +
                tokenStream.peek().getLine() + " Error lexeme: " + tokenStream.peek().getLexeme());

        while (!expected.raw().equals(lookaheadType()) && !isEofLookahead()) {
            tokenStream.consume();
        }

        if (expected.raw().equals(lookaheadType())) {
            return;
        }

        stack.pop();
        syntaxError("inserting missing token: " + expected.raw());
    }

    private void expandNonTerminalWithRecovery(Deque<Symbol> stack, NonTerminal nt, String lookahead) {
        Production p = table.get(nt, lookahead);

        if (p != null) {
            applyDerivationStep(nt, p.rhs());
            writeSententialForm();

            stack.pop();
            pushRhsReversed(stack, p.rhs());
            return;
        }

        skipError(stack, nt);
    }

    private void skipError(Deque<Symbol> stack, NonTerminal nt) {
        hadError = true;
        String A = ntKey(nt);
        String lookahead = lookaheadType();

        syntaxError("syntax error at " + lookahead +
                " (line " + tokenStream.peek().getLine() + "), expected: " + table.expected(nt) +
                " Error lexeme: " + tokenStream.peek().getLexeme());

        Set<String> firstA = ll1.getFirstSets().getOrDefault(A, Set.of());
        Set<String> followA = ll1.getFollowSets().getOrDefault(A, Set.of());
        boolean epsInFirst = firstA.contains(Grammar.EPS);

        // if follow has A we can skip
        if (isEofLookahead() || followA.contains(lookahead)) {
            applyDerivationStep(nt, List.of());
            writeSententialForm();

            stack.pop();
            return;
        }

        // Keep consuming input tokens until we reach a token that is allowed to restart or finish A.
        while (!firstA.contains(lookaheadType()) &&
                !(epsInFirst && followA.contains(lookaheadType())) &&
                !isEofLookahead()) {
            tokenStream.consume();
        }
    }

    private String ntKey(NonTerminal nt) {
        return nt.name();
    }

    private void pushRhsReversed(Deque<Symbol> stack, List<Symbol> rhs) {
        for (int i = rhs.size() - 1; i >= 0; i--) {
            stack.push(rhs.get(i));
        }
    }

    private void syntaxError(String msg) {
        try {
            syntaxErrorWriter.write("Syntax error: " + msg);
            syntaxErrorWriter.newLine();
            syntaxErrorWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error writing syntax errors", e);
        }
    }

    private void applyDerivationStep(NonTerminal nt, List<Symbol> rhs) {
        int idx = indexOfLeftmost(sentential, nt);
        if (idx < 0) {
            return;
        }
        sentential.remove(idx);
        if (!rhs.isEmpty()) {
            sentential.addAll(idx, rhs);
        }
    }

    private int indexOfLeftmost(List<Symbol> list, NonTerminal target) {
        for (int i = 0; i < list.size(); i++) {
            Symbol s = list.get(i);
            if (s instanceof NonTerminal nt && nt == target) return i;
        }
        for (int i = 0; i < list.size(); i++) {
            Symbol s = list.get(i);
            if (s instanceof NonTerminal nt && nt.name().equals(target.name())) return i;
        }
        return -1;
    }

    private void writeSententialForm() {
        try {
            for (int i = 0; i < sentential.size(); i++) {
                if (i > 0) derivationWriter.write(" ");

                Symbol s = sentential.get(i);
                if (s instanceof Terminal t) {
                    derivationWriter.write(t.raw());
                } else {
                    derivationWriter.write(s.toString());
                }
            }
            derivationWriter.newLine();
            derivationWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error writing derivation", e);
        }
    }
}
