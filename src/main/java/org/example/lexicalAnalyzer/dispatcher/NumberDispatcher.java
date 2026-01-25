package org.example.lexicalAnalyzer.dispatcher;

import lombok.RequiredArgsConstructor;

import org.example.lexicalAnalyzer.ICharStream;
import org.example.lexicalAnalyzer.config.DfaRegistry;
import org.example.lexicalAnalyzer.config.TransitionTable;
import org.example.lexicalAnalyzer.token.Token;

import java.util.Set;

import static org.example.lexicalAnalyzer.config.Constants.*;

@RequiredArgsConstructor
public class NumberDispatcher implements Dispatcher {

    private final String dfaName;

    private static final Set<Character> DELIMITER_SET = Set.of(
            PLUS, MINUS, STAR, SLASH, LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET, RBRACKET, SEMICOLON,
            COMMA, COLON);

    @Override
    public Token processToken(ICharStream stream) {
        TransitionTable table = initTable();
        int line = stream.getLine();

        ScanResult result = consumeNumberLexeme(stream, table);
        String lexeme = result.lexeme();
        String endState = result.endState();

        if (lexeme.isEmpty()) return null;
        if (result.forcedInvalid()) return new Token(getInvalidTokenType(null), lexeme, line, false);

        String stateType = table.getStateToType().get(endState);
        if (stateType != null && table.isAcceptState(endState)){
            return new Token(stateType, lexeme, line, true);
        }

        return new Token(getInvalidTokenType(null), lexeme, line, false);
    }

    public TransitionTable initTable() {
        TransitionTable table = DfaRegistry.getTable(dfaName);
        table.reset();
        return table;
    }

    @Override
    public boolean isDelimiter(char c) {
        return Character.isWhitespace(c) || DELIMITER_SET.contains(c);
    }

    @Override
    public String getInvalidTokenType(String lexeme) {
        return INVALID_NUMBER_MESSAGE;
    }

    private ScanResult consumeNumberLexeme(ICharStream stream, TransitionTable table) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean forcedInvalid = false;

        while (!stream.isAtEnd()) {
            char c = stream.peek();
            String classifiedSymbol = classify(c);
            String state = table.getCurrentState();

            if (isInAlphabet(classifiedSymbol) && table.hasTransition(state, classifiedSymbol)) {
                table.nextState(state, classifiedSymbol);
                stringBuilder.append(c);
                stream.advance();
                continue;
            }

            /* isEndOfToken */
            if (isDelimiter(c)) break;

            /* consume invalid tail */
            forcedInvalid = true;
            while (!stream.isAtEnd()) {
                char next = stream.peek();

                if (isDelimiter(next)) break;
                stringBuilder.append(next);
                stream.advance();
            }
            break;
        }

        return new ScanResult(stringBuilder.toString(), table.getCurrentState(), forcedInvalid);
    }

    private String classify(char inputSymbol) {
        if (inputSymbol == ZERO_DIGIT) return ZERO_DIGIT_NAME;
        else if (isNonZeroDigit(inputSymbol)) return NONZERO_DIGIT_NAME;
        else if (inputSymbol == DOT) return DOT_NAME;
        else if (inputSymbol == PLUS) return PLUS_NAME;
        else if (inputSymbol == MINUS) return MINUS_NAME;
        else if (inputSymbol == LOWER_CASE_E) return LOWER_CASE_E_NAME;

        return null;
    }

    private boolean isNonZeroDigit(char inputSymbol) {
        return inputSymbol > ZERO_DIGIT && inputSymbol <= NINE_DIGIT;
    }

    private boolean isInAlphabet(String classifiedSymbol) {
        return classifiedSymbol!= null;
    }
}
