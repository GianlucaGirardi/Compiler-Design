package org.example.parser.node;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompositeNode extends ASTNode{

    private final NodeType type;

    private final List<ASTNode> children;

}
