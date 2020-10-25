package com.tuyrk.tree;

/**
 * 中序遍历下一个结点
 */
public class InOrder {
    public static TreeNode next(TreeNode node) {
        if (node == null) {
            return null;
        }

        if (node.getRight() != null) {
            return first(node.getRight());
        } else {
            while (node.getParent() != null
                    && node.getParent().getLeft() != node) {
                node = node.getParent();
            }
        }
        // node.getParent() == null || node is left child of its parent
        return node.getParent();
    }

    /**
     * 获取中序遍历的第一个节点
     */
    public static TreeNode first(TreeNode node) {
        if (node == null) {
            return null;
        }
        TreeNode curNode = node;
        while (curNode.getLeft() != null) {
            curNode = curNode.getLeft();
        }
        return curNode;
    }

    /**
     * 中序遍历树结构
     */
    public static void traverse(TreeNode root) {
        for (TreeNode node = InOrder.first(root);
             node != null;
             node = InOrder.next(node)) {
            System.out.print(node.getValue());
        }
        System.out.println();
    }

    public static void main(String[] args) {
        traverse(TreeCreator.createSampleTree());
        traverse(TreeCreator.createTree("", ""));
        traverse(TreeCreator.createTree("A", "A"));
        traverse(TreeCreator.createTree("AB", "BA"));
        traverse(TreeCreator.createTree("ABCD", "DCBA"));
        traverse(TreeCreator.createTree("ABCD", "ABCD"));
    }
}
