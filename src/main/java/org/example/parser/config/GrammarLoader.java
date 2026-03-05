package org.example.parser.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.example.parser.grammar.Grammar;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GrammarLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    public Grammar loadFromResources(String resourcePath, String startSymbol) {
        try (InputStream in = GrammarLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("Grammar YAML not found on classpath: " + resourcePath);
            }

            Map<String, List<List<String>>> raw = MAPPER.readValue(in, new TypeReference<>() {});
            validate(raw, startSymbol);
            Map<String, List<List<String>>> normalized = normalize(raw);

            return new Grammar(startSymbol, normalized);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load grammar YAML: " + resourcePath, e);
        }
    }

    private static Map<String, List<List<String>>> normalize(Map<String, List<List<String>>> raw) {
        Map<String, List<List<String>>> out = new LinkedHashMap<>();

        for (var entry : raw.entrySet()) {
            String lhs = entry.getKey().trim();
            List<List<String>> rhsList = entry.getValue();
            if (rhsList == null) rhsList = List.of();

            List<List<String>> cleanedRhsList = new ArrayList<>();
            for (List<String> rhs : rhsList) {
                if (rhs == null) rhs = List.of();

                List<String> cleaned = new ArrayList<>();
                for (String sym : rhs) {
                    if (sym == null) continue;
                    String s = sym.trim();
                    if (!s.isEmpty()) cleaned.add(s);
                }
                cleanedRhsList.add(cleaned);
            }
            out.put(lhs, cleanedRhsList);
        }

        return out;
    }

    private static void validate(Map<String, List<List<String>>> grammar, String startSymbol) {
        if (grammar == null || grammar.isEmpty()) {
            throw new IllegalArgumentException("Grammar YAML is empty.");
        }
        if (!grammar.containsKey(startSymbol)) {
            throw new IllegalArgumentException(
                    "Start symbol '" + startSymbol + "' not found. Available: " + grammar.keySet());
        }

        for (var e : grammar.entrySet()) {
            if (e.getValue() == null) {
                throw new IllegalArgumentException("Nonterminal '" + e.getKey() + "' has null production list.");
            }
        }
    }
}

