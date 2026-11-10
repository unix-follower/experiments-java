package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "pfam_membership")
public class PfamMembership {
    private Integer id;
    private String enzymeId;
    private String pfamId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "enzyme_id")
    public String getEnzymeId() {
        return enzymeId;
    }

    public void setEnzymeId(String enzymeId) {
        this.enzymeId = enzymeId;
    }

    @Column(name = "pfam_id")
    public String getPfamId() {
        return pfamId;
    }

    public void setPfamId(String pfamId) {
        this.pfamId = pfamId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PfamMembership that = (PfamMembership) o;
        return Objects.equals(id, that.id)
                && Objects.equals(enzymeId, that.enzymeId)
                && Objects.equals(pfamId, that.pfamId)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enzymeId, pfamId, createdAt, updatedAt);
    }
}
