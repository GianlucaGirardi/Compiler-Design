package org.example.lexicalAnalyzer.dispatcher;

public record ScanResult(

        String lexeme,

        String endState,

        boolean forcedInvalid
) {
}
