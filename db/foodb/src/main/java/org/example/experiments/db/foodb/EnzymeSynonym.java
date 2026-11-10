package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "enzyme_synonym")
public class EnzymeSynonym {
    private Integer id;
    private Integer enzymeId;
    private String synonym;
    private String source;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "enzyme_id", nullable = false)
    public Integer getEnzymeId() {
        return enzymeId;
    }

    public void setEnzymeId(Integer enzymeId) {
        this.enzymeId = enzymeId;
    }

    @Column(nullable = false)
    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnzymeSynonym that = (EnzymeSynonym) o;
        return Objects.equals(id, that.id)
                && Objects.equals(enzymeId, that.enzymeId)
                && Objects.equals(synonym, that.synonym)
                && Objects.equals(source, that.source)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enzymeId, synonym, source, createdAt, updatedAt);
    }
}
