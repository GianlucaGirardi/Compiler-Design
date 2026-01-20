package org.example.lexicalAnalyzer.config;

import lombok.AllArgsConstructor;
import org.example.lexicalAnalyzer.dispatcher.*;

@AllArgsConstructor
public enum CharType {

    LOWER_CASE_LETTER(new IdDispatcher()),
    NUMBER(new NumberDispatcher()),
    PUNCTUATION_OPERATION(new PuncOperationDispatcher()),
    COMMENT(new CommentDispatcher()),
    INVALID_TYPE(null);

    private final Dispatcher dispatcher;

    Dispatcher getDispatcher(){
        return this.dispatcher;
    }
}
