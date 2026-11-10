package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Nutrient {
    private Integer id;
    private Integer legacyId;
    private String type;
    private String publicId;
    private String name;
    private String export;
    private Boolean state;
    private String annotationQuality;
    private String description;
    private String wikipediaId;
    private String comments;
    private String dfcId;
    private String dukeId;
    private Integer eafusId;
    private String dfcName;
    private String compoundSource;
    private String metabolism;
    private String synthesisCitations;
    private String generalCitations;
    private Integer creatorId;
    private Integer updaterId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "legacy_id")
    public Integer getLegacyId() {
        return legacyId;
    }

    public void setLegacyId(Integer legacyId) {
        this.legacyId = legacyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "public_id", nullable = false)
    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public String getExport() {
        return export;
    }

    public void setExport(String export) {
        this.export = export;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Column(name = "annotation_quality", nullable = false)
    public String getAnnotationQuality() {
        return annotationQuality;
    }

    public void setAnnotationQuality(String annotationQuality) {
        this.annotationQuality = annotationQuality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "wikipedia_id")
    public String getWikipediaId() {
        return wikipediaId;
    }

    public void setWikipediaId(String wikipediaId) {
        this.wikipediaId = wikipediaId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Column(name = "dfc_id")
    public String getDfcId() {
        return dfcId;
    }

    public void setDfcId(String dfcId) {
        this.dfcId = dfcId;
    }

    @Column(name = "duke_id")
    public String getDukeId() {
        return dukeId;
    }

    public void setDukeId(String dukeId) {
        this.dukeId = dukeId;
    }

    @Column(name = "eafus_id")
    public Integer getEafusId() {
        return eafusId;
    }

    public void setEafusId(Integer eafusId) {
        this.eafusId = eafusId;
    }

    @Column(name = "dfc_name")
    public String getDfcName() {
        return dfcName;
    }

    public void setDfcName(String dfcName) {
        this.dfcName = dfcName;
    }

    @Column(name = "compound_source", nullable = false)
    public String getCompoundSource() {
        return compoundSource;
    }

    public void setCompoundSource(String compoundSource) {
        this.compoundSource = compoundSource;
    }

    public String getMetabolism() {
        return metabolism;
    }

    public void setMetabolism(String metabolism) {
        this.metabolism = metabolism;
    }

    @Column(name = "synthesis_citations")
    public String getSynthesisCitations() {
        return synthesisCitations;
    }

    public void setSynthesisCitations(String synthesisCitations) {
        this.synthesisCitations = synthesisCitations;
    }

    @Column(name = "general_citations")
    public String getGeneralCitations() {
        return generalCitations;
    }

    public void setGeneralCitations(String generalCitations) {
        this.generalCitations = generalCitations;
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
        Nutrient nutrient = (Nutrient) o;
        return Objects.equals(id, nutrient.id)
                && Objects.equals(legacyId, nutrient.legacyId)
                && Objects.equals(type, nutrient.type)
                && Objects.equals(publicId, nutrient.publicId)
                && Objects.equals(name, nutrient.name)
                && Objects.equals(export, nutrient.export)
                && Objects.equals(state, nutrient.state)
                && Objects.equals(annotationQuality, nutrient.annotationQuality)
                && Objects.equals(description, nutrient.description)
                && Objects.equals(wikipediaId, nutrient.wikipediaId)
                && Objects.equals(comments, nutrient.comments)
                && Objects.equals(dfcId, nutrient.dfcId)
                && Objects.equals(dukeId, nutrient.dukeId)
                && Objects.equals(eafusId, nutrient.eafusId)
                && Objects.equals(dfcName, nutrient.dfcName)
                && Objects.equals(compoundSource, nutrient.compoundSource)
                && Objects.equals(metabolism, nutrient.metabolism)
                && Objects.equals(synthesisCitations, nutrient.synthesisCitations)
                && Objects.equals(generalCitations, nutrient.generalCitations)
                && Objects.equals(creatorId, nutrient.creatorId)
                && Objects.equals(updaterId, nutrient.updaterId)
                && Objects.equals(createdAt, nutrient.createdAt)
                && Objects.equals(updatedAt, nutrient.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, legacyId, type, publicId, name, export, state, annotationQuality,
                description, wikipediaId, comments, dfcId, dukeId, eafusId, dfcName,
                compoundSource, metabolism, synthesisCitations, generalCitations,
                creatorId, updaterId, createdAt, updatedAt
        );
    }
}
