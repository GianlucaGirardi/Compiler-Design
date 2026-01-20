package org.example.lexicalAnalyzer.dispatcher;

import org.example.lexicalAnalyzer.CharStream;
import org.example.lexicalAnalyzer.token.IdKeywordTokenType;
import org.example.lexicalAnalyzer.token.Token;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.example.lexicalAnalyzer.config.Constants.*;
import static org.example.lexicalAnalyzer.config.Constants.CARRIAGE_RETURN;

public class IdDispatcher implements Dispatcher{
    private final Map<String, IdKeywordTokenType> idKeywordTokenTypeMap;
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9_]*$");


    public IdDispatcher(){
        idKeywordTokenTypeMap = new HashMap<>();
        Arrays.stream(IdKeywordTokenType.values()).filter(token -> token.getLexeme() != null)
                .forEach((token ->
                idKeywordTokenTypeMap.put(token.getLexeme(), token)));
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
        IdKeywordTokenType tokenType = idKeywordTokenTypeMap.get(lexeme);

        if(isValidKeyword(tokenType))return new Token(tokenType.getName(), lexeme, line);

        if(isValidId(lexeme)) return new Token(IdKeywordTokenType.ID.getName(), lexeme, line);

        return new Token("Invalid" + getInvalidTokenType(lexeme), lexeme, line); // TO DO: Invalidtype
    }

    private boolean isValidKeyword(IdKeywordTokenType tokenType){
        return (tokenType != null && tokenType != IdKeywordTokenType.ID);
    }

    private boolean isValidId(String lexeme){
        return ID_PATTERN.matcher(lexeme).matches();
    }

    @Override
    public boolean isDelimiter(char c) {
        return c == WHITE_SPACE || c == TAB || c == NEXT_LINE || c == CARRIAGE_RETURN;
    }

    @Override
    public String getInvalidTokenType(String lexeme) {
        StringBuilder sbBacktrack = new StringBuilder(lexeme);

        while (sbBacktrack.length() > 0) {
            sbBacktrack.deleteCharAt(sbBacktrack.length() - 1);
            IdKeywordTokenType tokenType = idKeywordTokenTypeMap.get(sbBacktrack.toString());

            if (isValidKeyword(tokenType)) return tokenType.getType();

            if(isValidId(sbBacktrack.toString())) return IdKeywordTokenType.ID.getType();

        }
        return "ID_KEYWORD"; // TO DO: Replace with Constant
    }
}
