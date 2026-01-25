package org.example.lexicalAnalyzer;

class TestCharStream implements ICharStream {
    private final String s;
    private int i = 0;
    private int line = 1;

    TestCharStream(String s) {
        this.s = s;
    }

    @Override
    public boolean isAtEnd() {
        return i >= s.length();
    }

    @Override
    public char peek() {
        return isAtEnd() ? '\0' : s.charAt(i);
    }

    @Override public char peekNext() {
        return (i + 1 >= s.length()) ? '\0' : s.charAt(i + 1);
    }

    @Override public void advance() {
        if (!isAtEnd()) {
            if (s.charAt(i) == '\n') line++;
            i++;
        }
    }

    @Override public int getLine() { return line; }
}