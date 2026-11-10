package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Content {
    private Integer id;
    private Integer sourceId;
    private String sourceType;
    private Integer foodId;
    private Integer origFoodId;
    private String origFoodCommonName;
    private String origFoodScientificName;
    private String origFoodPart;
    private String origSourceId;
    private String origSourceName;
    private Double origContent;
    private Double origMin;
    private Double origMax;
    private String origUnit;
    private String origCitation;
    private String citation;
    private String citationType;
    private Integer creatorId;
    private Integer updaterId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String origMethod;
    private String origUnitExpression;
    private Double standardContent;
    private String preparationType;
    private Integer export;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Column(name = "food_id", nullable = false)
    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    @Column(name = "orig_food_id", nullable = false)
    public Integer getOrigFoodId() {
        return origFoodId;
    }

    public void setOrigFoodId(Integer origFoodId) {
        this.origFoodId = origFoodId;
    }

    @Column(name = "orig_food_common_name", nullable = false)
    public String getOrigFoodCommonName() {
        return origFoodCommonName;
    }

    public void setOrigFoodCommonName(String origFoodCommonName) {
        this.origFoodCommonName = origFoodCommonName;
    }

    @Column(name = "orig_food_scientific_name", nullable = false)
    public String getOrigFoodScientificName() {
        return origFoodScientificName;
    }

    public void setOrigFoodScientificName(String origFoodScientificName) {
        this.origFoodScientificName = origFoodScientificName;
    }

    @Column(name = "orig_food_part", nullable = false)
    public String getOrigFoodPart() {
        return origFoodPart;
    }

    public void setOrigFoodPart(String origFoodPart) {
        this.origFoodPart = origFoodPart;
    }

    @Column(name = "orig_source_id", nullable = false)
    public String getOrigSourceId() {
        return origSourceId;
    }

    public void setOrigSourceId(String origSourceId) {
        this.origSourceId = origSourceId;
    }

    @Column(name = "orig_source_name", nullable = false)
    public String getOrigSourceName() {
        return origSourceName;
    }

    public void setOrigSourceName(String origSourceName) {
        this.origSourceName = origSourceName;
    }

    @Column(name = "orig_content")
    public Double getOrigContent() {
        return origContent;
    }

    public void setOrigContent(Double origContent) {
        this.origContent = origContent;
    }

    @Column(name = "orig_min")
    public Double getOrigMin() {
        return origMin;
    }

    public void setOrigMin(Double origMin) {
        this.origMin = origMin;
    }

    @Column(name = "orig_max")
    public Double getOrigMax() {
        return origMax;
    }

    public void setOrigMax(Double origMax) {
        this.origMax = origMax;
    }

    @Column(name = "orig_unit")
    public String getOrigUnit() {
        return origUnit;
    }

    public void setOrigUnit(String origUnit) {
        this.origUnit = origUnit;
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

    @Column(name = "created_at")
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "updated_at")
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Column(name = "orig_method")
    public String getOrigMethod() {
        return origMethod;
    }

    public void setOrigMethod(String origMethod) {
        this.origMethod = origMethod;
    }

    @Column(name = "orig_unit_expression")
    public String getOrigUnitExpression() {
        return origUnitExpression;
    }

    public void setOrigUnitExpression(String origUnitExpression) {
        this.origUnitExpression = origUnitExpression;
    }

    @Column(name = "standard_content")
    public Double getStandardContent() {
        return standardContent;
    }

    public void setStandardContent(Double standardContent) {
        this.standardContent = standardContent;
    }

    @Column(name = "preparation_type", nullable = false)
    public String getPreparationType() {
        return preparationType;
    }

    public void setPreparationType(String preparationType) {
        this.preparationType = preparationType;
    }

    @Column(nullable = false)
    public Integer getExport() {
        return export;
    }

    public void setExport(Integer export) {
        this.export = export;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Content content = (Content) o;
        return Objects.equals(id, content.id)
                && Objects.equals(sourceId, content.sourceId)
                && Objects.equals(sourceType, content.sourceType)
                && Objects.equals(foodId, content.foodId)
                && Objects.equals(origFoodId, content.origFoodId)
                && Objects.equals(origFoodCommonName, content.origFoodCommonName)
                && Objects.equals(origFoodScientificName, content.origFoodScientificName)
                && Objects.equals(origFoodPart, content.origFoodPart)
                && Objects.equals(origSourceId, content.origSourceId)
                && Objects.equals(origSourceName, content.origSourceName)
                && Objects.equals(origContent, content.origContent)
                && Objects.equals(origMin, content.origMin)
                && Objects.equals(origMax, content.origMax)
                && Objects.equals(origUnit, content.origUnit)
                && Objects.equals(origCitation, content.origCitation)
                && Objects.equals(citation, content.citation)
                && Objects.equals(citationType, content.citationType)
                && Objects.equals(creatorId, content.creatorId)
                && Objects.equals(updaterId, content.updaterId)
                && Objects.equals(createdAt, content.createdAt)
                && Objects.equals(updatedAt, content.updatedAt)
                && Objects.equals(origMethod, content.origMethod)
                && Objects.equals(origUnitExpression, content.origUnitExpression)
                && Objects.equals(standardContent, content.standardContent)
                && Objects.equals(preparationType, content.preparationType)
                && Objects.equals(export, content.export);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, sourceId, sourceType, foodId, origFoodId, origFoodCommonName,
                origFoodScientificName, origFoodPart, origSourceId, origSourceName,
                origContent, origMin, origMax, origUnit, origCitation, citation,
                citationType, creatorId, updaterId, createdAt, updatedAt,
                origMethod, origUnitExpression, standardContent, preparationType,
                export
        );
    }
}
