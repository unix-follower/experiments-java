package org.example.experiments.db.foodb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Compound {
    private Integer id;
    private String publicId;
    private String name;
    private String moldbIupac;
    private String state;
    private String annotationQuality;
    private String description;
    private String casNumber;
    private String moldbInchKey;
    private String moldbInchi;
    private String moldbSmiles;
    private String moldbMonoMass;
    private String kingdom;
    private String superKlass;
    private String klass;
    private String subKlass;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "public_id", nullable = false)
    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "moldb_iupac")
    public String getMoldbIupac() {
        return moldbIupac;
    }

    public void setMoldbIupac(String moldbIupac) {
        this.moldbIupac = moldbIupac;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "annotation_quality")
    public String getAnnotationQuality() {
        return annotationQuality;
    }

    public void setAnnotationQuality(String annotationQuality) {
        this.annotationQuality = annotationQuality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "cas_number")
    public String getCasNumber() {
        return casNumber;
    }

    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    @Column(name = "moldb_inchikey")
    public String getMoldbInchKey() {
        return moldbInchKey;
    }

    public void setMoldbInchKey(String moldbInchKey) {
        this.moldbInchKey = moldbInchKey;
    }

    @Column(name = "moldb_inchi")
    public String getMoldbInchi() {
        return moldbInchi;
    }

    public void setMoldbInchi(String moldbInchi) {
        this.moldbInchi = moldbInchi;
    }

    @Column(name = "moldb_smiles")
    public String getMoldbSmiles() {
        return moldbSmiles;
    }

    public void setMoldbSmiles(String moldbSmiles) {
        this.moldbSmiles = moldbSmiles;
    }

    @Column(name = "moldb_mono_mass")
    public String getMoldbMonoMass() {
        return moldbMonoMass;
    }

    public void setMoldbMonoMass(String moldbMonoMass) {
        this.moldbMonoMass = moldbMonoMass;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    @Column(name = "superklass")
    public String getSuperKlass() {
        return superKlass;
    }

    public void setSuperKlass(String superKlass) {
        this.superKlass = superKlass;
    }

    public String getKlass() {
        return klass;
    }

    public void setKlass(String klass) {
        this.klass = klass;
    }

    @Column(name = "subklass")
    public String getSubKlass() {
        return subKlass;
    }

    public void setSubKlass(String subKlass) {
        this.subKlass = subKlass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Compound compound = (Compound) o;
        return Objects.equals(id, compound.id)
                && Objects.equals(publicId, compound.publicId)
                && Objects.equals(name, compound.name)
                && Objects.equals(moldbIupac, compound.moldbIupac)
                && Objects.equals(state, compound.state)
                && Objects.equals(annotationQuality, compound.annotationQuality)
                && Objects.equals(description, compound.description)
                && Objects.equals(casNumber, compound.casNumber)
                && Objects.equals(moldbInchKey, compound.moldbInchKey)
                && Objects.equals(moldbInchi, compound.moldbInchi)
                && Objects.equals(moldbSmiles, compound.moldbSmiles)
                && Objects.equals(moldbMonoMass, compound.moldbMonoMass)
                && Objects.equals(kingdom, compound.kingdom)
                && Objects.equals(superKlass, compound.superKlass)
                && Objects.equals(klass, compound.klass)
                && Objects.equals(subKlass, compound.subKlass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, publicId, name, moldbIupac, state, annotationQuality,
                description, casNumber, moldbInchKey, moldbInchi, moldbSmiles,
                moldbMonoMass, kingdom, superKlass, klass, subKlass
        );
    }
}
