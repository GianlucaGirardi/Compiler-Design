package org.example.lexicalAnalyzer.dispatcher;

import org.example.lexicalAnalyzer.CharStream;
import org.example.lexicalAnalyzer.token.Token;

import java.util.Map;

public interface Dispatcher {

   Token processToken(CharStream stream);

   boolean isDelimiter(char c);

   String getInvalidTokenType(String lexeme);
}
