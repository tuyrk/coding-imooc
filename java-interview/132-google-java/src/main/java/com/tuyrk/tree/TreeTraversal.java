package com.tuyrk.tree;

import org.junit.Test;
import sun.jvm.hotspot.memory.PlaceholderEntry;

import java.util.Arrays;

/**
 * 树的遍历
 */
public class TreeTraversal {
    /**
     * 前序遍历
     */
    public static void preOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.print(root.getValue());
        preOrder(root.getLeft());
        preOrder(root.getRight());
    }

    /**
     * 中序遍历
     */
    public static void inOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        inOrder(root.getLeft());
        System.out.print(root.getValue());
        inOrder(root.getRight());
    }

    /**
     * 后序遍历
     */
    public static void postOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        postOrder(root.getLeft());
        postOrder(root.getRight());
        System.out.print(root.getValue());
    }

    /**
     * 通过前序遍历和中序遍历得到后序遍历的结果
     */
    public static String postOrder(String preOrder, String inOrder) {
        if (preOrder.isEmpty()) {
            return "";
        }

        char rootValue = preOrder.charAt(0);
        int rootIndex = inOrder.indexOf(rootValue);

        return postOrder(preOrder.substring(1, 1 + rootIndex), inOrder.substring(0, rootIndex))
                + postOrder(preOrder.substring(1 + rootIndex), inOrder.substring(1 + rootIndex))
                + rootValue;
    }

    @Test
    public void order() {
        TreeNode sampleTree = TreeCreator.createSampleTree();
        preOrder(sampleTree);
        System.out.println();
        inOrder(sampleTree);
        System.out.println();
        postOrder(sampleTree);
    }

    @Test
    public void create() {
        postOrder(TreeCreator.createTree("ABDEGCF", "DBGEACF"));
        System.out.println();
        postOrder(TreeCreator.createTree("", ""));
        System.out.println();
        postOrder(TreeCreator.createTree("A", "A"));
        System.out.println();
        postOrder(TreeCreator.createTree("AB", "BA"));
    }

    @Test
    public void postOrder() {
        System.out.println(postOrder("ABDEGCF", "DBGEACF"));
        System.out.println(postOrder("", ""));
        System.out.println(postOrder("A", "A"));
        System.out.println(postOrder("AB", "BA"));
    }

    @Test
    public void examples() {
        System.out.println(postOrder("ACDEFHGB", "DECAHFBG"));
        System.out.println(postOrder("ABDGCEFH", "DGBAECFH"));
    }

    @Test
    public void exercise() {
    }
}
