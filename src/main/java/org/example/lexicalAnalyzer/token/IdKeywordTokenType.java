package org.example.lexicalAnalyzer.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.example.lexicalAnalyzer.token.Constants.*;

@Getter
@AllArgsConstructor
public enum IdKeywordTokenType {
    ID(NAME_ID, null, IDENTIFIER),

    IF(NAME_IF, NAME_IF, KEYWORD),
    THEN(NAME_THEN, NAME_THEN, KEYWORD),
    ELSE(NAME_ELSE, NAME_ELSE, KEYWORD),
    WHILE(NAME_WHILE, NAME_WHILE, KEYWORD),
    CLASS(NAME_CLASS, NAME_CLASS, KEYWORD),
    INT(NAME_INT, NAME_INT, KEYWORD),
    FLOAT(NAME_FLOAT, NAME_FLOAT, KEYWORD),
    DO(NAME_DO, NAME_DO, KEYWORD),
    END(NAME_END, NAME_END, KEYWORD),
    PUB(NAME_PUB, NAME_PUB, KEYWORD),
    PRIVATE(NAME_PRIVATE, NAME_PRIVATE, KEYWORD),
    OR(NAME_OR, NAME_OR, KEYWORD),
    AND(NAME_AND, NAME_AND, KEYWORD),
    NOT(NAME_NOT, NAME_NOT, KEYWORD),
    READ(NAME_READ, NAME_READ, KEYWORD),
    WRITE(NAME_WRITE, NAME_WRITE, KEYWORD),
    RETURN(NAME_RETURN, NAME_RETURN, KEYWORD),
    INH(NAME_INH, NAME_INH, KEYWORD),
    LOC(NAME_LOC, NAME_LOC, KEYWORD),
    VOID(NAME_VOID, NAME_VOID, KEYWORD),
    MAIN(NAME_MAIN, NAME_MAIN, KEYWORD);

    private final String name;

    private final String lexeme;

    private final String type;
}
