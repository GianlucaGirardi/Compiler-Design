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
                ? new Token(tokenType.getName(), lexeme, line)
                : new Token(getInvalidTokenType(lexeme), lexeme, line);
    }

    @Override
    public String getInvalidTokenType(String lexeme){
        StringBuilder sbBactrack = new StringBuilder(lexeme);

        while (sbBactrack.length() > 0) {
            sbBactrack.deleteCharAt(sbBactrack.length() - 1);
            PuncOperationTokenType tokenType = puncOperationTokenTypeMap.get(sbBactrack.toString());
            if (tokenType != null) return tokenType.getType();
        }

        return "INVALID_OP_PUNC"; // TO DO: Replace with Constant
    }

    public boolean isDelimiter(char c) {
        return c == WHITE_SPACE || c == TAB || c == NEXT_LINE || c == CARRIAGE_RETURN;
    }
}
