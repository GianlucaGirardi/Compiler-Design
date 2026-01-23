package org.example.lexicalAnalyzer.config;

import lombok.AllArgsConstructor;
import org.example.lexicalAnalyzer.dispatcher.*;

import static org.example.lexicalAnalyzer.config.Constants.*;

@AllArgsConstructor
public enum CharType {

    LETTER(new IdDispatcher(ID_DFA_NAME)),

    NUMBER(new NumberDispatcher(NUMBER_DFA_NAME)),

    PUNCTUATION_OPERATION(new PuncOperationDispatcher()),

    COMMENT(new CommentDispatcher(COMMENT_DFA_NAME)),

    INVALID_TYPE(null);

    private final Dispatcher dispatcher;

    Dispatcher getDispatcher(){
        return this.dispatcher;
    }
}
