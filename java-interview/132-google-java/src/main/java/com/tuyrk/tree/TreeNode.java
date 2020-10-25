package com.tuyrk.tree;

import lombok.Data;

/**
 * 树节点
 */
@Data
public class TreeNode {
    private final char value;
    private TreeNode left;
    private TreeNode right;
    private TreeNode parent;

    public void setLeft(TreeNode left) {
        this.left = left;
        if (this.left != null) {
            this.left.setParent(this);
        }
    }

    public void setRight(TreeNode right) {
        this.right = right;
        if (this.right != null) {
            this.right.setParent(this);
        }
    }

    private void setParent(TreeNode parent) {
        this.parent = parent;
    }
}
