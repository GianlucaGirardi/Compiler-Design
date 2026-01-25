package org.example.lexicalAnalyzer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.example.lexicalAnalyzer.config.CharType;
import org.example.lexicalAnalyzer.config.LexerConfig;

import org.example.lexicalAnalyzer.token.Token;

import static org.example.lexicalAnalyzer.config.Constants.EOF_MARKER;
import static org.example.lexicalAnalyzer.config.Constants.INVALID_CHAR_MESSAGE;


@Data
@ToString
@RequiredArgsConstructor
public class LexicalAnalyzer {
    private final LexerConfig lexerConfig;

    private final ICharStream stream;

    public Token nextToken(){
        advanceToNextToken();

        if(stream.isAtEnd()) return new Token(EOF_MARKER, EOF_MARKER, stream.getLine(), false);

        CharType type =  getCurrentCharType();

        if(type == CharType.INVALID_TYPE){
            String invalidCharInput = String.valueOf(stream.peek());
            stream.advance();
            return new Token(INVALID_CHAR_MESSAGE, invalidCharInput, stream.getLine(), false);
        }

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
