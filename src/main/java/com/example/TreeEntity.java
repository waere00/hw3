package com.example;


import jakarta.persistence.*;

@Entity
@Table(name = "trees")
public class TreeEntity {
    @Id
    public int id;

    @Column(name = "parent_id")
    public int parentId;

    public TreeEntity() {}

    public TreeEntity(Integer parentId) {
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
