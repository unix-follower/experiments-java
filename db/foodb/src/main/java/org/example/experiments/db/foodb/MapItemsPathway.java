package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "map_items_pathway")
public class MapItemsPathway {
    private Integer id;
    private String mapItemId;
    private String mapItemType;
    private Integer pathwayId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "map_item_id")
    public String getMapItemId() {
        return mapItemId;
    }

    public void setMapItemId(String mapItemId) {
        this.mapItemId = mapItemId;
    }

    @Column(name = "map_item_type")
    public String getMapItemType() {
        return mapItemType;
    }

    public void setMapItemType(String mapItemType) {
        this.mapItemType = mapItemType;
    }

    @Column(name = "pathway_id")
    public Integer getPathwayId() {
        return pathwayId;
    }

    public void setPathwayId(Integer pathwayId) {
        this.pathwayId = pathwayId;
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
        MapItemsPathway that = (MapItemsPathway) o;
        return Objects.equals(id, that.id)
                && Objects.equals(mapItemId, that.mapItemId)
                && Objects.equals(mapItemType, that.mapItemType)
                && Objects.equals(pathwayId, that.pathwayId)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mapItemId, mapItemType, pathwayId, createdAt, updatedAt);
    }
}
