package org.example.lexicalAnalyzer.dispatcher;

import org.example.lexicalAnalyzer.CharStream;
import org.example.lexicalAnalyzer.token.Token;

public class IdDispatcher implements Dispatcher{
    @Override
    public Token processToken(CharStream stream) {
        return null;
    }

    @Override
    public boolean isDelimiter(char c) {
        return false;
    }

    @Override
    public String getInvalidTokenType(String lexeme) {
        return "";
    }
}
