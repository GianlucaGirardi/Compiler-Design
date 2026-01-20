package org.example.lexicalAnalyzer.token;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String NAME_EQ = "assign";
    public static final String NAME_EQ_EQ = "eq";
    public static final String NAME_PLUS = "plus";
    public static final String NAME_MINUS = "minus";
    public static final String NAME_MULT = "mult";
    public static final String NAME_DIV = "div";

    public static final String NAME_LTHAN = "lt";
    public static final String NAME_GTHAN = "gt";
    public static final String NAME_LEQ = "leq";
    public static final String NAME_GEQ = "geq";
    public static final String NAME_NEQ = "noteq";

    public static final String NAME_LPAR = "openpar";
    public static final String NAME_RPAR = "closepar";

    public static final String NAME_LBRACE = "opencubr";
    public static final String NAME_RBRACE = "closecubr";

    public static final String NAME_LBRACKET = "opensqbr";
    public static final String NAME_RBRACKET = "closesqrbr";

    public static final String NAME_SEMI = "semi";
    public static final String NAME_COLON = "colon";
    public static final String NAME_COLON_COLON = "coloncolon";
    public static final String NAME_COMMA = "comma";

    public static String OPERATOR = "operator";
    public static String PUNCTUATION = "punctuation";
}
