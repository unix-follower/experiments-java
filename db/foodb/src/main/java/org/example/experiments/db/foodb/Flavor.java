package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Flavor {
    private Integer id;
    private String name;
    private String flavorGroup;
    private String category;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer creatorId;
    private Integer updaterId;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "flavor_group")
    public String getFlavorGroup() {
        return flavorGroup;
    }

    public void setFlavorGroup(String flavorGroup) {
        this.flavorGroup = flavorGroup;
    }

    @Column(nullable = false)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "created_at", nullable = false)
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "updated_at", nullable = false)
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Column(name = "creator_id")
    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "updater_id")
    public Integer getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Integer updaterId) {
        this.updaterId = updaterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Flavor flavor = (Flavor) o;
        return Objects.equals(id, flavor.id)
                && Objects.equals(name, flavor.name)
                && Objects.equals(flavorGroup, flavor.flavorGroup)
                && Objects.equals(category, flavor.category)
                && Objects.equals(createdAt, flavor.createdAt)
                && Objects.equals(updatedAt, flavor.updatedAt)
                && Objects.equals(creatorId, flavor.creatorId)
                && Objects.equals(updaterId, flavor.updaterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, flavorGroup, category, createdAt, updatedAt, creatorId, updaterId);
    }
}
