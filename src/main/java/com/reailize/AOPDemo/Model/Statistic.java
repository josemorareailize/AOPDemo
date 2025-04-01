package com.reailize.AOPDemo.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "statistics")
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private long created;
    @Column
    private long updated;
    @Column
    private long deleted;

    public Statistic() {}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public long getDeleted() {
        return deleted;
    }

    public void setDeleted(long deleted) {
        this.deleted = deleted;
    }
}
