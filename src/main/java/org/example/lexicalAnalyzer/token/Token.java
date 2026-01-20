package org.example.lexicalAnalyzer.token;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Token {

    private final String type;

    private final String lexeme;

    private final Integer line;
    
}
