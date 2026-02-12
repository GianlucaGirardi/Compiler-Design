package org.example.parser.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.example.parser.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseTableLoader {

    private static final ObjectMapper MAPPER =
            new ObjectMapper(new YAMLFactory());

    public ParseTable loadFromResources(String resourcePath) {
        try (InputStream in = ParseTableLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("YAML grammar file not found: " + resourcePath);
            }

            Map<String, Map<String, List<String>>> ymlMap = MAPPER.readValue(in, new TypeReference<>() {});
            Map<NonTerminal, Map<String, Production>> table = new HashMap<>();

            ymlMap.forEach((rowKey, rowValue) -> {
                Map<String, Production> row = new HashMap<>();

                rowValue.forEach((cellKey, cellValue) -> {
                    row.put(cellKey,
                            new Production((NonTerminal) this.toSymbol(rowKey),
                                    cellValue.stream().map(this::toSymbol).toList()));

                });
            });

            return
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load grammar YAML", e);
        }
    }

    private Symbol toSymbol(String raw) {

        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Invalid grammar symbol: " + raw);
        }

        if (Character.isUpperCase(raw.charAt(0))) {
            try {
                return NonTerminal.valueOf(raw);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown NonTerminal in grammar: " + raw, e);
            }
        }

        return new Terminal(raw);
    }

}
