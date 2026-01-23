package org.example.lexicalAnalyzer.dispatcher;

public record NumberScanResult(
        String lexeme,

        String endState,

        boolean forcedInvalid
) {
}
