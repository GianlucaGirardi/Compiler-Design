package org.example.lexicalAnalyzer.config;

import lombok.Data;
import org.example.lexicalAnalyzer.dispatcher.Dispatcher;

import java.util.*;

import static org.example.lexicalAnalyzer.config.Constants.*;

@Data
public class LexerConfig {

    private static final Set<Character> PuncOperationSet = Set.of(
            EQUALS, PLUS, MINUS, STAR, SLASH, GREATER, LESS, COLON, LPAREN, RPAREN,
            LBRACE, RBRACE, LBRACKET, RBRACKET, SEMICOLON, COMMA, DOT);

    private Map<CharType, Dispatcher> enumDispatcherMap;

    public LexerConfig(){
        enumDispatcherMap = new HashMap<>();
        Arrays.stream(CharType.values()).forEach(charType ->
                enumDispatcherMap.put(charType, charType.getDispatcher()));
    }

    /* Dispatcher helper */
    public CharType classify(char c1, char c2){

        if(isLetter(c1)) return CharType.LETTER;

        else if(c1 == UNDER_SCORE && (isLetter(c2) || Character.isDigit(c2))) return CharType.LETTER;

        else if(Character.isDigit(c1)) return CharType.NUMBER;

        else if(c1 == SLASH && (c2 == SLASH || c2 == STAR)) return CharType.COMMENT;

        else if(PuncOperationSet.contains(c1)) return CharType.PUNCTUATION_OPERATION;

        return CharType.INVALID_TYPE;
    }

    private boolean isLetter(char c){
        return (c >= LOWER_CASE_START && c <= LOWER_CASE_END)
                || (c >= UPPER_CASE_START && c <= UPPER_CASE_END);
    }
}
