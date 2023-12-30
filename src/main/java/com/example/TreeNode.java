package com.example;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private int id;
    private TreeNode parent;
    private List<TreeNode> children;

    public TreeNode(int id) {
        this.id = id;
        this.children = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }
    public void removeChild(TreeNode child) {children.remove(child);}

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }
}
