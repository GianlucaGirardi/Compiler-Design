package org.example.parser;

import org.example.lexicalAnalyzer.token.Token;
import org.example.parser.node.*;

import java.util.List;

public final class SemanticActionExecutor {

    public void execute(String action, SemanticStack sem, Token lastMatched) {
        System.out.println("Executing semantic action: " + action +
                " with lastMatched: " + lastMatched +
                " | stack before = " + sem.debugView());
        switch (action) {

            case "{A1}" -> sem.pushMarker();

            case "{A2}" -> sem.push(NodeFactory.createLeaf(NodeType.ID, requireLast(action, lastMatched)));
            case "{A3}" -> sem.push(NodeFactory.createLeaf(NodeType.NUM, requireLast(action, lastMatched)));
            case "{A4}" -> sem.push(NodeFactory.createLeaf(NodeType.TYPE, requireLast(action, lastMatched)));
            case "{A5}" -> sem.push(NodeFactory.createLeaf(NodeType.VOID, requireLast(action, lastMatched)));
            case "{A6}" -> sem.push(NodeFactory.createLeaf(NodeType.VISIBILITY, requireLast(action, lastMatched)));
            case "{A7}" -> sem.push(NodeFactory.createLeaf(NodeType.ASSIGN_OP, requireLast(action, lastMatched)));
            case "{A8}" -> sem.push(NodeFactory.createLeaf(NodeType.REL_OP, requireLast(action, lastMatched)));
            case "{A9}" -> sem.push(NodeFactory.createLeaf(NodeType.ADD_OP, requireLast(action, lastMatched)));
            case "{A10}" -> sem.push(NodeFactory.createLeaf(NodeType.MULT_OP, requireLast(action, lastMatched)));
            case "{A11}" -> sem.push(NodeFactory.createLeaf(NodeType.SIGN, requireLast(action, lastMatched)));
            case "{A12}" -> sem.push(NodeFactory.createLeaf(NodeType.NOT, requireLast(action, lastMatched)));
            case "{A13}" -> sem.push(NodeFactory.createLeaf(NodeType.EPSILON, null));


            // Program level
            case "{B1}" -> sem.push(NodeFactory.createSubtree(NodeType.CLASS_DECL_LIST, sem.popUntilMarker(action)));
            case "{B2}" -> sem.push(NodeFactory.createSubtree(NodeType.FUNC_DEF_LIST, sem.popUntilMarker(action)));
            case "{B3}" -> {
                ASTNode funcBody = sem.pop();
                ASTNode funcDefList = sem.pop();
                ASTNode classDeclList = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.PROG, classDeclList, funcDefList, funcBody));
            }

