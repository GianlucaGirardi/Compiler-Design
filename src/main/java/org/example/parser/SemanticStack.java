package org.example.parser;

import org.example.parser.node.ASTNode;
import org.example.parser.node.NodeType;
import org.example.parser.node.NodeFactory;

import java.util.*;

public class SemanticStack {

    private final Deque<ASTNode> semanticStack = new ArrayDeque<>();
    private static final ASTNode MARKER = NodeFactory.marker();

    public void push(ASTNode n) {
        if (n == null) {
            throw new IllegalArgumentException("Cannot push null ASTNode onto semantic stack");
        }
        semanticStack.push(n);
    }

    public ASTNode pop() {
        if (semanticStack.isEmpty()) {
            throw new IllegalStateException("Cannot pop from an empty semantic stack");
        }
        return semanticStack.pop();
    }

    public ASTNode peek() {
        return semanticStack.peek();
    }

    public boolean isEmpty() {
        return semanticStack.isEmpty();
    }

    public void pushMarker() {
        semanticStack.push(MARKER);
    }

    public List<ASTNode> popUntilMarker(String action) {
        List<ASTNode> items = new ArrayList<>();

        while (true) {
            if (semanticStack.isEmpty()) {
                throw new IllegalStateException("Semantic marker not found while executing " + action + " | current stack = " + debugView());
            }

            if (semanticStack.peek() == MARKER) {
                semanticStack.pop();
                Collections.reverse(items);
                return items;
            }
            items.add(semanticStack.pop());
        }
    }

    public String debugView() {
        return semanticStack.stream()
                .map(n -> n.getType().name())
                .toList()
                .toString();
    }

    public List<ASTNode> popAccessChain() {
        List<ASTNode> items = new ArrayList<>();

        while (!semanticStack.isEmpty()) {
            ASTNode top = semanticStack.peek();
            NodeType t = top.getType();

            if (top == MARKER) {
                semanticStack.pop();
                Collections.reverse(items);
                return items;
            }

            if (t == NodeType.DOT_PARAM || t == NodeType.ID || t == NodeType.DATA_MEMBER || t == NodeType.FUNC_CALL) {
                items.add(semanticStack.pop());
                continue;
            }

            break;
        }

        if (items.isEmpty()) {
            throw new IllegalStateException("B31 could not build DOT_ACCESS: no access-chain items found");
        }

        Collections.reverse(items);
        return items;
    }

    public int size() {
        return semanticStack.size();
    }

    public void clear() {
        semanticStack.clear();
    }
}
