package org.example.lexicalAnalyzer.dispatcher;

import org.example.lexicalAnalyzer.CharStream;
import org.example.lexicalAnalyzer.token.PuncOperationTokenType;
import org.example.lexicalAnalyzer.token.Token;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.example.lexicalAnalyzer.config.Constants.*;

public class PuncOperationDispatcher implements Dispatcher{
    private final Map<String, PuncOperationTokenType> puncOperationTokenTypeMap;

    public PuncOperationDispatcher(){
        puncOperationTokenTypeMap = new HashMap<>();
        Arrays.stream(PuncOperationTokenType.values()).forEach((token ->
            puncOperationTokenTypeMap.put(token.getLexeme(), token)));
    }

    @Override
    public Token processToken(CharStream stream) {
        StringBuilder stringBuilder = new StringBuilder();
        int line = stream.getLine();

        while (!stream.isAtEnd() && !isDelimiter((char) stream.getCurr())) {
            stringBuilder.append((char) stream.getCurr());
            stream.advance();
        }

        String lexeme = stringBuilder.toString();
        PuncOperationTokenType tokenType = puncOperationTokenTypeMap.get(lexeme);

        return tokenType != null
                ? new Token(tokenType.getName(), lexeme, line, true)
                : new Token(INVALID_MESSAGE_PREFIX + getInvalidTokenType(lexeme), lexeme, line, false);
    }

    @Override
    public String getInvalidTokenType(String lexeme){
        StringBuilder sbBacktrack = new StringBuilder(lexeme);

        while (!sbBacktrack.isEmpty()) {
            sbBacktrack.deleteCharAt(sbBacktrack.length() - 1);
            PuncOperationTokenType tokenType = puncOperationTokenTypeMap.get(sbBacktrack.toString());
            if (tokenType != null) return tokenType.getType();
        }

        return INVALID_PUNCOP_MESSAGE;
    }

    public boolean isDelimiter(char c) {
        return Character.isWhitespace(c);
    }
}
