package org.example.lexicalAnalyzer.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String DFA_INDEX_PATH = "dfa/dfa.index";
    public static final String DFA_RESOURCE_PATH = "dfa/";
    public static final String INDEX_FILE_COMMENT= "#";
    public static final String EOF_MARKER = "EOF";
    public static final String EOF_MARKER_TYPE = "eof";

    public static final char ZERO_DIGIT = '0';
    public static final char NINE_DIGIT = '9';
    public static final String DIGIT_NAME = "DIGIT";
    public static final String ZERO_DIGIT_NAME = "ZERO_DIGIT";
    public static final String NONZERO_DIGIT_NAME = "NONZERO_DIGIT";

    public static final char LOWER_CASE_START = 'a';
    public static final char LOWER_CASE_END = 'z';
    public static final char LOWER_CASE_E = 'e';
    public static final String LOWER_CASE_E_NAME = "E_LETTER";
    public static final char UPPER_CASE_START = 'A';
    public static final char UPPER_CASE_END = 'Z';
    public static final String LETTER_NAME = "LETTER";

    public static final char SLASH = '/';
    public static final String SLASH_NAME = "SLASH";

    public static final String STAR_NAME = "STAR";
    public static final char STAR = '*';

    public static final char EQUALS = '=';
    public static final String PLUS_NAME = "PLUS";
    public static final char PLUS = '+';
    public static final String MINUS_NAME = "MINUS";
    public static final char MINUS = '-';
    public static final char GREATER = '>';
    public static final char LESS = '<';
    public static final char UNDER_SCORE = '_';
    public static final String UNDER_SCORE_NAME = "UNDERSCORE";

    public static final String DOT_NAME = "DOT";
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

    public static final String EQUALS_OP = "==";
    public static final String LESS_THAN_OP = "<=";
    public static final String GREATER_THAN_OP = ">=";
    public static final String NOT_EQUAL = "<>";

    public static final String COLON_COLON = "::";

    public static final String COMMENT_NAME = "COMMENT";
    public static final String INLINE_COMMENT_NAME = "INLINE_COMMENT";
    public static final String MULTI_LINE_COMMENT_NAME = "MULTI_LINE_COMMENT";
    public static final String DEFAULT_NAME = "DEFAULT";

    public static final String NUMBER_DFA_NAME = "NumDfa";
    public static final String ID_DFA_NAME = "IdDfa";
    public static final String COMMENT_DFA_NAME = "CommentDfa";

    public static final String INVALID_MESSAGE_PREFIX = "Invalid";
    public static final String INVALID_NUMBER_MESSAGE = "invalidnum";
    public static final String INVALID_CHAR_MESSAGE = "invalidchar";
    public static final String INVALID_ID_MESSAGE = "invalidid";
    public static final String INVALID_PUNCOP_MESSAGE = "invalidpuncop";
    public static final String INVALID_COMMENT_MESSAGE = "invalidcomment";
}
