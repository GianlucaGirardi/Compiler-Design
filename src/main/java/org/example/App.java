package org.example;

import org.example.lexicalAnalyzer.*;
import org.example.lexicalAnalyzer.config.DfaRegistry;

import java.io.IOException;
import java.nio.file.Path;

public class App {
    public static void main( String[] args ) {

        if (args.length != 1) {
            System.err.println("Usage: java App <input-file>");
            System.exit(1);
        }

        DfaRegistry.init();
        LexerRunnerResult lexicalAnalyzerResult;

        try {
            lexicalAnalyzerResult = LexerRunner.run(Path.of(args[0]));

            /* To be removed in A2 */
            LexerOutputWriter.writeAll(lexicalAnalyzerResult);
        } catch (IOException e) {
            System.err.println("Could not read input file: " + e.getMessage());
            System.exit(1);
        }


    }
}
