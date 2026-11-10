package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "compounds_health_effect")
public class CompoundsHealthEffect {
    private Integer id;
    private Integer compoundId;
    private Integer healthEffectId;
    private String origHealthEffectName;
    private String origCompoundName;
    private String origCitation;
    private String citation;
    private String citationType;
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

    @Column(name = "health_effect_id", nullable = false)
    public Integer getHealthEffectId() {
        return healthEffectId;
    }

    public void setHealthEffectId(Integer healthEffectId) {
        this.healthEffectId = healthEffectId;
    }

    @Column(name = "orig_health_effect_name", nullable = false)
    public String getOrigHealthEffectName() {
        return origHealthEffectName;
    }

    public void setOrigHealthEffectName(String origHealthEffectName) {
        this.origHealthEffectName = origHealthEffectName;
    }

    @Column(name = "orig_compound_name", nullable = false)
    public String getOrigCompoundName() {
        return origCompoundName;
    }

    public void setOrigCompoundName(String origCompoundName) {
        this.origCompoundName = origCompoundName;
    }

    @Column(name = "orig_citation")
    public String getOrigCitation() {
        return origCitation;
    }

    public void setOrigCitation(String origCitation) {
        this.origCitation = origCitation;
    }

    @Column(nullable = false)
    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    @Column(name = "citation_type", nullable = false)
    public String getCitationType() {
        return citationType;
    }

    public void setCitationType(String citationType) {
        this.citationType = citationType;
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
        CompoundsHealthEffect that = (CompoundsHealthEffect) o;
        return Objects.equals(id, that.id)
                && Objects.equals(compoundId, that.compoundId)
                && Objects.equals(healthEffectId, that.healthEffectId)
                && Objects.equals(origHealthEffectName, that.origHealthEffectName)
                && Objects.equals(origCompoundName, that.origCompoundName)
                && Objects.equals(origCitation, that.origCitation)
                && Objects.equals(citation, that.citation)
                && Objects.equals(citationType, that.citationType)
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
                id, compoundId, healthEffectId, origHealthEffectName, origCompoundName,
                origCitation, citation, citationType, createdAt, updatedAt,
                creatorId, updaterId, sourceId, sourceType
        );
    }
}
