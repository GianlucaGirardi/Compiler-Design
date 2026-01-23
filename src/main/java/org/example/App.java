package org.example;

import org.example.lexicalAnalyzer.CharStream;
import org.example.lexicalAnalyzer.LexicalAnalyzer;
import org.example.lexicalAnalyzer.config.DfaRegistry;
import org.example.lexicalAnalyzer.config.LexerConfig;
import org.example.lexicalAnalyzer.token.Token;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main( String[] args ) {
        DfaRegistry.init();
        CharStream stream;
        List<Token> tokens = new ArrayList<>();

        if(args.length != 1){
            System.err.println("Usage: java Main <input-file>");
            System.exit(1);
        }

        try{
            stream = new CharStream(Path.of(args[0]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(new LexerConfig(), stream);

        while(true){
            Token token = lexicalAnalyzer.nextToken();
            /* Skip invalid token */
            if(token == null) continue;
            if(token.getType().equals("EOF")){ //replace with == TokenType.EOF
                break;
            }
            System.out.println(token);
            tokens.add(token);
        }

    }
}
