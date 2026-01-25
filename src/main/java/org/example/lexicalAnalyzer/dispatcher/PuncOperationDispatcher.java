package org.example.lexicalAnalyzer.dispatcher;

import org.example.lexicalAnalyzer.ICharStream;
import org.example.lexicalAnalyzer.token.PuncOperationTokenType;
import org.example.lexicalAnalyzer.token.Token;

import java.util.*;

import static org.example.lexicalAnalyzer.config.Constants.*;

public class PuncOperationDispatcher implements Dispatcher{

    private final Map<String, PuncOperationTokenType> puncOperationTokenTypeMap;

    public PuncOperationDispatcher(){
        puncOperationTokenTypeMap = new HashMap<>();
        Arrays.stream(PuncOperationTokenType.values()).forEach((token ->
            puncOperationTokenTypeMap.put(token.getLexeme(), token)));
    }

    @Override
    public Token processToken(ICharStream stream) {
        int line = stream.getLine();

        if (stream.isAtEnd()) return null;

        char c1 = stream.peek();
        if (Character.isWhitespace(c1)) return null;

        String one = String.valueOf(c1);

        char c2 = stream.peekNext();
        String two = (c2 == '\0') ? null : ("" + c1 + c2);

        /* handle potential op size of two */
        if (two != null) {
            PuncOperationTokenType tokenType2 = puncOperationTokenTypeMap.get(two);
            if (tokenType2 != null) {
                stream.advance();
                stream.advance();
                return new Token(tokenType2.getName(), two, line, true);
            }
        }

        /* default back to op size one */
        PuncOperationTokenType tokenType1 = puncOperationTokenTypeMap.get(one);
        if (tokenType1 != null) {
            stream.advance();
            return new Token(tokenType1.getName(), one, line, true);
        }

        stream.advance();
        return new Token(INVALID_MESSAGE_PREFIX + getInvalidTokenType(one), one, line, false);
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
