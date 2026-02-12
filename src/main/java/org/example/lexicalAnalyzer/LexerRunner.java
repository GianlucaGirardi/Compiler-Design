package org.example.lexicalAnalyzer;

import org.example.lexicalAnalyzer.config.LexerConfig;
import org.example.lexicalAnalyzer.token.Token;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.example.lexicalAnalyzer.config.Constants.*;

public class LexerRunner {

    private static final List<Token> validTokens = new ArrayList<>();

    private static final List<Token> errorTokens = new ArrayList<>();;

    private static final List<Token> commentTokens = new ArrayList<>();;

    public static LexerRunnerResult run(Path inputFile) throws IOException {
        ICharStream stream = new CharStream(inputFile);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(new LexerConfig(), stream);


        while(true){
            Token token = lexicalAnalyzer.nextToken();

            if(token == null) continue;
            if(token.getType().equals(EOF_MARKER)){
                validTokens.add(new Token(EOF_MARKER_TYPE, EOF_MARKER, stream.getLine(), true));
                break;
            }

            handleToken(token);
        }

        return new LexerRunnerResult(validTokens, errorTokens, commentTokens, inputFile);
    }

    public static void handleToken(Token token){
        if(!token.isValid()){
            errorTokens.add(token);
        } else if(token.getType().equals(INLINE_COMMENT_NAME) || token.getType().equals(MULTI_LINE_COMMENT_NAME)){
            commentTokens.add(token);
        } else {
            validTokens.add(token);
        }
    }
}
