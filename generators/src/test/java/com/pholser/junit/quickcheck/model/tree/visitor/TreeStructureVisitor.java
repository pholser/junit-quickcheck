package com.pholser.junit.quickcheck.model.tree.visitor;

public class TreeStructureVisitor implements TreeVisitor {
    @Override public Integer visit(Empty empty) {
        return 0;
    }

    @Override public Integer visit(Leaf leaf) {
        return 1;
    }

    @Override public Integer visit(Node node) {
        Integer leftDepth = (Integer) node.getLeft().accept(this);
        Integer rightDepth = (Integer) node.getRight().accept(this);
        return 1 + Math.max(leftDepth, rightDepth);
    }
}
