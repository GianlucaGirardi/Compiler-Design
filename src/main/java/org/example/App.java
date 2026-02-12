package org.example;

import org.example.lexicalAnalyzer.*;
import org.example.lexicalAnalyzer.config.DfaRegistry;

import java.io.IOException;
import java.nio.file.Path;

public class App {
    public static void main( String[] args ) {

        if (args.length != 1) {
            System.err.println("Usage: lexdriver <input-file.src>");
            System.exit(1);
        }

        DfaRegistry.init();
        LexerRunnerResult lexicalAnalyzerResult;

        try {
            lexicalAnalyzerResult = LexerRunner.run(Path.of(args[0]));

            /* Uncomment to visualize A1 output */
//            LexerOutputWriter.writeAll(lexicalAnalyzerResult);
        } catch (IOException e) {
            System.err.println("Could not read input file: " + e.getMessage());
            System.exit(1);
        }


    }
}
