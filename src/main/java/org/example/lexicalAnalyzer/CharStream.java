package org.example.lexicalAnalyzer;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Data
public final class CharStream {
    private final BufferedReader reader;
    private int curr;
    private int next;
    private int line;

    public CharStream(Path path) throws IOException {
        this.reader = Files.newBufferedReader(path);
        this.curr = reader.read();
        this.next = reader.read();
        this.line = 1;
    }

    public char peek() {
        return curr == -1 ? '\0' : (char) curr;
    }

    public char peekNext() {
        return next == -1 ? '\0' : (char) next;
    }

    public boolean isAtEnd() {
        return curr == -1;
    }

    /*
    Assumes Windows CRLF line endings (\r\n).
    If not,  line count won't increment.
    Might need to adapt
    */
    public void advance() {
        try {
            char c = peek();

            if (c == '\r') {
                curr = next;
                next = reader.read();

                if (peek() == '\n') {
                    curr = next;
                    next = reader.read();
                }
                line++;
            }

            curr = next;
            next = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

