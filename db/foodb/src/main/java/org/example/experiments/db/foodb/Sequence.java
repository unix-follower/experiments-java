package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Sequence {
    private Integer id;
    private String header;
    private String chain;
    private String sequenceableId;
    private String sequenceableType;
    private String type;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    @Column(name = "sequenceable_id")
    public String getSequenceableId() {
        return sequenceableId;
    }

    public void setSequenceableId(String sequenceableId) {
        this.sequenceableId = sequenceableId;
    }

    @Column(name = "sequenceable_type")
    public String getSequenceableType() {
        return sequenceableType;
    }

    public void setSequenceableType(String sequenceableType) {
        this.sequenceableType = sequenceableType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        Sequence sequence = (Sequence) o;
        return Objects.equals(id, sequence.id)
                && Objects.equals(header, sequence.header)
                && Objects.equals(chain, sequence.chain)
                && Objects.equals(sequenceableId, sequence.sequenceableId)
                && Objects.equals(sequenceableType, sequence.sequenceableType)
                && Objects.equals(type, sequence.type)
                && Objects.equals(createdAt, sequence.createdAt)
                && Objects.equals(updatedAt, sequence.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, header, chain, sequenceableId, sequenceableType, type, createdAt, updatedAt);
    }
}
