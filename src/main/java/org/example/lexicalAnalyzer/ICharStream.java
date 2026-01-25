package org.example.lexicalAnalyzer;

public interface ICharStream {

    boolean isAtEnd();

    char peek();

    char peekNext();

    void advance();

    int getLine();

}
