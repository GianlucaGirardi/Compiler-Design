package org.example.lexicalAnalyzer;
import org.example.lexicalAnalyzer.token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class LexerOutputWriter {

    public static void writeAll(LexerRunnerResult result) throws IOException {
        String fileName = result.inputFile().getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);

        Path flaciOut = result.inputFile().resolveSibling(baseName + ".outlextokensflaci");
        Path tokensOut = result.inputFile().resolveSibling(baseName + ".outlextokens");
        Path errorsOut = result.inputFile().resolveSibling(baseName + ".outlexerrors");

        writeFlaciTokens(flaciOut, result);
        writeTokens(tokensOut, result);
        writeErrors(errorsOut, result);
    }

    private static void writeFlaciTokens(Path out, LexerRunnerResult result) throws IOException {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(out)) {

            AtomicInteger currentLine = new AtomicInteger(-1);

            result.validTokens().forEach(token -> {
                try {
                    if (token.getLine() != currentLine.get()) {
                        if (currentLine.get() != -1) bufferedWriter.newLine();
                        currentLine.set(token.getLine());
                    }

                    bufferedWriter.write(token.getType());
                    bufferedWriter.write(' ');
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            bufferedWriter.newLine();
        }
    }

    private static void writeTokens(Path out, LexerRunnerResult result) throws IOException {
        BufferedWriter bufferedWriter;

        try{
            bufferedWriter = Files.newBufferedWriter(out);
            Stream.concat(result.validTokens().stream(), result.commentTokens().stream())
                    .toList().forEach(token -> writeTokenFormatted(bufferedWriter, token));
            bufferedWriter.close();
        } catch (IOException e){
            throw new IOException("Could not create tokens output file: " + e.getMessage());
        }
    }

    private static void writeErrors(Path out, LexerRunnerResult result) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(out)) {

            result.errorTokens().forEach(token -> writeTokenFormatted(bufferedWriter, token));

        } catch (IOException e) {
            throw new RuntimeException("Error writing lexical errors file", e);
        }
    }

    private static void writeTokenFormatted(BufferedWriter w, Token token) {
        try {
            w.write('[');
            w.write(token.getType());
            w.write(", ");
            w.write(token.getLexeme());
            w.write(", ");
            w.write(Integer.toString(token.getLine()));
            w.write(']');
            w.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
