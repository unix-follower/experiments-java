package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Enzyme {
    private Integer id;
    private String name;
    private String geneName;
    private String description;
    private String goClassification;
    private String generalFunction;
    private String specificFunction;
    private String pathway;
    private String reaction;
    private String cellularLocation;
    private String signals;
    private String transmembraneRegions;
    private Double molecularWeight;
    private String theoreticalPi;
    private String locus;
    private String chromosome;
    private String uniprotName;
    private String uniprotId;
    private String pdbId;
    private String genbankProteinId;
    private String genbankGeneId;
    private String geneCardId;
    private String genAtlasId;
    private String hgncId;
    private String hprdId;
    private String organism;
    private String generalCitations;
    private String comments;
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

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "gene_name", nullable = false)
    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "go_classification")
    public String getGoClassification() {
        return goClassification;
    }

    public void setGoClassification(String goClassification) {
        this.goClassification = goClassification;
    }

    @Column(name = "general_function")
    public String getGeneralFunction() {
        return generalFunction;
    }

    public void setGeneralFunction(String generalFunction) {
        this.generalFunction = generalFunction;
    }

    @Column(name = "specific_function")
    public String getSpecificFunction() {
        return specificFunction;
    }

    public void setSpecificFunction(String specificFunction) {
        this.specificFunction = specificFunction;
    }

    public String getPathway() {
        return pathway;
    }

    public void setPathway(String pathway) {
        this.pathway = pathway;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    @Column(name = "cellular_location")
    public String getCellularLocation() {
        return cellularLocation;
    }

    public void setCellularLocation(String cellularLocation) {
        this.cellularLocation = cellularLocation;
    }

    public String getSignals() {
        return signals;
    }

    public void setSignals(String signals) {
        this.signals = signals;
    }

    @Column(name = "transmembrane_regions")
    public String getTransmembraneRegions() {
        return transmembraneRegions;
    }

    public void setTransmembraneRegions(String transmembraneRegions) {
        this.transmembraneRegions = transmembraneRegions;
    }

    @Column(name = "molecular_weight")
    public Double getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(Double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    @Column(name = "theoretical_pi")
    public String getTheoreticalPi() {
        return theoreticalPi;
    }

    public void setTheoreticalPi(String theoreticalPi) {
        this.theoreticalPi = theoreticalPi;
    }

    public String getLocus() {
        return locus;
    }

    public void setLocus(String locus) {
        this.locus = locus;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    @Column(name = "uniprot_name")
    public String getUniprotName() {
        return uniprotName;
    }

    public void setUniprotName(String uniprotName) {
        this.uniprotName = uniprotName;
    }

    @Column(name = "uniprot_id", nullable = false)
    public String getUniprotId() {
        return uniprotId;
    }

    public void setUniprotId(String uniprotId) {
        this.uniprotId = uniprotId;
    }

    @Column(name = "pdb_id")
    public String getPdbId() {
        return pdbId;
    }

    public void setPdbId(String pdbId) {
        this.pdbId = pdbId;
    }

    @Column(name = "genbank_protein_id")
    public String getGenbankProteinId() {
        return genbankProteinId;
    }

    public void setGenbankProteinId(String genbankProteinId) {
        this.genbankProteinId = genbankProteinId;
    }

    @Column(name = "genbank_gene_id")
    public String getGenbankGeneId() {
        return genbankGeneId;
    }

    public void setGenbankGeneId(String genbankGeneId) {
        this.genbankGeneId = genbankGeneId;
    }

    @Column(name = "genecard_id")
    public String getGeneCardId() {
        return geneCardId;
    }

    public void setGeneCardId(String geneCardId) {
        this.geneCardId = geneCardId;
    }

    @Column(name = "genatlas_id")
    public String getGenAtlasId() {
        return genAtlasId;
    }

    public void setGenAtlasId(String genAtlasId) {
        this.genAtlasId = genAtlasId;
    }

    @Column(name = "hgnc_id")
    public String getHgncId() {
        return hgncId;
    }

    public void setHgncId(String hgncId) {
        this.hgncId = hgncId;
    }

    @Column(name = "hprd_id")
    public String getHprdId() {
        return hprdId;
    }

    public void setHprdId(String hprdId) {
        this.hprdId = hprdId;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    @Column(name = "general_citations")
    public String getGeneralCitations() {
        return generalCitations;
    }

    public void setGeneralCitations(String generalCitations) {
        this.generalCitations = generalCitations;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
        Enzyme enzyme = (Enzyme) o;
        return Objects.equals(id, enzyme.id)
                && Objects.equals(name, enzyme.name)
                && Objects.equals(geneName, enzyme.geneName)
                && Objects.equals(description, enzyme.description)
                && Objects.equals(goClassification, enzyme.goClassification)
                && Objects.equals(generalFunction, enzyme.generalFunction)
                && Objects.equals(specificFunction, enzyme.specificFunction)
                && Objects.equals(pathway, enzyme.pathway)
                && Objects.equals(reaction, enzyme.reaction)
                && Objects.equals(cellularLocation, enzyme.cellularLocation)
                && Objects.equals(signals, enzyme.signals)
                && Objects.equals(transmembraneRegions, enzyme.transmembraneRegions)
                && Objects.equals(molecularWeight, enzyme.molecularWeight)
                && Objects.equals(theoreticalPi, enzyme.theoreticalPi)
                && Objects.equals(locus, enzyme.locus)
                && Objects.equals(chromosome, enzyme.chromosome)
                && Objects.equals(uniprotName, enzyme.uniprotName)
                && Objects.equals(uniprotId, enzyme.uniprotId)
                && Objects.equals(pdbId, enzyme.pdbId)
                && Objects.equals(genbankProteinId, enzyme.genbankProteinId)
                && Objects.equals(genbankGeneId, enzyme.genbankGeneId)
                && Objects.equals(geneCardId, enzyme.geneCardId)
                && Objects.equals(genAtlasId, enzyme.genAtlasId)
                && Objects.equals(hgncId, enzyme.hgncId)
                && Objects.equals(hprdId, enzyme.hprdId)
                && Objects.equals(organism, enzyme.organism)
                && Objects.equals(generalCitations, enzyme.generalCitations)
                && Objects.equals(comments, enzyme.comments)
                && Objects.equals(creatorId, enzyme.creatorId)
                && Objects.equals(updaterId, enzyme.updaterId)
                && Objects.equals(createdAt, enzyme.createdAt)
                && Objects.equals(updatedAt, enzyme.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, name, geneName, description, goClassification, generalFunction,
                specificFunction, pathway, reaction, cellularLocation, signals,
                transmembraneRegions, molecularWeight, theoreticalPi, locus,
                chromosome, uniprotName, uniprotId, pdbId, genbankProteinId,
                genbankGeneId, geneCardId, genAtlasId, hgncId, hprdId, organism,
                generalCitations, comments, creatorId, updaterId, createdAt,
                updatedAt
        );
    }
}
