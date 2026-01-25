package org.example.lexicalAnalyzer;

import org.example.lexicalAnalyzer.token.Token;

import java.nio.file.Path;
import java.util.List;

public record LexerRunnerResult(

        List<Token> validTokens,

        List<Token> errorTokens,

        List<Token> commentTokens,

        Path inputFile
) {
}
