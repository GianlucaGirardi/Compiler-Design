package org.example.lexicalAnalyzer;

import org.example.lexicalAnalyzer.config.CharType;
import org.example.lexicalAnalyzer.config.LexerConfig;
import org.example.lexicalAnalyzer.dispatcher.Dispatcher;
import org.example.lexicalAnalyzer.token.Token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DispatcherSelectionTest {

    private CharStream stream;
    private LexerConfig config;

    private Dispatcher idDispatcher;
    private Dispatcher numberDispatcher;
    private Dispatcher commentDispatcher;
    private Dispatcher puncOperationDispatcher;

    private Map<CharType, Dispatcher> map;

    @BeforeEach
    void setUp() {
        stream = mock(CharStream.class);
        config = mock(LexerConfig.class);

        idDispatcher = mock(Dispatcher.class);
        numberDispatcher = mock(Dispatcher.class);
        commentDispatcher = mock(Dispatcher.class);
        puncOperationDispatcher = mock(Dispatcher.class);

        map = new EnumMap<>(CharType.class);
        map.put(CharType.LETTER, idDispatcher);
        map.put(CharType.NUMBER, numberDispatcher);
        map.put(CharType.COMMENT, commentDispatcher);
        map.put(CharType.PUNCTUATION_OPERATION, puncOperationDispatcher);
        map.put(CharType.INVALID_TYPE, null);

        lenient().when(config.getEnumDispatcherMap()).thenReturn(map);

        when(stream.isAtEnd()).thenReturn(false);
    }

    @Test
    void callsIdDispatcher_whenClassifyReturnsLETTER() {
        when(stream.peek()).thenReturn('a');
        when(stream.peekNext()).thenReturn('b');
        when(config.classify('a', 'b')).thenReturn(CharType.LETTER);
        LexicalAnalyzer lexer = new LexicalAnalyzer(config, stream);

        Token result = lexer.nextToken();

        verify(idDispatcher, times(1)).processToken(stream);
        verify(numberDispatcher, never()).processToken(any());
        verify(commentDispatcher, never()).processToken(any());
        verify(puncOperationDispatcher, never()).processToken(any());
    }

    @Test
    void callsNumberDispatcher_whenClassifyReturnsNumber() {
        when(stream.peek()).thenReturn('1');
        when(stream.peekNext()).thenReturn('2');
        when(config.classify('1', '2')).thenReturn(CharType.NUMBER);
        LexicalAnalyzer lexer = new LexicalAnalyzer(config, stream);

        Token result = lexer.nextToken();

        verify(idDispatcher, never()).processToken(any());
        verify(numberDispatcher, times(1)).processToken(stream);
        verify(commentDispatcher, never()).processToken(any());
        verify(puncOperationDispatcher, never()).processToken(any());
    }

    @Test
    void callsCommentDispatcher_whenClassifyReturnsComment() {
        /* Same for multiline comment */
        when(stream.peek()).thenReturn('/');
        when(stream.peekNext()).thenReturn('/');
        when(config.classify('/', '/')).thenReturn(CharType.COMMENT);
        LexicalAnalyzer lexer = new LexicalAnalyzer(config, stream);

        Token result = lexer.nextToken();

        verify(idDispatcher, never()).processToken(any());
        verify(numberDispatcher, never()).processToken(any());
        verify(commentDispatcher, times(1)).processToken(stream);
        verify(puncOperationDispatcher, never()).processToken(any());
    }

    @Test
    void callsPuncOperationDispatcher_whenClassifyReturnsPunctuationOperation() {
        /* Same for multiline comment */
        when(stream.peek()).thenReturn('<');
        when(stream.peekNext()).thenReturn('=');
        when(config.classify('<', '=')).thenReturn(CharType.PUNCTUATION_OPERATION);
        LexicalAnalyzer lexer = new LexicalAnalyzer(config, stream);

        Token result = lexer.nextToken();

        verify(idDispatcher, never()).processToken(any());
        verify(numberDispatcher, never()).processToken(any());
        verify(commentDispatcher, never()).processToken(any());
        verify(puncOperationDispatcher, times(1)).processToken(stream);
    }

    @Test
    void callsNoDispatcher_whenClassifyReturnsInvalid() {
        /* Same for multiline comment */
        when(stream.peek()).thenReturn('@');
        when(stream.peekNext()).thenReturn('#');
        when(config.classify('@', '#')).thenReturn(CharType.INVALID_TYPE);
        LexicalAnalyzer lexer = new LexicalAnalyzer(config, stream);

        Token result = lexer.nextToken();

        verify(idDispatcher, never()).processToken(any());
        verify(numberDispatcher, never()).processToken(any());
        verify(commentDispatcher, never()).processToken(any());
        verify(puncOperationDispatcher, never()).processToken(any());
    }

}
