package org.example.lexicalAnalyzer.dispatcher;

import org.example.lexicalAnalyzer.ICharStream;
import org.example.lexicalAnalyzer.config.DfaRegistry;
import org.example.lexicalAnalyzer.config.TransitionTable;
import org.example.lexicalAnalyzer.token.IdKeywordTokenType;
import org.example.lexicalAnalyzer.token.Token;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.example.lexicalAnalyzer.config.Constants.*;

public class IdDispatcher implements Dispatcher{

    private final String dfaName;

    private final Map<String, IdKeywordTokenType> idKeywordTokenTypeMap;

    private static final Set<Character> DELIMITER_SET = Set.of(
            PLUS, MINUS, STAR, SLASH, LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET, RBRACKET, SEMICOLON,
            COMMA, COLON );

    public IdDispatcher(String dfaName){
        this.dfaName = dfaName;
        idKeywordTokenTypeMap = new HashMap<>();
        Arrays.stream(IdKeywordTokenType.values()).filter(token -> token.getLexeme() != null)
                .forEach((token ->
                idKeywordTokenTypeMap.put(token.getLexeme(), token)));
    }

    @Override
    public Token processToken(ICharStream stream) {
        TransitionTable table = initTable();
        int line = stream.getLine();

        ScanResult result = consumeIdLexeme(stream, table);
        String lexeme = result.lexeme();

        if (lexeme.isEmpty()) return null;

        if (result.forcedInvalid()) return new Token(getInvalidTokenType(null), lexeme, line, false);

        IdKeywordTokenType tokenType = idKeywordTokenTypeMap.get(lexeme);
        if (isValidKeyword(tokenType)) return new Token(tokenType.getName(), lexeme, line, true);

        return new Token(IdKeywordTokenType.ID.getName(), lexeme, line, true);
    }

    private ScanResult consumeIdLexeme(ICharStream stream, TransitionTable table) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean forcedInvalid = false;

        while (!stream.isAtEnd()) {
            char c = stream.peek();
            String charClass = classify(c);

            /* not in sigma */
            if (charClass == null) break;

            String state = table.getCurrentState();

            /* guard */
            if (!table.hasTransition(state, charClass)) break;

            table.nextState(state, charClass);
            stringBuilder.append(c);
            stream.advance();

            /* If invalid -> consume tail && force invalid */
            if (table.isTrapState(table.getCurrentState())) {
                forcedInvalid = true;
            }
        }
        return new ScanResult(stringBuilder.toString(), table.getCurrentState(), forcedInvalid);
    }

    public String classify(char c){
        if((c >= LOWER_CASE_START && c <= LOWER_CASE_END) || (c >= UPPER_CASE_START && c <= UPPER_CASE_END)){
            return LETTER_NAME;
        } else if(Character.isDigit(c))return DIGIT_NAME;
        else if(c == UNDER_SCORE) return UNDER_SCORE_NAME;
        return null;
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

    private boolean isValidKeyword(IdKeywordTokenType tokenType){
        return (tokenType != null && tokenType != IdKeywordTokenType.ID);
    }

    @Override
    public String getInvalidTokenType(String lexeme) {
        return INVALID_ID_MESSAGE;
    }
}
