package org.example.parser;

import org.example.lexicalAnalyzer.token.Token;

public interface TokenStream {

    Token peek();

    Token consume();

    boolean isAtEnd();

}
