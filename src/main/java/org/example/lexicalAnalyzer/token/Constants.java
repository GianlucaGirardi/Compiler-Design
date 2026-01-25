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
    public static final String NAME_DOT = "dot";

    public static final String NAME_ID = "id";

    public static final String NAME_IF = "if";
    public static final String NAME_THEN = "then";
    public static final String NAME_ELSE = "else";
    public static final String NAME_WHILE = "while";
    public static final String NAME_CLASS = "class";
    public static final String NAME_INT = "integer";
    public static final String NAME_FLOAT = "float";
    public static final String NAME_DO = "do";
    public static final String NAME_END = "end";
    public static final String NAME_PUB = "public";
    public static final String NAME_PRIVATE = "private";
    public static final String NAME_OR = "or";
    public static final String NAME_AND = "and";
    public static final String NAME_NOT = "not";
    public static final String NAME_READ = "read";
    public static final String NAME_WRITE = "write";
    public static final String NAME_RETURN = "return";
    public static final String NAME_INH = "inherits";
    public static final String NAME_LOC = "local";
    public static final String NAME_VOID = "void";
    public static final String NAME_MAIN = "main";

    public static final String OPERATOR = "operator";
    public static final String PUNCTUATION = "punctuation";

    public static final String IDENTIFIER = "id";
    public static final String KEYWORD = "keyword";
}
