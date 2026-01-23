package org.example.lexicalAnalyzer.config;

import java.util.List;
import java.util.Map;

public record DfaYamlConfig(

     String name,

     String startState,

     List<String> alphabet,

     List<String> acceptStates,

     List<String> trapStates,

     List<String> types,

     Map<String, List<String>> stateTypes,

     Map<String, Map<String, String>> transitions ){
}
