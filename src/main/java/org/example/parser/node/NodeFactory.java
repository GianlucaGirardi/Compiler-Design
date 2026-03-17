package org.example.parser.node;


import lombok.experimental.UtilityClass;
import org.example.lexicalAnalyzer.token.Token;

import java.util.List;

@UtilityClass
public class NodeFactory {

    public static ASTNode marker() {
        return LeafNode.MARKER;
    }

    public static ASTNode createLeaf(NodeType type, Token token) {
        return new LeafNode(type, token);
    }

    public static ASTNode createSubtree(NodeType type, List<ASTNode> children) {
        return new CompositeNode(type, children);
    }

    public static ASTNode createSubtree(NodeType type, ASTNode... children) {
        return new CompositeNode(type, List.of(children));
    }
}
