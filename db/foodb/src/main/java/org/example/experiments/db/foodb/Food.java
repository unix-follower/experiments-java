package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Food {
    private Integer id;
    private String name;
    private String nameScientific;
    private String description;
    private Integer itisId;
    private String wikipediaId;
    private String pictureFileName;
    private String pictureContentType;
    private Integer pictureFileSize;
    private OffsetDateTime pictureUpdatedAt;
    private Integer legacyId;
    private String foodGroup;
    private String foodSubgroup;
    private String foodType;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer creatorId;
    private Integer updaterId;
    private Boolean exportToAfcDb;
    private String category;
    private Integer ncbiTaxonomyId;
    private Boolean exportToFoodb;
    private String publicId;

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

    @Column(name = "name_scientific")
    public String getNameScientific() {
        return nameScientific;
    }

    public void setNameScientific(String nameScientific) {
        this.nameScientific = nameScientific;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "itis_id")
    public Integer getItisId() {
        return itisId;
    }

    public void setItisId(Integer itisId) {
        this.itisId = itisId;
    }

    @Column(name = "wikipedia_id")
    public String getWikipediaId() {
        return wikipediaId;
    }

    public void setWikipediaId(String wikipediaId) {
        this.wikipediaId = wikipediaId;
    }

    @Column(name = "picture_file_name")
    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

    @Column(name = "picture_content_type")
    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    @Column(name = "picture_file_size")
    public Integer getPictureFileSize() {
        return pictureFileSize;
    }

    public void setPictureFileSize(Integer pictureFileSize) {
        this.pictureFileSize = pictureFileSize;
    }

    @Column(name = "picture_updated_at")
    public OffsetDateTime getPictureUpdatedAt() {
        return pictureUpdatedAt;
    }

    public void setPictureUpdatedAt(OffsetDateTime pictureUpdatedAt) {
        this.pictureUpdatedAt = pictureUpdatedAt;
    }

    @Column(name = "legacy_id")
    public Integer getLegacyId() {
        return legacyId;
    }

    public void setLegacyId(Integer legacyId) {
        this.legacyId = legacyId;
    }

    @Column(name = "food_group")
    public String getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(String foodGroup) {
        this.foodGroup = foodGroup;
    }

    @Column(name = "food_subgroup")
    public String getFoodSubgroup() {
        return foodSubgroup;
    }

    public void setFoodSubgroup(String foodSubgroup) {
        this.foodSubgroup = foodSubgroup;
    }

    @Column(name = "food_type", nullable = false)
    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
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

    @Column(name = "export_to_afcdb", nullable = false)
    public Boolean getExportToAfcDb() {
        return exportToAfcDb;
    }

    public void setExportToAfcDb(Boolean exportToAfcDb) {
        this.exportToAfcDb = exportToAfcDb;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "ncbi_taxonomy_id")
    public Integer getNcbiTaxonomyId() {
        return ncbiTaxonomyId;
    }

    public void setNcbiTaxonomyId(Integer ncbiTaxonomyId) {
        this.ncbiTaxonomyId = ncbiTaxonomyId;
    }

    @Column(name = "export_to_foodb", nullable = false)
    public Boolean getExportToFoodb() {
        return exportToFoodb;
    }

    public void setExportToFoodb(Boolean exportToFoodb) {
        this.exportToFoodb = exportToFoodb;
    }

    @Column(name = "public_id", nullable = false)
    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Food food = (Food) o;
        return Objects.equals(id, food.id)
                && Objects.equals(name, food.name)
                && Objects.equals(nameScientific, food.nameScientific)
                && Objects.equals(description, food.description)
                && Objects.equals(itisId, food.itisId)
                && Objects.equals(wikipediaId, food.wikipediaId)
                && Objects.equals(pictureFileName, food.pictureFileName)
                && Objects.equals(pictureContentType, food.pictureContentType)
                && Objects.equals(pictureFileSize, food.pictureFileSize)
                && Objects.equals(pictureUpdatedAt, food.pictureUpdatedAt)
                && Objects.equals(legacyId, food.legacyId)
                && Objects.equals(foodGroup, food.foodGroup)
                && Objects.equals(foodSubgroup, food.foodSubgroup)
                && Objects.equals(foodType, food.foodType)
                && Objects.equals(createdAt, food.createdAt)
                && Objects.equals(updatedAt, food.updatedAt)
                && Objects.equals(creatorId, food.creatorId)
                && Objects.equals(updaterId, food.updaterId)
                && Objects.equals(exportToAfcDb, food.exportToAfcDb)
                && Objects.equals(category, food.category)
                && Objects.equals(ncbiTaxonomyId, food.ncbiTaxonomyId)
                && Objects.equals(exportToFoodb, food.exportToFoodb)
                && Objects.equals(publicId, food.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, name, nameScientific, description, itisId, wikipediaId,
                pictureFileName, pictureContentType, pictureFileSize, pictureUpdatedAt,
                legacyId, foodGroup, foodSubgroup, foodType, createdAt, updatedAt,
                creatorId, updaterId, exportToAfcDb, category, ncbiTaxonomyId,
                exportToFoodb, publicId
        );
    }
}
