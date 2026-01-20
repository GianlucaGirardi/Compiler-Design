package org.example;

import org.example.lexicalAnalyzer.CharStream;
import org.example.lexicalAnalyzer.LexicalAnalyzer;
import org.example.lexicalAnalyzer.config.LexerConfig;
import org.example.lexicalAnalyzer.token.Token;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main( String[] args ) {
        CharStream stream;
        List<Token> tokens = new ArrayList<>();

        try{
            stream = new CharStream(Path.of("./lexpositivegrading.src"));
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
