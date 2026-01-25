package org.example.lexicalAnalyzer.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.example.lexicalAnalyzer.config.Constants.*;
import static org.example.lexicalAnalyzer.token.Constants.*;

@Getter
@AllArgsConstructor
public enum PuncOperationTokenType {
    EQ_OP(NAME_EQ, String.valueOf(EQUALS), OPERATOR),
    EQ_EQ_OP(NAME_EQ_EQ, EQUALS_OP, OPERATOR),
    PLUS_OP(NAME_PLUS, String.valueOf(PLUS), OPERATOR),
    MINUS_OP(NAME_MINUS, String.valueOf(MINUS), OPERATOR),
    MULT_OP(NAME_MULT, String.valueOf(STAR), OPERATOR),
    DIV_OP(NAME_DIV, String.valueOf(SLASH), OPERATOR),
    L_THAN_OP(NAME_LTHAN, String.valueOf(LESS), OPERATOR),
    GTHAN_OP(NAME_GTHAN, String.valueOf(GREATER), OPERATOR),
    LEQ_OP(NAME_LEQ, LESS_THAN_OP, OPERATOR),
    GEQ_OP(NAME_GEQ, GREATER_THAN_OP, OPERATOR),
    NEQ_OP(NAME_NEQ, NOT_EQUAL, OPERATOR),

    LPAR_PUNC(NAME_LPAR, String.valueOf(LPAREN), PUNCTUATION),
    RPAR_PUNC(NAME_RPAR, String.valueOf(RPAREN), PUNCTUATION),
    LBRACE_PUNC(NAME_LBRACE,String.valueOf(LBRACE), PUNCTUATION),
    RBRACE_PUNC(NAME_RBRACE, String.valueOf(RBRACE), PUNCTUATION),
    LBRACKET_PUNC(NAME_LBRACKET, String.valueOf(LBRACKET), PUNCTUATION),
    RBRACKET_PUNC(NAME_RBRACKET, String.valueOf(RBRACKET), PUNCTUATION),
    SEMI_PUNC(NAME_SEMI, String.valueOf(SEMICOLON), PUNCTUATION),
    COLON_PUNC(NAME_COLON, String.valueOf(COLON), PUNCTUATION),
    COLON_COLON_PUNC(NAME_COLON_COLON, COLON_COLON, PUNCTUATION),
    COMMA_PUNC(NAME_COMMA, String.valueOf(COMMA), PUNCTUATION),
    DOT_PUNC(NAME_DOT, String.valueOf(DOT), PUNCTUATION);

    private final String name;

    private final String lexeme;

    private final String type;

}
