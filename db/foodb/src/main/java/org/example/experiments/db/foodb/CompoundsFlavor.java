package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "compounds_flavor")
public class CompoundsFlavor {
    private Integer id;
    private Integer compoundId;
    private Integer flavorId;
    private String citations;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer creatorId;
    private Integer updaterId;
    private Integer sourceId;
    private String sourceType;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "compound_id", nullable = false)
    public Integer getCompoundId() {
        return compoundId;
    }

    public void setCompoundId(Integer compoundId) {
        this.compoundId = compoundId;
    }

    @Column(name = "flavor_id", nullable = false)
    public Integer getFlavorId() {
        return flavorId;
    }

    public void setFlavorId(Integer flavorId) {
        this.flavorId = flavorId;
    }

    @Column(nullable = false)
    public String getCitations() {
        return citations;
    }

    public void setCitations(String citations) {
        this.citations = citations;
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

    @Column(name = "source_id", nullable = false)
    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Column(name = "source_type", nullable = false)
    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompoundsFlavor that = (CompoundsFlavor) o;
        return Objects.equals(id, that.id)
                && Objects.equals(compoundId, that.compoundId)
                && Objects.equals(flavorId, that.flavorId)
                && Objects.equals(citations, that.citations)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt)
                && Objects.equals(creatorId, that.creatorId)
                && Objects.equals(updaterId, that.updaterId)
                && Objects.equals(sourceId, that.sourceId)
                && Objects.equals(sourceType, that.sourceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, compoundId, flavorId, citations, createdAt,
                updatedAt, creatorId, updaterId, sourceId, sourceType
        );
    }
}
