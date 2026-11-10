package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "compound_ontology_term")
public class CompoundOntologyTerm {
    private Integer id;
    private Integer compoundId;
    private Boolean export;
    private Integer ontologyTermId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

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

    @Column(nullable = false)
    public Boolean getExport() {
        return export;
    }

    public void setExport(Boolean export) {
        this.export = export;
    }

    @Column(name = "ontology_term_id", nullable = false)
    public Integer getOntologyTermId() {
        return ontologyTermId;
    }

    public void setOntologyTermId(Integer ontologyTermId) {
        this.ontologyTermId = ontologyTermId;
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
        CompoundOntologyTerm that = (CompoundOntologyTerm) o;
        return Objects.equals(id, that.id)
                && Objects.equals(compoundId, that.compoundId)
                && Objects.equals(export, that.export)
                && Objects.equals(ontologyTermId, that.ontologyTermId)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, compoundId, export, ontologyTermId, createdAt, updatedAt);
    }
}
