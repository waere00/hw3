package com.example;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeDao {

    public List<Tree> readTrees(Session session) {
        List<TreeEntity> treeEntities = session.createQuery("from TreeEntity", TreeEntity.class).list();
        Map<Integer, TreeNode> nodes = new HashMap<>();
        for (TreeEntity entity : treeEntities) {
            nodes.putIfAbsent(entity.getId(), new TreeNode(entity.getId()));
        }
        TreeNode root = null;
        for (TreeEntity entity : treeEntities) {
            TreeNode currentNode = nodes.get(entity.getId());
            if (entity.getParentId() != null) {
                TreeNode parent = nodes.get(entity.getParentId());
                currentNode.setParent(parent);
                parent.addChild(currentNode);
            } else {
                root = currentNode;
            }
        }
        List<Tree> result = new ArrayList<>();
        // Создание дерева
        if (root != null) {
            result.add(new Tree(root));
        }
        return result;
    }

    public void writeTrees(List<Tree> trees, Session session) {
        Transaction tx = session.beginTransaction();
        for (Tree tree : trees) {
            for (TreeNode node : tree.getAllNodes()) {
                TreeEntity entity = new TreeEntity();
                entity.setId(node.getId());
                if (node.getParent() != null) {
                    entity.setParentId(node.getParent().getId());
                }
                session.saveOrUpdate(entity);
            }
        }
        tx.commit();
    }
    public TreeNode findNodeById(int nodeId, Session session) {
        return (TreeNode) session.get(TreeNode.class, nodeId);
    }

    public void addChild(TreeNode parent, TreeNode child, Session session) {
        Transaction tx = session.beginTransaction();
        parent.addChild(child);
        child.setParent(parent);
        session.saveOrUpdate(parent);
        session.saveOrUpdate(child);
        tx.commit();
    }

    public void removeNode(int nodeId, Session session) {
        TreeNode node = findNodeById(nodeId, session);
        Transaction tx = session.beginTransaction();
        TreeNode parent = node.getParent();
        if (parent != null) {
            parent.removeChild(node);
            session.saveOrUpdate(parent);
        }
        session.delete(node);
        tx.commit();
    }

    public void saveOrUpdateNode(TreeNode node, Session session) {
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(node);
        tx.commit();
    }

    public static void populateDB(Session session) {
        int [] pairs = {1, 1, 2, 1, 3, 1, 4, 4, 5, 4};
        Transaction tx = session.beginTransaction();
        for (int i = 0; i < pairs.length; i += 2) {
            TreeEntity treeEntity = new TreeEntity(pairs[i + 1]);
            treeEntity.setId(pairs[i]);
            session.save(treeEntity);
        }
        tx.commit();
    }
}
