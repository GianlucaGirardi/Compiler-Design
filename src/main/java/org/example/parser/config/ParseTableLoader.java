package org.example.parser.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.*;
import org.example.parser.*;
import org.example.parser.grammar.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class ParseTableLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    private static final java.util.regex.Pattern ACTION = java.util.regex.Pattern.compile("^\\{[AB]\\d+\\}$");

    @Getter
    private static ParseTable table;

    public void loadFromResources(String resourcePath) {
        try (InputStream in = ParseTableLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("YAML grammar file not found: " + resourcePath);
            }

            Map<String, Map<String, List<String>>> ymlMap = MAPPER.readValue(in, new TypeReference<>() {});
            Map<NonTerminal, Map<String, Production>> table = new HashMap<>();

            ymlMap.forEach((rowKey, rowValue) -> {
                NonTerminal lhs = NonTerminal.valueOf(rowKey);
                Map<String, Production> row = new HashMap<>();

                rowValue.forEach((cellKey, cellValue) ->
                        row.put(cellKey, new Production(lhs, cellValue.stream().map(this::toSymbol).toList())));
                
                table.put(lhs, row);
            });

            ParseTableLoader.table = new ParseTable(table);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load grammar YAML", e);
        }
    }

    private Symbol toSymbol(String raw) {

        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Invalid grammar symbol: " + raw);
        }

        if (ACTION.matcher(raw).matches()) {
            return new SemanticAction(raw);
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
