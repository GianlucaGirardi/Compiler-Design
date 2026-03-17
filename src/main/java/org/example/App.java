package org.example;

import org.example.lexicalAnalyzer.*;
import org.example.lexicalAnalyzer.config.DfaRegistry;
import org.example.parser.ATSDotWriter;
import org.example.parser.grammar.Grammar;
import org.example.parser.config.GrammarLoader;
import org.example.parser.ListValidTokenStream;
import org.example.parser.Parser;
import org.example.parser.config.ParseTableLoader;
import org.example.parser.node.ASTNode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.example.Constants.LL1_RESOURCE_PATH;

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

            ParseTableLoader.builder().build().loadFromResources(LL1_RESOURCE_PATH);
            String fileName = Path.of(args[0]).toString();
            Path outDerivation = Path.of(args[0]).resolveSibling(fileName.substring(0, fileName.lastIndexOf('.')) + ".outderivation");
            Path outSyntax = Path.of(args[0]).resolveSibling(fileName.substring(0, fileName.lastIndexOf('.')) + ".outsyntaxerrors");
            Path outAST = Path.of(args[0]).resolveSibling(fileName.substring(0, fileName.lastIndexOf('.')) + ".outast");
            GrammarLoader loader = new GrammarLoader();
            Grammar grammar = loader.loadFromResources("parser/ll1-grammar.yml", "START");


            try (BufferedWriter writer = Files.newBufferedWriter(outDerivation);
            BufferedWriter writer2 = Files.newBufferedWriter(outSyntax);
            BufferedWriter ASTDotWriter = Files.newBufferedWriter(outAST)
            ) {
                Parser parser = new Parser(ParseTableLoader.getTable(), new ListValidTokenStream(lexicalAnalyzerResult.validTokens()), grammar, writer, writer2);

                ASTNode astRoot = parser.parse();
                System.out.println(astRoot != null ? "PARSE OK" : "PARSE FAILED");
                if (astRoot != null) {
                    String dotRepresentation = new ATSDotWriter().export(astRoot);
                    ASTDotWriter.write(dotRepresentation);
                }

            }

            /* Uncomment to visualize A1 output */
            LexerOutputWriter.writeAll(lexicalAnalyzerResult);
        } catch (IOException e) {
            System.err.println("Could not read input file: " + e.getMessage());
            System.exit(1);
        }

    }


}
