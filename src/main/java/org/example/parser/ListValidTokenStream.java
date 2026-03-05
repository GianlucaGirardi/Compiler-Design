package org.example.parser;

import lombok.RequiredArgsConstructor;
import org.example.lexicalAnalyzer.token.Token;

import java.util.List;

@RequiredArgsConstructor
public class ListValidTokenStream  implements TokenStream{
    private final List<Token> tokens;
    private int position = 0;

    @Override
    public Token peek() {
        return tokens.get(position >= tokens.size() ? tokens.size() - 1 : position);
    }

    @Override
    public Token consume() {
        Token current = peek();
        position++;
        return current;
    }

    @Override
    public boolean isAtEnd() {
        return peek().getType().equals("eof");
    }
}
