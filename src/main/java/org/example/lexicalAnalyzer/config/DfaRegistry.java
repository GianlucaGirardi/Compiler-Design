package org.example.lexicalAnalyzer.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.example.lexicalAnalyzer.config.Constants.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DfaRegistry {

    private static final Map<String, TransitionTable> TABLES = new HashMap<>();

    private static boolean initialized = false;

    public static synchronized void init() {
        if (initialized) return;

        InputStream stream;
        BufferedReader reader;

        try  {
            stream = DfaRegistry.class.getClassLoader().getResourceAsStream(DFA_INDEX_PATH);
            if (stream == null) {
                throw new IllegalArgumentException("Missing DFA index file: " + DFA_INDEX_PATH);
            }

            try {
                reader = new BufferedReader(new InputStreamReader(stream));
                String file;
                while ((file = reader.readLine()) != null) {
                    file = file.trim();
                    if (file.isEmpty() || file.startsWith(INDEX_FILE_COMMENT)) continue;

                    String resourcePath = DFA_RESOURCE_PATH + file;
                    TransitionTable table = TransitionTableLoader.loadFromResources(resourcePath);

                    TABLES.put(table.getName(), table);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            initialized = true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DFA registry", e);
        }
    }

    public static TransitionTable getTable(String dfaName) {
        if (!initialized) {
            throw new IllegalStateException("Call DfaRegistry.init() on boot first.");
        }

        TransitionTable table = TABLES.get(dfaName);
        if (table == null){
            throw new IllegalArgumentException("Unknown DFA name: " + dfaName);
        }

        return table;
    }
}

