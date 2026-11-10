package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "health_effect")
public class HealthEffect {
    private Integer id;
    private String name;
    private String description;
    private String chebiName;
    private Integer chebiId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer creatorId;
    private Integer updaterId;
    private String chebiDefinition;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "chebi_name")
    public String getChebiName() {
        return chebiName;
    }

    public void setChebiName(String chebiName) {
        this.chebiName = chebiName;
    }

    @Column(name = "chebi_id")
    public Integer getChebiId() {
        return chebiId;
    }

    public void setChebiId(Integer chebiId) {
        this.chebiId = chebiId;
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

    @Column(name = "chebi_definition")
    public String getChebiDefinition() {
        return chebiDefinition;
    }

    public void setChebiDefinition(String chebiDefinition) {
        this.chebiDefinition = chebiDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HealthEffect that = (HealthEffect) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(chebiName, that.chebiName)
                && Objects.equals(chebiId, that.chebiId)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt)
                && Objects.equals(creatorId, that.creatorId)
                && Objects.equals(updaterId, that.updaterId)
                && Objects.equals(chebiDefinition, that.chebiDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, name, description, chebiName, chebiId, createdAt,
                updatedAt, creatorId, updaterId, chebiDefinition
        );
    }
}
