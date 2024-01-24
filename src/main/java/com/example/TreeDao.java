package com.example;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeDao {

    public static List<Tree> read(Session session) {
        List<TreeEntity> treeEntities = session.createQuery("from TreeEntity", TreeEntity.class).list();
        Map<Integer, TreeNode> nodeMap = new HashMap<>();
        Map<Integer, Tree> treeMap = new HashMap<>();

        for (TreeEntity entity : treeEntities) {
            int nodeId = entity.getId();
            int parentId = entity.getParentId();

            TreeNode node = nodeMap.computeIfAbsent(nodeId, TreeNode::new);
            TreeNode parent = nodeMap.computeIfAbsent(parentId, TreeNode::new);

            if (nodeId != parentId) {
                parent.addChild(node);
                node.setParent(parent);
            } else {
                treeMap.putIfAbsent(nodeId, new Tree(node));
            }
        }

        return new ArrayList<>(treeMap.values());
    }
    public static void write(List<Tree> trees, Session session) {
        Transaction truncateTx = session.beginTransaction();
        session.createNativeQuery("TRUNCATE TABLE Trees", TreeEntity.class).executeUpdate();
        truncateTx.commit();
        Transaction tx = session.beginTransaction();
        session.clear();
        for (Tree tree : trees) {
            for (TreeNode node : tree.getAllNodes()) {
                TreeEntity entity = new TreeEntity();
                entity.setId(node.getId());
                if (node.getParent() == null) {
                    entity.setParentId(node.getId());
                } else {
                    entity.setParentId(node.getParent().getId());
                }
                session.saveOrUpdate(entity);
            }
        }
        tx.commit();
    }
    public static void populateDB(Session session) {
        int [] pairs = {1, 1, 2, 1, 3, 1, 4, 4, 5, 4};
        Transaction tx = session.beginTransaction();
        List<TreeEntity> treeEntities = session.createQuery("from TreeEntity", TreeEntity.class).list();
        if (treeEntities.isEmpty()) {
            for (int i = 0; i < pairs.length; i += 2) {
                TreeEntity treeEntity = new TreeEntity(pairs[i + 1]);
                treeEntity.setId(pairs[i]);
                session.save(treeEntity);
            }
        }
        tx.commit();
    }
}
