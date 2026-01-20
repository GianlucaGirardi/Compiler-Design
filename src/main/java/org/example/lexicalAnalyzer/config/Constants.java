package org.example.lexicalAnalyzer.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static char LOWER_CASE_START = 'a';
    public static char LOWER_CASE_END = 'z';

    public static char SLASH = '/';
    public static char STAR = '*';
    public static final char EQUALS = '=';
    public static final char PLUS = '+';
    public static final char MINUS = '-';
    public static final char GREATER = '>';
    public static final char LESS = '<';

    public static final char DOT = '.';
    public static final char COLON = ':';
    public static final char LPAREN = '(';
    public static final char RPAREN = ')';
    public static final char LBRACE = '{';
    public static final char RBRACE = '}';
    public static final char LBRACKET = '[';
    public static final char RBRACKET = ']';
    public static final char SEMICOLON = ';';
    public static final char COMMA = ',';

    public static final char WHITE_SPACE = ' ';
    public static final char NEXT_LINE = '\n';
    public static final char CARRIAGE_RETURN = '\r';
    public static final char TAB = '\t';

    public static final String EQUALS_OP = "==";
    public static final String LESS_THAN_OP = "<=";
    public static final String GREATER_THAN_OP = ">=";
    public static final String NOT_EQUAL = "<>";

    public static final String COLON_COLON = "::";

}
