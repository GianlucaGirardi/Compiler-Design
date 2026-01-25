package org.example.lexicalAnalyzer;

import org.example.lexicalAnalyzer.config.DfaRegistry;
import org.example.lexicalAnalyzer.config.LexerConfig;
import org.example.lexicalAnalyzer.token.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.example.lexicalAnalyzer.config.Constants.INLINE_COMMENT_NAME;
import static org.example.lexicalAnalyzer.config.Constants.MULTI_LINE_COMMENT_NAME;
import static org.junit.jupiter.api.Assertions.*;

public class TokenOutputTest {

    private LexerConfig config;

    private static final String INTERGER = "INTEGER";
    private static final String FLOAT = "FLOAT";

    @BeforeEach
    void setUp() {
        DfaRegistry.init();
        config = new LexerConfig();
    }


    private Token lex(String input) {
        ICharStream stream = new TestCharStream(input);
        LexicalAnalyzer lexer = new LexicalAnalyzer(config, stream);
        return lexer.nextToken();
    }

    private void assertToken(Token token, String type, String lexeme) {
        assertNotNull(token);
        assertEquals(type, token.getType());
        assertEquals(lexeme, token.getLexeme());
        assertEquals(1, token.getLine());
        assertTrue(token.isValid());
    }

    private LexicalAnalyzer lexAll(String input) {
        return new LexicalAnalyzer(new LexerConfig(), new TestCharStream(input));
    }


    @Test
    void validPuncOperationTokenTest2() {
        String lexeme = ";";
        Token token = lex(lexeme);
        assertToken(token, "semi", lexeme);
    }

    @Test
    void validPuncOperationTokenTest() {
        String lexeme = "<>";
        Token token = lex(lexeme);
        assertToken(token, "noteq", lexeme);
    }

    @Test
    void validIdTokenTest() {
        String lexeme = "a1c_1z2p_3";
        Token token = lex(lexeme);
        assertToken(token, "id", lexeme);
    }

    @Test
    void valIntegerIdTokenTest() {
        String lexeme = "1234567890";
        Token token = lex(lexeme);
        assertToken(token, INTERGER, lexeme);
    }

    @Test
    void valFloatTokenTest1() {
        String lexeme = "123.4500670809";
        Token token = lex(lexeme);
        assertToken(token, FLOAT, lexeme);
    }

    @Test
    void valFloatTokenTest2() {
        String lexeme = "123.0";
        Token token = lex(lexeme);
        assertToken(token, FLOAT, lexeme);
    }

    @Test
    void valFloatTokenTest3() {
        String lexeme = "123.0e150";
        Token token = lex(lexeme);
        assertToken(token, FLOAT, lexeme);
    }

    @Test
    void valFloatTokenTest4() {
        String lexeme = "123.0e+150";
        Token token = lex(lexeme);
        assertToken(token, FLOAT, lexeme);
    }

    @Test
    void valFloatTokenTest5() {
        String lexeme = "123.500509e-0";
        Token token = lex(lexeme);
        assertToken(token, FLOAT, lexeme);
    }


    @Test
    void valMultilineCommentTokenTest() {
        String lexeme = "/* \\r\n*\\rThis!@!qdsfs'f]sIs_a\\n\\r%$&%Comment///\t/*\n/* */";
        Token token = lex(lexeme);
        assertToken(token, MULTI_LINE_COMMENT_NAME, lexeme);
    }

    @Test
    void valInlineCommentTokenTest() {
        String lexeme = "//*/ This is an inline comment*/*///* ";
        Token token = lex(lexeme);
        assertToken(token,INLINE_COMMENT_NAME, lexeme);
    }

    @Test
    void valKeywordTokenTest() {
        String lexeme = "inherits";
        Token token = lex(lexeme);
        System.out.println(token);
        assertToken(token, lexeme, lexeme);
    }

    @Test
    void valExpression() {
        LexicalAnalyzer lexer = lexAll("i=i+1");

        assertToken(lexer.nextToken(), "id", "i");
        assertToken(lexer.nextToken(), "assign", "=");
        assertToken(lexer.nextToken(), "id", "i");
        assertToken(lexer.nextToken(), "plus", "+");
        assertToken(lexer.nextToken(), INTERGER, "1");
    }


}
