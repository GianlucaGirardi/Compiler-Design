package org.example.lexicalAnalyzer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransitionTableLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());
    private static final String DEFAULT = "DEFAULT";

    public static TransitionTable loadFromResources(String resourcePath) {
        try (InputStream in = TransitionTableLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {

            if (in == null) {
                throw new IllegalArgumentException("YAML file not found in resources folder: " + resourcePath);
            }

            DfaYamlConfig config = MAPPER.readValue(in, DfaYamlConfig.class);
            validate(config, resourcePath);

            String startState = config.startState();

            Map<String, Map<String, String>> delta = new HashMap<>();

            config.transitions().forEach((state, row) ->
                    delta.put(state, row == null ? new HashMap<>() : new HashMap<>(row)));

            Set<String> accept = new HashSet<>(config.acceptStates());

            Set<String> traps = (config.trapStates() == null)
                    ? Collections.emptySet()
                    : new HashSet<>(config.trapStates());

            Map<String, String> stateToType = new HashMap<>();
            buildStateToTypeMapFromYAML(config, stateToType);

            return new TransitionTable(config.name(), new HashSet<>(config.alphabet()),
                    accept, startState, startState,  traps, delta, stateToType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load DFA YAML: " + resourcePath, e);
        }
    }

    private static void buildStateToTypeMapFromYAML(DfaYamlConfig config, Map<String, String> stateToType){
        if (config.types() == null || config.stateTypes() == null) {
            throw new IllegalArgumentException("Missing types or type assignment");
        }

        Map<String,Set<String>> stateTypesMap = new HashMap<>();
        config.types().forEach((type) -> stateTypesMap.put(type, new HashSet<>(config.stateTypes().get(type))));

        stateTypesMap.forEach((type, states) -> {
            states.forEach(state -> {
                String prev = stateToType.putIfAbsent(state, type);
                if (prev != null) {
                    throw new IllegalArgumentException(
                            "State '" + state + "' belongs to multiple types... " +
                                    "Sets are not disjoint: " + prev + ": " + type);
                }
            });
        });
    }

    /* ensure .yml dfa is properly filled */
    private static void validate(DfaYamlConfig config, String path) {
        validateGeneralSetup(config, path);

        validateStatesInTransitions(config, path);

        validateTrapStateRows(config, path);

        validateTransitions(config, path);
    }

        private static void validateGeneralSetup(DfaYamlConfig config, String path){
            if (config == null) throw new IllegalArgumentException(path + ": YAML is empty");
            if (config.name() == null || config.name().isBlank())
                throw new IllegalArgumentException(path + ": missing 'name'");

            if (config.startState() == null || config.startState().isBlank())
                throw new IllegalArgumentException(path + ": missing 'startState'");

            if (config.alphabet() == null || config.alphabet().isEmpty())
                throw new IllegalArgumentException(path + ": missing/empty 'alphabet'");

            if (config.acceptStates() == null || config.acceptStates().isEmpty())
                throw new IllegalArgumentException(path + ": missing/empty 'acceptStates'");

            if (config.transitions() == null || config.transitions().isEmpty())
                throw new IllegalArgumentException(path + ": missing/empty 'transitions'");


        }

        private static void validateStatesInTransitions(DfaYamlConfig config, String path){
            if (!config.transitions().containsKey(config.startState())) {
                throw new IllegalArgumentException(
                        path + ": startState '" + config.startState() + "' not found in transitions");
            }

            List<String> errorList = config.acceptStates().stream().filter(acc ->
                    !config.transitions().containsKey(acc)).toList();

            if(!errorList.isEmpty()){
                errorList.forEach(e -> {
                    throw new IllegalArgumentException(path + ": acceptState '" +  e + "' not found in transitions");
                });
            }
        }

        public static void validateTrapStateRows(DfaYamlConfig config, String path){
            if (config.trapStates() != null && !config.trapStates().isEmpty()) {
                var unmappedTrapStates = config.trapStates().stream().filter(trap ->
                        !config.transitions().containsKey(trap)).toList();

                if(!unmappedTrapStates.isEmpty()){
                    unmappedTrapStates.forEach(el -> {
                        throw new IllegalArgumentException(
                                path + ": trapState '" + el + "' not found in transitions");
                    });
                }
            }
        }

    public static void validateTransitions(DfaYamlConfig config, String path) {
        Set<String> alphabetSet = new HashSet<>(config.alphabet());
        Map<String, Map<String, String>> transitions = config.transitions();

        transitions.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .forEach(stateEntry -> {
                    String state = stateEntry.getKey();
                    Map<String, String> row = stateEntry.getValue();

                    /* Validate input symbols */
                    row.keySet().stream()
                            .filter(symbol -> !DEFAULT.equals(symbol))
                            .filter(symbol -> !alphabetSet.contains(symbol))
                            .findFirst()
                            .ifPresent(badSymbol -> {
                                throw new IllegalArgumentException(
                                        path + ": state '" + state + "' uses symbol '" + badSymbol +
                                                "' not in alphabet " + alphabetSet + " (or '" + DEFAULT + "')"
                                );
                            });

                    /* validate states */
                    row.entrySet().stream()
                            .filter(move -> move.getValue() == null || move.getValue().isBlank())
                            .findFirst()
                            .ifPresent(badMove -> {
                                throw new IllegalArgumentException(
                                        path + ": state '" + state + "' has blank nextState for symbol '" + badMove.getKey() + "'"
                                );
                            });

                    /* validate transitions */
                    row.entrySet().stream()
                            .filter(move -> move.getValue() != null && !move.getValue().isBlank())
                            .filter(move -> !transitions.containsKey(move.getValue()))
                            .findFirst()
                            .ifPresent(badMove -> {
                                throw new IllegalArgumentException(
                                        path + ": transition from '" + state + "' on '" + badMove.getKey() +
                                                "' points to unknown state '" + badMove.getValue() + "'"
                                );
                            });
                });
    }

}
