package org.example.parser;

import org.example.parser.node.ASTNode;
import org.example.parser.node.CompositeNode;
import org.example.parser.node.LeafNode;
import org.example.parser.node.NodeType;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class ATSDotWriter {

    private final StringBuilder sb = new StringBuilder();
    private final Map<ASTNode, String> nodeIds = new IdentityHashMap<>();
    private final Set<ASTNode> emitted = Collections.newSetFromMap(new IdentityHashMap<>());
    private int counter = 0;

    public String export(ASTNode root) {
        sb.setLength(0);
        nodeIds.clear();
        emitted.clear();
        counter = 0;

        sb.append("digraph AST {\n");
        sb.append("    rankdir=TB;\n");
        sb.append("    node [shape=box, style=filled, fontname=\"Consolas\"];\n");

        if (root != null) {
            traverse(root);
        }

        sb.append("}\n");
        return sb.toString();
    }

    private void traverse(ASTNode node) {
        writeNode(node);

        if (node instanceof CompositeNode composite) {
            for (ASTNode child : composite.getChildren()) {
                writeNode(child);

                sb.append("    ")
                        .append(getId(node))
                        .append(" -> ")
                        .append(getId(child))
                        .append(";\n");

                traverse(child);
            }
        }
    }

    private void writeNode(ASTNode node) {
        if (!emitted.add(node)) {
            return;
        }

        sb.append("    ")
                .append(getId(node))
                .append(" [label=\"")
                .append(escape(buildLabel(node)))
                .append("\"");

        if (node instanceof LeafNode) {
            sb.append(", fillcolor=\"lightblue\"");
        } else if (node instanceof CompositeNode) {
            sb.append(", fillcolor=\"lightyellow\"");
        } else {
            sb.append(", fillcolor=\"white\"");
        }

        sb.append("];\n");
    }

    private String getId(ASTNode node) {
        return nodeIds.computeIfAbsent(node, n -> "n" + counter++);
    }

    private String buildLabel(ASTNode node) {
        if (node instanceof LeafNode leaf) {
            if (leaf.getType() == NodeType.EPSILON) {
                return "EPSILON";
            }

            if (leaf.getToken() != null) {
                return leaf.getType() + "\n" + leaf.getToken().getLexeme();
            }

            return leaf.getType().toString();
        }

        if (node instanceof CompositeNode composite) {
            return composite.getType().toString();
        }

        return node.getClass().getSimpleName();
    }

    private String escape(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

}
