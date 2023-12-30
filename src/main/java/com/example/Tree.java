package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Tree {
    private TreeNode root;

    public Tree(TreeNode root) {
        this.root = root;
    }

    public TreeNode getRoot() {
        return root;
    }

    public List<TreeNode> getAllNodes() {
        List<TreeNode> allNodes = new ArrayList<>();
        findChildNodes(root, allNodes);
        return allNodes;
    }

    private void findChildNodes(TreeNode node, List<TreeNode> allNodes) {
        if (node != null) {
            allNodes.add(node);
            for (TreeNode child : node.getChildren()) {
                findChildNodes(child, allNodes);
            }
        }
    }

    public List<TreeNode> getAllLeaves() {
        List<TreeNode> leaves = new ArrayList<>();
        for (TreeNode node : getAllNodes()) {
            if (node.isLeaf()) {
                leaves.add(node);
            }
        }
        return leaves;
    }

    public String toString() {
        StringBuilder info = new StringBuilder();
        for(TreeNode node : getAllNodes()) {
            info.append("Узел: ").append(node.getId())
                    .append(", Родитель: ").append(node.getParent() != null ? node.getParent().getId() : "None")
                    .append(", Дети: ");
            node.getChildren().forEach(child -> info.append(child.getId()).append(" "));
            info.append("\n");
        }
        return info.toString();
    }

    public void addChildById(int parentId, int childId) {
        TreeNode child = new TreeNode(childId);
        findNodeById(root, parentId).ifPresent(parent -> {
            parent.addChild(child);
            child.setParent(parent);
        });
    }
    public void addParentById(int childId, int parentId) {
        TreeNode parent = new TreeNode(parentId);
        findNodeById(root, childId).ifPresent(child -> {
            if (root == child) {
                parent.addChild(child);
                child.setParent(parent);
                root = parent;
            } else {
                TreeNode oldParent = child.getParent();
                parent.addChild(child);
                child.setParent(parent);
                oldParent.addChild(parent);
                oldParent.removeChild(child);
                parent.setParent(oldParent);
            }
        });
    }

    public void removeNodeById(int nodeId) {
        Optional<TreeNode> toRemove = findNodeById(root, nodeId);
        toRemove.ifPresent(node -> {
            TreeNode parent = node.getParent();
            parent.removeChild(node);
            node.getChildren().forEach(child -> child.setParent(parent));
            node.getChildren().forEach(parent::addChild);
        });
    }

    private Optional<TreeNode> findNodeById(TreeNode node, int id) {if (node.getId() == id) {
        return Optional.of(node);
    }
        for(TreeNode child : node.getChildren()) {
            Optional<TreeNode> found = findNodeById(child, id);
            if(found.isPresent()) {
                return found;
            }
        }
        return Optional.empty();
    }
}
