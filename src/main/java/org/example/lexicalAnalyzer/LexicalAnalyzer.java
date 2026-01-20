package org.example.lexicalAnalyzer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.example.lexicalAnalyzer.config.CharType;
import org.example.lexicalAnalyzer.config.LexerConfig;

import org.example.lexicalAnalyzer.token.Token;


@Data
@ToString
@RequiredArgsConstructor
public class LexicalAnalyzer {
    private final LexerConfig lexerConfig;

    private final CharStream stream;

    public Token nextToken(){
        advanceToNextToken();

        if(stream.isAtEnd()) return new Token("EOF", "EOF", stream.getLine()); /* TO DO: Replace temp Handling EOF */

        CharType type =  getCurrentCharType();
        /* Handle Invalid Char type where dispatcher is null - all dispatcher null cases are handled */
        if(type == CharType.INVALID_TYPE) return null;

        return lexerConfig.getEnumDispatcherMap().get(type).processToken(stream);
    }

    private void advanceToNextToken(){
        while (!stream.isAtEnd() && Character.isWhitespace(stream.peek())) {
            stream.advance();
        }
    }

    private CharType getCurrentCharType(){
        return lexerConfig.classify(stream.peek(), stream.peekNext());
    }

}
