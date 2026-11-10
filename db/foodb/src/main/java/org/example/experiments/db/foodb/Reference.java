package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Reference {
    private Integer id;
    private String refType;
    private String text;
    private String pubmedId;
    private String link;
    private String title;
    private Integer creatorId;
    private Integer updaterId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer sourceId;
    private String sourceType;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ref_type")
    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "pubmed_id")
    public String getPubmedId() {
        return pubmedId;
    }

    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Column(name = "source_id")
    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Column(name = "source_type")
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
        Reference reference = (Reference) o;
        return Objects.equals(id, reference.id)
                && Objects.equals(refType, reference.refType)
                && Objects.equals(text, reference.text)
                && Objects.equals(pubmedId, reference.pubmedId)
                && Objects.equals(link, reference.link)
                && Objects.equals(title, reference.title)
                && Objects.equals(creatorId, reference.creatorId)
                && Objects.equals(updaterId, reference.updaterId)
                && Objects.equals(createdAt, reference.createdAt)
                && Objects.equals(updatedAt, reference.updatedAt)
                && Objects.equals(sourceId, reference.sourceId)
                && Objects.equals(sourceType, reference.sourceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, refType, text, pubmedId, link, title, creatorId, updaterId,
                createdAt, updatedAt, sourceId, sourceType
        );
    }
}
