package org.example.lexicalAnalyzer.dispatcher;

import org.example.lexicalAnalyzer.ICharStream;
import org.example.lexicalAnalyzer.token.Token;

public interface Dispatcher {

   Token processToken(ICharStream stream);

   boolean isDelimiter(char c);

   String getInvalidTokenType(String lexeme);
}
