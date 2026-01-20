package org.example.lexicalAnalyzer.dispatcher;

import org.example.lexicalAnalyzer.CharStream;
import org.example.lexicalAnalyzer.token.Token;

public class NumberDispatcher implements Dispatcher{

    @Override
    public Token processToken(CharStream stream) {
        System.out.println("Number");
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
