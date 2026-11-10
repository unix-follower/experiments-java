package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Pathway {
    private Integer id;
    private String smpdbId;
    private String keggMapId;
    private String name;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "smpdb_id", nullable = false)
    public String getSmpdbId() {
        return smpdbId;
    }

    public void setSmpdbId(String smpdbId) {
        this.smpdbId = smpdbId;
    }

    @Column(name = "kegg_map_id", nullable = false)
    public String getKeggMapId() {
        return keggMapId;
    }

    public void setKeggMapId(String keggMapId) {
        this.keggMapId = keggMapId;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        Pathway pathway = (Pathway) o;
        return Objects.equals(id, pathway.id)
                && Objects.equals(smpdbId, pathway.smpdbId)
                && Objects.equals(keggMapId, pathway.keggMapId)
                && Objects.equals(name, pathway.name)
                && Objects.equals(createdAt, pathway.createdAt)
                && Objects.equals(updatedAt, pathway.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, smpdbId, keggMapId, name, createdAt, updatedAt);
    }
}