            // Class / members / inheritance
            case "{B4}" -> sem.push(NodeFactory.createSubtree(NodeType.INHERITANCE_LIST, sem.popUntilMarker(action)));
            case "{B5}" -> sem.push(NodeFactory.createSubtree(NodeType.MEMBER_DECL_LIST, sem.popUntilMarker(action)));
            case "{B6}" -> {
                ASTNode member = sem.pop();
                ASTNode vis = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.MEMBER_DECL, vis, member));
            }
            case "{B7}" -> {
                ASTNode memberDeclList = sem.pop();
                ASTNode inheritanceList = sem.pop();
                ASTNode id = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.CLASS_DECL, id, inheritanceList, memberDeclList));
            }

            // Decls / params / return
            case "{B8}" -> sem.push(NodeFactory.createSubtree(NodeType.DIMENSION_LIST, sem.popUntilMarker(action)));

            case "{B9}" -> {
                ASTNode dim = sem.pop();
                ASTNode id = sem.pop();
                ASTNode type = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.VAR_DECL, type, id, dim));
            }

            case "{B10}" -> {
                ASTNode dim = sem.pop();
                ASTNode id = sem.pop();
                ASTNode type = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.FPARAM, type, id, dim));
            }

            case "{B11}" -> sem.push(NodeFactory.createSubtree(NodeType.FPARAM_LIST, sem.popUntilMarker(action)));
            case "{B12}" -> sem.push(NodeFactory.createSubtree(NodeType.APARAMS, sem.popUntilMarker(action)));

            case "{B13}" -> {
                ASTNode typeOrVoid = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.RETURN_TYPE, typeOrVoid));
            }

            case "{B14}" -> {
                ASTNode returnType = sem.pop();
                ASTNode fParamList = sem.pop();
                ASTNode id = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.FUNC_DECL, id, fParamList, returnType));
            }

            case "{B15}" -> {
                ASTNode id = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.SCOPE_RESOLUTION, id));
            }

            case "{B16}" -> {
                ASTNode funcBody = sem.pop();
                ASTNode returnType = sem.pop();
                ASTNode fParamList = sem.pop();
                ASTNode scopeRes = sem.pop();
                ASTNode id = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.FUNC_DEF, scopeRes, id, fParamList, returnType, funcBody));
            }

            // FuncBody / blocks / statement lists
            case "{B17}" -> sem.push(NodeFactory.createSubtree(NodeType.VAR_DECL_LIST, sem.popUntilMarker(action)));
            case "{B18}" -> sem.push(NodeFactory.createSubtree(NodeType.STATEMENT_LIST, sem.popUntilMarker(action)));

            case "{B19}" -> {
                ASTNode stmtList = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.STAT_BLOCK, stmtList));
            }

            case "{B20}" -> {
                ASTNode statBlock = sem.pop();
                ASTNode varDeclList = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.FUNC_BODY, varDeclList, statBlock));
            }

            // Statements
            case "{B21}" -> {
                ASTNode expr = sem.pop();
                ASTNode assignOp = sem.pop();
                ASTNode dataMember = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.ASSIGN_STAT, dataMember, assignOp, expr));
            }

            case "{B22}" -> {
                ASTNode elseBlock = sem.pop();
                ASTNode thenBlock = sem.pop();
                ASTNode relExpr = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.IF_STAT, relExpr, thenBlock, elseBlock));
            }

            case "{B23}" -> {
                ASTNode block = sem.pop();
                ASTNode relExpr = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.WHILE_STAT, relExpr, block));
            }

            case "{B24}" -> sem.push(NodeFactory.createSubtree(NodeType.READ_STAT, sem.pop()));
            case "{B25}" -> sem.push(NodeFactory.createSubtree(NodeType.WRITE_STAT, sem.pop()));
            case "{B26}" -> sem.push(NodeFactory.createSubtree(NodeType.RETURN_STAT, sem.pop()));

            // Access / calls / chaining
            case "{B27}" -> sem.push(NodeFactory.createSubtree(NodeType.INDEX_LIST, sem.popUntilMarker(action)));

            case "{B28}" -> {
                ASTNode indexList = sem.pop();
                ASTNode id = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.DATA_MEMBER, id, indexList));
            }

            case "{B29}" -> {
                ASTNode aParams = sem.pop();
                ASTNode id = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.FUNC_CALL, id, aParams));
            }

            case "{B30}" -> {
                ASTNode dmOrCall = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.DOT_PARAM, dmOrCall));
            }

            case "{B31}" -> sem.push(NodeFactory.createSubtree(
                    NodeType.DOT_ACCESS,
                    sem.popAccessChain()
            ));

            // Expressions
            case "{B32}" -> {
                ASTNode right = sem.pop();
                ASTNode op = sem.pop();
                ASTNode left = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.REL_EXPR, left, op, right));
            }

            case "{B33}" -> sem.push(NodeFactory.createSubtree(NodeType.ARITH_EXPR, sem.popUntilMarker(action)));
            case "{B34}" -> sem.push(NodeFactory.createSubtree(NodeType.TERM, sem.popUntilMarker(action)));

            // Unary
            case "{B35}" -> sem.push(NodeFactory.createSubtree(NodeType.NOT_FACTOR, sem.pop()));
            case "{B36}" -> {
                ASTNode factor = sem.pop();
                ASTNode sign = sem.pop();
                sem.push(NodeFactory.createSubtree(NodeType.SIGNED_FACTOR, sign, factor));
            }

            case "{B37}" -> {
                List<ASTNode> chain = sem.popAccessChain();

                if (chain.isEmpty()) {
                    throw new IllegalStateException("B37 expected a function-call chain but found nothing");
                }

                ASTNode reduced = reduceAccessChainForFactor(chain);

                if (reduced.getType() != NodeType.FUNC_CALL && reduced.getType() != NodeType.DOT_ACCESS) {
                    throw new IllegalStateException(
                            "B37 expected FUNC_CALL or DOT_ACCESS in statement position but found " + reduced.getType()
                    );
                }

                sem.push(NodeFactory.createSubtree(NodeType.FUNC_CALL_STAT, reduced));
            }

            case "{B38}" -> {
                List<ASTNode> chain = sem.popAccessChain();

                if (chain.isEmpty()) {
                    throw new IllegalStateException("B38 expected factor chain but found nothing");
                }

                sem.push(reduceAccessChainForFactor(chain));
            }

            default -> throw new IllegalArgumentException("Unknown semantic action: " + action);
        }

    }

    private ASTNode reduceAccessChainForFactor(List<ASTNode> chain) {
        if (chain.isEmpty()) {
            throw new IllegalStateException("Cannot reduce empty access chain");
        }

        if (chain.size() == 1 && chain.get(0).getType() == NodeType.ID) {
            return chain.get(0);
        }

        if (chain.size() == 1 && chain.get(0).getType() == NodeType.DOT_PARAM) {
            ASTNode inner = unwrapDotParam(chain.get(0));

            if (inner.getType() == NodeType.FUNC_CALL) {
                return inner;
            }

            if (inner.getType() == NodeType.DATA_MEMBER) {
                return reduceSingleDataMemberFactor(chain.get(0), inner);
            }

            throw new IllegalStateException(
                    "Unexpected DOT_PARAM child in factor reduction: " + inner.getType()
            );
        }
        return NodeFactory.createSubtree(NodeType.DOT_ACCESS, chain);
    }

    private ASTNode reduceSingleDataMemberFactor(ASTNode dotParamNode, ASTNode dataMemberNode) {
        if (!(dataMemberNode instanceof CompositeNode composite)) {
            throw new IllegalStateException("DATA_MEMBER node is malformed");
        }

        if (composite.getType() != NodeType.DATA_MEMBER) {
            throw new IllegalStateException(
                    "Expected DATA_MEMBER but found " + composite.getType()
            );
        }

        if (composite.getChildren().size() != 2) {
            throw new IllegalStateException("DATA_MEMBER should have exactly 2 children");
        }

        ASTNode idNode = composite.getChildren().get(0);
        ASTNode indexListNode = composite.getChildren().get(1);

        if (idNode.getType() != NodeType.ID) {
            throw new IllegalStateException(
                    "DATA_MEMBER first child should be ID but found " + idNode.getType()
            );
        }

        if (indexListNode.getType() != NodeType.INDEX_LIST) {
            throw new IllegalStateException(
                    "DATA_MEMBER second child should be INDEX_LIST but found " + indexListNode.getType()
            );
        }

        if (isEmptyIndexList(indexListNode)) {
            return idNode;
        }
        return NodeFactory.createSubtree(NodeType.DOT_ACCESS, dotParamNode);
    }

    private boolean isEmptyIndexList(ASTNode node) {
        if (!(node instanceof CompositeNode composite)) {
            return false;
        }

        if (composite.getType() != NodeType.INDEX_LIST) {
            return false;
        }
        return composite.getChildren().isEmpty();
    }

    private ASTNode unwrapDotParam(ASTNode node) {
        if (!(node instanceof CompositeNode composite)) {
            throw new IllegalStateException("DOT_PARAM node is malformed");
        }

        if (composite.getType() != NodeType.DOT_PARAM) {
            throw new IllegalStateException(
                    "Expected DOT_PARAM but found " + composite.getType()
            );
        }

        if (composite.getChildren().size() != 1) {
            throw new IllegalStateException("DOT_PARAM should have exactly 1 child");
        }
        return composite.getChildren().get(0);
    }


    private Token requireLast(String action, Token lastMatched) {
        if (lastMatched == null) {
            throw new IllegalStateException(action + " requires lastMatched token, but it is null");
        }
        return lastMatched;
    }
}
