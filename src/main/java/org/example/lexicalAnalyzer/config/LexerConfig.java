package org.example.lexicalAnalyzer.config;

import lombok.Data;
import org.example.lexicalAnalyzer.dispatcher.Dispatcher;

import java.util.*;

import static org.example.lexicalAnalyzer.config.Constants.*;

@Data
public class LexerConfig {

    private Set<Character> PuncOperationSet = Set.of(
            EQUALS, PLUS, MINUS, STAR, SLASH, GREATER, LESS, COLON, LPAREN, RPAREN,
            LBRACE, RBRACE, LBRACKET, RBRACKET, SEMICOLON, COMMA, DOT);

    private Map<CharType, Dispatcher> enumDispatcherMap;

    public LexerConfig(){
        enumDispatcherMap = new HashMap<>();
        Arrays.stream(CharType.values()).forEach(charType ->
                enumDispatcherMap.put(charType, charType.getDispatcher()));
    }

    /* Conditional selecting will only apply on first char vs whole token */
    public CharType classify(char c1, char c2){
        if(c1 > LOWER_CASE_START && c1 < LOWER_CASE_END) return CharType.LOWER_CASE_LETTER;
        else if(Character.isDigit(c1)) return CharType.NUMBER;
        else if(c1 == SLASH && (c2 == SLASH || c2 == STAR)) return CharType.COMMENT;
        else if(getPuncOperationSet().contains(c1)) return CharType.PUNCTUATION_OPERATION;
        return CharType.INVALID_TYPE;
    }
}
