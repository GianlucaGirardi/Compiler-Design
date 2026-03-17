package org.example.parser.node;

import lombok.Data;
import org.example.lexicalAnalyzer.token.Token;

@Data
public class ASTNode {

    private Token token;

    private NodeType type;

    private ASTNode parent;

    private ASTNode rightSibling;

    private ASTNode leftChild;

    private ASTNode leftmostSibling;


}
