package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "ontology_synonym")
public class OntologySynonym {
    private Integer id;
    private Integer ontologyTermId;
    private String synonym;
    private String externalId;
    private String externalSource;
    private String parentId;
    private String parentSource;
    private String comment;
    private String curator;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ontology_term_id", nullable = false)
    public Integer getOntologyTermId() {
        return ontologyTermId;
    }

    public void setOntologyTermId(Integer ontologyTermId) {
        this.ontologyTermId = ontologyTermId;
    }

    @Column(nullable = false)
    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    @Column(name = "external_id")
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Column(name = "external_srouce")
    public String getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(String externalSource) {
        this.externalSource = externalSource;
    }

    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "parent_source")
    public String getParentSource() {
        return parentSource;
    }

    public void setParentSource(String parentSource) {
        this.parentSource = parentSource;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCurator() {
        return curator;
    }

    public void setCurator(String curator) {
        this.curator = curator;
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
        OntologySynonym that = (OntologySynonym) o;
        return Objects.equals(id, that.id)
                && Objects.equals(ontologyTermId, that.ontologyTermId)
                && Objects.equals(synonym, that.synonym)
                && Objects.equals(externalId, that.externalId)
                && Objects.equals(externalSource, that.externalSource)
                && Objects.equals(parentId, that.parentId)
                && Objects.equals(parentSource, that.parentSource)
                && Objects.equals(comment, that.comment)
                && Objects.equals(curator, that.curator)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, ontologyTermId, synonym, externalId, externalSource, parentId,
                parentSource, comment, curator, createdAt, updatedAt
        );
    }
}
