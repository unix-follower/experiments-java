package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "food_taxonomy")
public class FoodTaxonomy {
    private Integer id;
    private Integer foodId;
    private Integer ncbiTaxonomyId;
    private String classificationName;
    private Integer classificationOrder;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "food_id", nullable = false)
    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    @Column(name = "ncbi_taxonomy_id", nullable = false)
    public Integer getNcbiTaxonomyId() {
        return ncbiTaxonomyId;
    }

    public void setNcbiTaxonomyId(Integer ncbiTaxonomyId) {
        this.ncbiTaxonomyId = ncbiTaxonomyId;
    }

    @Column(name = "classification_name", nullable = false)
    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    @Column(name = "classification_order", nullable = false)
    public Integer getClassificationOrder() {
        return classificationOrder;
    }

    public void setClassificationOrder(Integer classificationOrder) {
        this.classificationOrder = classificationOrder;
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
        FoodTaxonomy that = (FoodTaxonomy) o;
        return Objects.equals(id, that.id)
                && Objects.equals(foodId, that.foodId)
                && Objects.equals(ncbiTaxonomyId, that.ncbiTaxonomyId)
                && Objects.equals(classificationName, that.classificationName)
                && Objects.equals(classificationOrder, that.classificationOrder)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foodId, ncbiTaxonomyId, classificationName, classificationOrder, createdAt, updatedAt);
    }
}
