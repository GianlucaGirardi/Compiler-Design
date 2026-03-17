package org.example.parser.node;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.lexicalAnalyzer.token.Token;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeafNode extends ASTNode {

    private final NodeType type;

    private final Token token;

    public static final LeafNode MARKER = new LeafNode(NodeType.MARKER, null);

}
