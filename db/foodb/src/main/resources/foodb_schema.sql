-- =====================================================================================
-- Load order
--   1. Run this file.
--   2. COPY the CSVs (parents first: compound, food, nutrient, enzyme, flavor, health_effect, pathway,
--      ontology_term ... then children).
--   3. CALL pr_add_post_load_foreign_keys();   -- FKs that the export violates (orphans), added NOT VALID
--   4. CALL pr_refresh_materialized_views();
--
-- DEVIATIONS from the raw files / JPA model
--     COPY with an explicit column list in the *file* order, e.g.
--       COPY compound (id, public_id, name, state, annotation_quality, description, cas_number, moldb_smiles,
--                      moldb_inchi, moldb_mono_mass, moldb_inchikey, moldb_iupac, kingdom, superklass, klass, subklass)
--       FROM 'Compound.csv' WITH (FORMAT csv, HEADER true);
--   * ncbi_taxonomy_map: CSV header is 'TaxonomyName,Rank'; columns are taxonomy_name, rank here.
--   * ontology_synonym: CSV header typo 'external_srouce' -> external_source.
--   * content.orig_food_id is varchar (values like 'WI4600'), JPA has Integer.
--   * No FK for food.ncbi_taxonomy_id / food_taxonomy.ncbi_taxonomy_id: they are real NCBI tax ids, not
--     ncbi_taxonomy_map.id (0 of 1508 values match).
--   * Polymorphic source_id/source_type pairs cannot have a FK; guarded by CHECK + composite index.
-- =====================================================================================

CREATE
EXTENSION IF NOT EXISTS pg_trgm;

-- -------------------------------------------------------------------------------------
-- Generic helpers
-- -------------------------------------------------------------------------------------
CREATE
OR REPLACE FUNCTION fn_set_updated_at() RETURNS trigger
    LANGUAGE plpgsql AS
$$
BEGIN
    NEW.updated_at
:= now();
RETURN NEW;
END;
$$;

-- -------------------------------------------------------------------------------------
-- Reference / dictionary tables
-- -------------------------------------------------------------------------------------
CREATE TABLE compound
(
    id                 integer PRIMARY KEY,
    public_id          varchar(16)  NOT NULL,
    name               varchar(255) NOT NULL,
    state              varchar(16),
    annotation_quality varchar(16),
    description        text,
    cas_number         varchar(32),
    moldb_smiles       text,
    moldb_inchi        text,
    moldb_mono_mass    numeric(18, 9),
    moldb_inchikey     varchar(27),
    moldb_iupac        text,
    kingdom            varchar(64),
    superklass         varchar(128),
    klass              varchar(128),
    subklass           varchar(128),
    CONSTRAINT uq_compound_public_id UNIQUE (public_id),
    CONSTRAINT ck_compound_state CHECK (state IS NULL OR lower(state) IN ('solid', 'liquid', 'gas'))
);
COMMENT
ON TABLE compound IS 'Chemical compounds.';
CREATE INDEX ix_compound_name_trgm ON compound USING gin (name gin_trgm_ops);
CREATE INDEX ix_compound_lower_name ON compound (lower(name));
CREATE INDEX ix_compound_inchikey ON compound (moldb_inchikey) WHERE moldb_inchikey IS NOT NULL;
CREATE INDEX ix_compound_cas_number ON compound (cas_number) WHERE cas_number IS NOT NULL;
CREATE INDEX ix_compound_taxonomy ON compound (kingdom, superklass, klass, subklass) WHERE kingdom IS NOT NULL;

CREATE TABLE nutrient
(
    id                  integer PRIMARY KEY,
    legacy_id           integer,
    type                varchar(64),
    public_id           varchar(16)  NOT NULL,
    name                varchar(128) NOT NULL,
    export              boolean      NOT NULL DEFAULT false,
    state               varchar(16),
    annotation_quality  varchar(16),
    description         text,
    wikipedia_id        varchar(128),
    comments            text,
    dfc_id              varchar(64),
    duke_id             varchar(64),
    eafus_id            integer,
    dfc_name            varchar(128),
    compound_source     varchar(32),
    metabolism          text,
    synthesis_citations text,
    general_citations   text,
    creator_id          integer,
    updater_id          integer,
    created_at          timestamptz  NOT NULL DEFAULT now(),
    updated_at          timestamptz  NOT NULL DEFAULT now(),
    CONSTRAINT uq_nutrient_public_id UNIQUE (public_id)
);
CREATE INDEX ix_nutrient_name ON nutrient (name);
CREATE INDEX ix_nutrient_legacy_id ON nutrient (legacy_id) WHERE legacy_id IS NOT NULL;

CREATE TABLE food
(
    id                   integer PRIMARY KEY,
    name                 varchar(255) NOT NULL,
    name_scientific      varchar(255),
    description          text,
    itis_id              integer,
    wikipedia_id         varchar(255),
    picture_file_name    varchar(255),
    picture_content_type varchar(64),
    picture_file_size    integer,
    picture_updated_at   timestamptz,
    legacy_id            integer,
    food_group           varchar(64),
    food_subgroup        varchar(64),
    food_type            varchar(16)  NOT NULL,
    created_at           timestamptz  NOT NULL DEFAULT now(),
    updated_at           timestamptz  NOT NULL DEFAULT now(),
    creator_id           integer,
    updater_id           integer,
    export_to_afcdb      boolean      NOT NULL DEFAULT false,
    category             varchar(16),
    ncbi_taxonomy_id     integer,
    export_to_foodb      boolean      NOT NULL DEFAULT false,
    public_id            varchar(16)  NOT NULL,
    CONSTRAINT uq_food_public_id UNIQUE (public_id),
    CONSTRAINT uq_food_name UNIQUE (name),
    CONSTRAINT ck_food_category CHECK (category IS NULL OR category IN ('generic', 'specific'))
);
CREATE INDEX ix_food_name_trgm ON food USING gin (name gin_trgm_ops);
CREATE INDEX ix_food_group ON food (food_group, food_subgroup);
CREATE INDEX ix_food_ncbi_taxonomy_id ON food (ncbi_taxonomy_id) WHERE ncbi_taxonomy_id IS NOT NULL;
CREATE INDEX ix_food_legacy_id ON food (legacy_id) WHERE legacy_id IS NOT NULL;

CREATE TABLE flavor
(
    id           integer PRIMARY KEY,
    name         varchar(64) NOT NULL,
    flavor_group varchar(32),
    category     varchar(16) NOT NULL,
    created_at   timestamptz NOT NULL DEFAULT now(),
    updated_at   timestamptz NOT NULL DEFAULT now(),
    creator_id   integer,
    updater_id   integer,
    CONSTRAINT uq_flavor_name UNIQUE (name)
);

CREATE TABLE health_effect
(
    id               integer PRIMARY KEY,
    name             varchar(128) NOT NULL,
    description      text,
    chebi_name       varchar(128),
    chebi_id         integer,
    created_at       timestamptz  NOT NULL DEFAULT now(),
    updated_at       timestamptz  NOT NULL DEFAULT now(),
    creator_id       integer,
    updater_id       integer,
    chebi_definition text,
    CONSTRAINT uq_health_effect_name UNIQUE (name)
);
CREATE INDEX ix_health_effect_chebi_id ON health_effect (chebi_id) WHERE chebi_id IS NOT NULL;

CREATE TABLE pathway
(
    id          integer PRIMARY KEY,
    smpdb_id    varchar(16)  NOT NULL,
    kegg_map_id varchar(16),
    name        varchar(255) NOT NULL,
    created_at  timestamptz  NOT NULL DEFAULT now(),
    updated_at  timestamptz  NOT NULL DEFAULT now(),
    CONSTRAINT uq_pathway_smpdb_id UNIQUE (smpdb_id)
);
CREATE INDEX ix_pathway_kegg_map_id ON pathway (kegg_map_id) WHERE kegg_map_id IS NOT NULL;

CREATE TABLE enzyme
(
    id                    integer PRIMARY KEY,
    name                  varchar(255) NOT NULL,
    gene_name             varchar(64),
    description           text,
    go_classification     text,
    general_function      text,
    specific_function     text,
    pathway               text,
    reaction              text,
    cellular_location     text,
    signals               text,
    transmembrane_regions text,
    molecular_weight      double precision,
    theoretical_pi        varchar(32),
    locus                 varchar(64),
    chromosome            varchar(16),
    uniprot_name          varchar(64),
    uniprot_id            varchar(16)  NOT NULL,
    pdb_id                varchar(16),
    genbank_protein_id    varchar(32),
    genbank_gene_id       varchar(32),
    genecard_id           varchar(32),
    genatlas_id           varchar(32),
    hgnc_id               varchar(32),
    hprd_id               varchar(32),
    organism              varchar(128),
    general_citations     text,
    comments              text,
    creator_id            integer,
    updater_id            integer,
    created_at            timestamptz  NOT NULL DEFAULT now(),
    updated_at            timestamptz  NOT NULL DEFAULT now(),
    CONSTRAINT uq_enzyme_uniprot_id UNIQUE (uniprot_id)
);
CREATE INDEX ix_enzyme_gene_name ON enzyme (gene_name);
CREATE INDEX ix_enzyme_name_trgm ON enzyme USING gin (name gin_trgm_ops);

CREATE TABLE ncbi_taxonomy_map
(
    id            integer PRIMARY KEY,
    taxonomy_name varchar(64) NOT NULL,
    rank          varchar(32) NOT NULL,
    created_at    timestamptz DEFAULT now(),
    updated_at    timestamptz DEFAULT now()
);
CREATE INDEX ix_ncbi_taxonomy_map_name ON ncbi_taxonomy_map (taxonomy_name);

CREATE TABLE ontology_term
(
    id              integer PRIMARY KEY,
    term            varchar(255) NOT NULL,
    definition      text,
    external_id     varchar(64),
    external_source varchar(255),
    comment         varchar(255),
    curator         varchar(64),
    parent_id       integer,
    level           smallint,
    created_at      timestamptz  NOT NULL DEFAULT now(),
    updated_at      timestamptz  NOT NULL DEFAULT now(),
    legacy_id       varchar(32),
    CONSTRAINT fk_ontology_term_parent FOREIGN KEY (parent_id) REFERENCES ontology_term (id)
);
CREATE INDEX ix_ontology_term_parent_id ON ontology_term (parent_id);
CREATE INDEX ix_ontology_term_external_id ON ontology_term (external_id) WHERE external_id IS NOT NULL;
CREATE INDEX ix_ontology_term_term_trgm ON ontology_term USING gin (term gin_trgm_ops);

CREATE TABLE ontology_synonym
(
    id               integer PRIMARY KEY,
    ontology_term_id integer      NOT NULL,
    synonym          varchar(255) NOT NULL,
    external_id      varchar(64),
    external_source  varchar(255),
    parent_id        varchar(64),
    parent_source    varchar(255),
    comment          varchar(255),
    curator          varchar(64),
    created_at       timestamptz  NOT NULL DEFAULT now(),
    updated_at       timestamptz  NOT NULL DEFAULT now(),
    CONSTRAINT fk_ontology_synonym_term FOREIGN KEY (ontology_term_id) REFERENCES ontology_term (id)
);
CREATE INDEX ix_ontology_synonym_term_id ON ontology_synonym (ontology_term_id);
CREATE INDEX ix_ontology_synonym_synonym_trgm ON ontology_synonym USING gin (synonym gin_trgm_ops);

CREATE TABLE pfam
(
    id         integer PRIMARY KEY,
    identifier varchar(32) NOT NULL,
    name       varchar(255),
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT uq_pfam_identifier UNIQUE (identifier)
);

-- -------------------------------------------------------------------------------------
-- Compound children
-- -------------------------------------------------------------------------------------
CREATE TABLE accession_number
(
    id          integer PRIMARY KEY,
    number      varchar(32) NOT NULL,
    compound_id integer     NOT NULL,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now(),
    source_id   integer,
    source_type varchar(32)
);
CREATE INDEX ix_accession_number_compound_id ON accession_number (compound_id);
CREATE INDEX ix_accession_number_number ON accession_number (number);

CREATE TABLE compound_alternate_parent
(
    id          integer PRIMARY KEY,
    name        varchar(255),
    compound_id integer     NOT NULL,
    creator_id  integer,
    updater_id  integer,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX ix_compound_alternate_parent_compound_id ON compound_alternate_parent (compound_id);
CREATE INDEX ix_compound_alternate_parent_name ON compound_alternate_parent (name);

CREATE TABLE compound_external_descriptor
(
    id          integer PRIMARY KEY,
    external_id varchar(64) NOT NULL,
    annotations varchar(512),
    compound_id integer     NOT NULL,
    creator_id  integer,
    updater_id  integer,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX ix_compound_external_descriptor_compound_id ON compound_external_descriptor (compound_id);
CREATE INDEX ix_compound_external_descriptor_external_id ON compound_external_descriptor (external_id);

CREATE TABLE compound_substituent
(
    id          integer PRIMARY KEY,
    name        varchar(255),
    compound_id integer     NOT NULL,
    creator_id  integer,
    updater_id  integer,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX ix_compound_substituent_compound_id ON compound_substituent (compound_id);
CREATE INDEX ix_compound_substituent_name ON compound_substituent (name);

CREATE TABLE compounds_enzyme
(
    id          integer PRIMARY KEY,
    compound_id integer     NOT NULL,
    enzyme_id   integer     NOT NULL,
    citations   text,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now(),
    creator_id  integer,
    updater_id  integer,
    CONSTRAINT uq_compounds_enzyme UNIQUE (compound_id, enzyme_id),
    CONSTRAINT fk_compounds_enzyme_enzyme FOREIGN KEY (enzyme_id) REFERENCES enzyme (id)
);
CREATE INDEX ix_compounds_enzyme_enzyme_id ON compounds_enzyme (enzyme_id, compound_id);

CREATE TABLE compounds_flavor
(
    id          integer PRIMARY KEY,
    compound_id integer     NOT NULL,
    flavor_id   integer     NOT NULL,
    citations   text,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now(),
    creator_id  integer,
    updater_id  integer,
    source_id   integer,
    source_type varchar(32),
    CONSTRAINT uq_compounds_flavor UNIQUE (compound_id, flavor_id),
    CONSTRAINT fk_compounds_flavor_flavor FOREIGN KEY (flavor_id) REFERENCES flavor (id),
    CONSTRAINT ck_compounds_flavor_source_type CHECK (source_type IS NULL OR source_type = 'Compound')
);
CREATE INDEX ix_compounds_flavor_flavor_id ON compounds_flavor (flavor_id, compound_id);

CREATE TABLE compounds_health_effect
(
    id                      integer PRIMARY KEY,
    compound_id             integer     NOT NULL,
    health_effect_id        integer     NOT NULL,
    orig_health_effect_name varchar(255),
    orig_compound_name      varchar(255),
    orig_citation           text,
    citation                text        NOT NULL,
    citation_type           varchar(32) NOT NULL,
    created_at              timestamptz NOT NULL DEFAULT now(),
    updated_at              timestamptz NOT NULL DEFAULT now(),
    creator_id              integer,
    updater_id              integer,
    source_id               integer,
    source_type             varchar(32),
    CONSTRAINT fk_compounds_health_effect_health_effect FOREIGN KEY (health_effect_id) REFERENCES health_effect (id),
    CONSTRAINT ck_compounds_health_effect_source_type CHECK (source_type IS NULL OR source_type = 'Compound')
);
CREATE INDEX ix_compounds_health_effect_compound_id ON compounds_health_effect (compound_id);
CREATE INDEX ix_compounds_health_effect_health_effect_id ON compounds_health_effect (health_effect_id, compound_id);

CREATE TABLE compounds_pathway
(
    id          integer PRIMARY KEY,
    compound_id integer     NOT NULL,
    pathway_id  integer     NOT NULL,
    creator_id  integer,
    updater_id  integer,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT uq_compounds_pathway UNIQUE (compound_id, pathway_id),
    CONSTRAINT fk_compounds_pathway_pathway FOREIGN KEY (pathway_id) REFERENCES pathway (id)
);
CREATE INDEX ix_compounds_pathway_pathway_id ON compounds_pathway (pathway_id, compound_id);

CREATE TABLE pdb_identifier
(
    id          integer PRIMARY KEY,
    compound_id integer     NOT NULL,
    pdb_id      varchar(16) NOT NULL,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX ix_pdb_identifier_compound_id ON pdb_identifier (compound_id);

CREATE TABLE reference
(
    id          integer PRIMARY KEY,
    ref_type    varchar(16) NOT NULL,
    text        text        NOT NULL,
    pubmed_id   varchar(16),
    link        varchar(512),
    title       text,
    creator_id  integer,
    updater_id  integer,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now(),
    source_id   integer     NOT NULL,
    source_type varchar(32) NOT NULL,
    CONSTRAINT ck_reference_source_type CHECK (source_type IN ('Compound'))
);
CREATE INDEX ix_reference_source ON reference (source_type, source_id);
CREATE INDEX ix_reference_pubmed_id ON reference (pubmed_id) WHERE pubmed_id IS NOT NULL;

-- -------------------------------------------------------------------------------------
-- Food children / enzyme children / polymorphic links
-- -------------------------------------------------------------------------------------
CREATE TABLE food_taxonomy
(
    id                   integer PRIMARY KEY,
    food_id              integer     NOT NULL,
    ncbi_taxonomy_id     integer     NOT NULL,
    classification_name  varchar(64) NOT NULL,
    classification_order smallint    NOT NULL,
    created_at           timestamptz NOT NULL DEFAULT now(),
    updated_at           timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT fk_food_taxonomy_food FOREIGN KEY (food_id) REFERENCES food (id)
);
CREATE INDEX ix_food_taxonomy_food_id ON food_taxonomy (food_id, classification_order);
CREATE INDEX ix_food_taxonomy_ncbi_taxonomy_id ON food_taxonomy (ncbi_taxonomy_id);

CREATE TABLE enzyme_synonym
(
    id         integer PRIMARY KEY,
    enzyme_id  integer      NOT NULL,
    synonym    varchar(255) NOT NULL,
    source     varchar(64),
    created_at timestamptz  NOT NULL DEFAULT now(),
    updated_at timestamptz  NOT NULL DEFAULT now(),
    CONSTRAINT fk_enzyme_synonym_enzyme FOREIGN KEY (enzyme_id) REFERENCES enzyme (id)
);
CREATE INDEX ix_enzyme_synonym_enzyme_id ON enzyme_synonym (enzyme_id);

CREATE TABLE pfam_membership
(
    id         integer PRIMARY KEY,
    enzyme_id  integer     NOT NULL,
    pfam_id    integer     NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT uq_pfam_membership UNIQUE (enzyme_id, pfam_id),
    CONSTRAINT fk_pfam_membership_enzyme FOREIGN KEY (enzyme_id) REFERENCES enzyme (id),
    CONSTRAINT fk_pfam_membership_pfam FOREIGN KEY (pfam_id) REFERENCES pfam (id)
);
CREATE INDEX ix_pfam_membership_pfam_id ON pfam_membership (pfam_id);

CREATE TABLE map_items_pathway
(
    id            integer PRIMARY KEY,
    map_item_id   integer     NOT NULL,
    map_item_type varchar(32) NOT NULL,
    pathway_id    integer     NOT NULL,
    created_at    timestamptz NOT NULL DEFAULT now(),
    updated_at    timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT fk_map_items_pathway_pathway FOREIGN KEY (pathway_id) REFERENCES pathway (id)
);
CREATE INDEX ix_map_items_pathway_item ON map_items_pathway (map_item_type, map_item_id);
CREATE INDEX ix_map_items_pathway_pathway_id ON map_items_pathway (pathway_id);

CREATE TABLE sequence
(
    id                integer PRIMARY KEY,
    header            text        NOT NULL,
    chain             text        NOT NULL,
    sequenceable_id   integer     NOT NULL,
    sequenceable_type varchar(32) NOT NULL,
    type              varchar(32) NOT NULL,
    created_at        timestamptz NOT NULL DEFAULT now(),
    updated_at        timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX ix_sequence_sequenceable ON sequence (sequenceable_type, sequenceable_id);

-- -------------------------------------------------------------------------------------
-- Partitioned tables (RANGE by created_at, yearly)
-- The partition key must be part of every PK/unique constraint, hence PRIMARY KEY (id, created_at).
-- -------------------------------------------------------------------------------------
CREATE TABLE content
(
    id                        bigint       NOT NULL,
    source_id                 integer      NOT NULL,
    source_type               varchar(16)  NOT NULL,
    food_id                   integer      NOT NULL,
    orig_food_id              varchar(32),
    orig_food_common_name     varchar(255),
    orig_food_scientific_name varchar(128),
    orig_food_part            varchar(64),
    orig_source_id            varchar(255),
    orig_source_name          varchar(255),
    orig_content              double precision,
    orig_min                  double precision,
    orig_max                  double precision,
    orig_unit                 varchar(32),
    orig_citation             varchar(512),
    citation                  varchar(512) NOT NULL,
    citation_type             varchar(32)  NOT NULL,
    creator_id                integer,
    updater_id                integer,
    created_at                timestamptz  NOT NULL DEFAULT now(),
    updated_at                timestamptz  NOT NULL DEFAULT now(),
    orig_method               varchar(64),
    orig_unit_expression      varchar(32),
    standard_content          double precision,
    preparation_type          varchar(64),
    export                    boolean      NOT NULL DEFAULT false,
    PRIMARY KEY (id, created_at),
    CONSTRAINT ck_content_source_type CHECK (source_type IN ('Compound', 'Nutrient'))
) PARTITION BY RANGE (created_at);
COMMENT
ON TABLE content IS 'Food composition facts (~5.1M rows). source_id/source_type is polymorphic (compound | nutrient).';
CREATE INDEX ix_content_food_source ON content (food_id, source_type, source_id);
CREATE INDEX ix_content_source_food ON content (source_type, source_id, food_id);
CREATE INDEX ix_content_standard_content ON content (source_type, source_id, standard_content) WHERE standard_content IS NOT NULL;

CREATE TABLE compound_ontology_term
(
    id               bigint      NOT NULL,
    compound_id      integer     NOT NULL,
    export           boolean,
    ontology_term_id integer     NOT NULL,
    created_at       timestamptz NOT NULL DEFAULT now(),
    updated_at       timestamptz NOT NULL DEFAULT now(),
    PRIMARY KEY (id, created_at)
) PARTITION BY RANGE (created_at);
COMMENT
ON TABLE compound_ontology_term IS 'Compound <-> ontology_term link (~1.6M rows, contains duplicate pairs in the source).';
CREATE INDEX ix_compound_ontology_term_compound ON compound_ontology_term (compound_id, ontology_term_id);
CREATE INDEX ix_compound_ontology_term_term ON compound_ontology_term (ontology_term_id, compound_id);

CREATE TABLE compound_synonym
(
    id             integer      NOT NULL,
    synonym        varchar(255) NOT NULL,
    synonym_source varchar(32)  NOT NULL,
    created_at     timestamptz  NOT NULL DEFAULT now(),
    updated_at     timestamptz  NOT NULL DEFAULT now(),
    source_id      integer      NOT NULL,
    source_type    varchar(32)  NOT NULL,
    PRIMARY KEY (id, created_at),
    CONSTRAINT ck_compound_synonym_source_type CHECK (source_type IN ('Compound', 'Nutrient', 'FoodcomexCompound'))
) PARTITION BY RANGE (created_at);
CREATE INDEX ix_compound_synonym_source ON compound_synonym (source_type, source_id);
CREATE INDEX ix_compound_synonym_synonym_trgm ON compound_synonym USING gin (synonym gin_trgm_ops);

-- Creates (idempotently) the yearly partition <parent>_y<year>.
CREATE
OR REPLACE FUNCTION fn_create_year_partition(p_parent regclass, p_year integer) RETURNS text
    LANGUAGE plpgsql AS
$$
DECLARE
v_name text := format('%s_y%s', p_parent::text, p_year);
BEGIN
EXECUTE format(
        'CREATE TABLE IF NOT EXISTS %I PARTITION OF %s FOR VALUES FROM (%L) TO (%L)',
        v_name, p_parent::text,
        format('%s-01-01 00:00:00+00', p_year), format('%s-01-01 00:00:00+00', p_year + 1));
RETURN v_name;
END;
$$;

-- Creates yearly partitions for [p_from, p_to] and a DEFAULT partition (catches out-of-range rows).
CREATE
OR REPLACE PROCEDURE pr_create_partitions(p_parent regclass, p_from integer, p_to integer)
    LANGUAGE plpgsql AS
$$
BEGIN
FOR y IN p_from..p_to LOOP
        PERFORM fn_create_year_partition(p_parent, y);
END LOOP;
EXECUTE format('CREATE TABLE IF NOT EXISTS %I PARTITION OF %s DEFAULT',
               p_parent::text || '_default', p_parent::text);
END;
$$;

-- Source data ranges: content 2014-2020, compound_ontology_term 2018-2019, compound_synonym 2010-2017.
-- Future years are pre-created; call pr_create_partitions(...) yearly to extend.
CALL pr_create_partitions('content', 2014, 2028);
CALL pr_create_partitions('compound_ontology_term', 2018, 2028);
CALL pr_create_partitions('compound_synonym', 2010, 2028);

-- -------------------------------------------------------------------------------------
-- Foreign keys
-- The export contains orphans (child rows whose parent was not exported), e.g. 43% of
-- compound_ontology_term.compound_id and 2826 content.food_id values. Those FKs are created by
-- pr_add_post_load_foreign_keys() as NOT VALID (enforced for new/changed rows, existing rows untouched),
-- so the bulk COPY is not rejected. VALIDATE CONSTRAINT after cleaning the data.
-- -------------------------------------------------------------------------------------
CREATE
OR REPLACE PROCEDURE pr_add_post_load_foreign_keys()
    LANGUAGE plpgsql AS
$$
DECLARE
r record;
BEGIN
FOR r IN
SELECT *
FROM (VALUES ('accession_number', 'compound_id', 'compound', 'fk_accession_number_compound'),
             ('compound_alternate_parent', 'compound_id', 'compound', 'fk_compound_alternate_parent_compound'),
             ('compound_external_descriptor', 'compound_id', 'compound', 'fk_compound_external_descriptor_compound'),
             ('compound_substituent', 'compound_id', 'compound', 'fk_compound_substituent_compound'),
             ('compounds_enzyme', 'compound_id', 'compound', 'fk_compounds_enzyme_compound'),
             ('compounds_flavor', 'compound_id', 'compound', 'fk_compounds_flavor_compound'),
             ('compounds_health_effect', 'compound_id', 'compound', 'fk_compounds_health_effect_compound'),
             ('compounds_pathway', 'compound_id', 'compound', 'fk_compounds_pathway_compound'),
             ('pdb_identifier', 'compound_id', 'compound', 'fk_pdb_identifier_compound'),
             ('compound_ontology_term', 'compound_id', 'compound', 'fk_compound_ontology_term_compound'),
             ('compound_ontology_term', 'ontology_term_id', 'ontology_term', 'fk_compound_ontology_term_term'),
             ('content', 'food_id', 'food', 'fk_content_food')) AS t(child, child_col, parent, fk_name) LOOP
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = r.fk_name AND conrelid = r.child::regclass) THEN
            EXECUTE format('ALTER TABLE %I ADD CONSTRAINT %I FOREIGN KEY (%I) REFERENCES %I (id) NOT VALID',
                           r.child, r.fk_name, r.child_col, r.parent);
END IF;
END LOOP;
END;
$$;

-- -------------------------------------------------------------------------------------
-- updated_at triggers (every table that has the column)
-- -------------------------------------------------------------------------------------
DO
$$
DECLARE
t text;
BEGIN
FOR t IN
SELECT c.relname
FROM pg_class c
         JOIN pg_namespace n ON n.oid = c.relnamespace
         JOIN pg_attribute a ON a.attrelid = c.oid AND a.attname = 'updated_at' AND NOT a.attisdropped
WHERE n.nspname = current_schema()
  AND c.relkind IN ('r', 'p')
  AND NOT c.relispartition LOOP
        EXECUTE format('CREATE TRIGGER trg_%s_set_updated_at BEFORE UPDATE ON %I
                        FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at()', t, t);
END LOOP;
END;
$$;

-- -------------------------------------------------------------------------------------
-- Views
-- -------------------------------------------------------------------------------------
-- Content resolved to its polymorphic source (compound or nutrient) and food.
CREATE
OR REPLACE VIEW v_food_content AS
SELECT ct.id                              AS content_id,
       ct.food_id,
       f.public_id                        AS food_public_id,
       f.name                             AS food_name,
       f.food_group,
       ct.source_type,
       ct.source_id,
       COALESCE(c.public_id, n.public_id) AS source_public_id,
       COALESCE(c.name, n.name)           AS source_name,
       ct.standard_content,
       ct.orig_content,
       ct.orig_min,
       ct.orig_max,
       ct.orig_unit,
       ct.preparation_type,
       ct.citation,
       ct.citation_type,
       ct.created_at
FROM content ct
         JOIN food f ON f.id = ct.food_id
         LEFT JOIN compound c ON ct.source_type = 'Compound' AND c.id = ct.source_id
         LEFT JOIN nutrient n ON ct.source_type = 'Nutrient' AND n.id = ct.source_id;

-- Compound with its classification and counters of related facts.
CREATE
OR REPLACE VIEW v_compound_overview AS
SELECT c.id,
       c.public_id,
       c.name,
       c.cas_number,
       c.moldb_inchikey,
       c.kingdom,
       c.superklass,
       c.klass,
       c.subklass,
       COALESCE(fl.flavors, ARRAY[]::text[])        AS flavors,
       COALESCE(he.health_effects, ARRAY[]::text[]) AS health_effects,
       COALESCE(en.enzymes, ARRAY[]::text[])        AS enzymes,
       COALESCE(pw.pathways, ARRAY[]::text[])       AS pathways
FROM compound c
         LEFT JOIN LATERAL (SELECT array_agg(DISTINCT x.name ORDER BY x.name) AS flavors
                            FROM compounds_flavor cf
                                     JOIN flavor x ON x.id = cf.flavor_id
                            WHERE cf.compound_id = c.id) fl ON true
         LEFT JOIN LATERAL (SELECT array_agg(DISTINCT x.name ORDER BY x.name) AS health_effects
                            FROM compounds_health_effect ch
                                     JOIN health_effect x ON x.id = ch.health_effect_id
                            WHERE ch.compound_id = c.id) he ON true
         LEFT JOIN LATERAL (SELECT array_agg(DISTINCT x.name ORDER BY x.name) AS enzymes
                            FROM compounds_enzyme ce
                                     JOIN enzyme x ON x.id = ce.enzyme_id
                            WHERE ce.compound_id = c.id) en ON true
         LEFT JOIN LATERAL (SELECT array_agg(DISTINCT x.name ORDER BY x.name) AS pathways
                            FROM compounds_pathway cp
                                     JOIN pathway x ON x.id = cp.pathway_id
                            WHERE cp.compound_id = c.id) pw ON true;

-- Ontology terms with their full ancestor path (recursive).
CREATE
OR REPLACE VIEW v_ontology_term_path AS
WITH RECURSIVE t AS (
    SELECT id, term, parent_id, 0 AS depth, ARRAY[id] AS id_path, term::text AS term_path
    FROM ontology_term
    WHERE parent_id IS NULL
    UNION ALL
    SELECT o.id, o.term, o.parent_id, t.depth + 1, t.id_path || o.id, t.term_path || ' > ' || o.term
    FROM ontology_term o
             JOIN t ON o.parent_id = t.id
    WHERE NOT o.id = ANY (t.id_path)
)
SELECT id, term, parent_id, depth, id_path, term_path
FROM t;

-- Per (food, source) aggregates over the 5M-row content table: the expensive query most consumers need.
CREATE
MATERIALIZED VIEW mv_food_content_stats AS
SELECT food_id,
       source_type,
       source_id,
       count(*)              AS measurements,
       min(standard_content) AS min_content,
       avg(standard_content) AS avg_content,
       max(standard_content) AS max_content
FROM content
WHERE standard_content IS NOT NULL
GROUP BY food_id, source_type, source_id WITH NO DATA;
CREATE UNIQUE INDEX uq_mv_food_content_stats ON mv_food_content_stats (food_id, source_type, source_id);
CREATE INDEX ix_mv_food_content_stats_source ON mv_food_content_stats (source_type, source_id);

CREATE
OR REPLACE PROCEDURE pr_refresh_materialized_views()
    LANGUAGE plpgsql AS
$$
BEGIN
    IF
EXISTS (SELECT 1 FROM pg_class WHERE oid = 'mv_food_content_stats'::regclass AND relispopulated) THEN
        REFRESH MATERIALIZED VIEW CONCURRENTLY mv_food_content_stats;
ELSE
        REFRESH MATERIALIZED VIEW mv_food_content_stats;
END IF;
END;
$$;

-- -------------------------------------------------------------------------------------
-- Functions
-- -------------------------------------------------------------------------------------
-- Fuzzy compound lookup by name or synonym (uses the pg_trgm GIN indexes).
CREATE
OR REPLACE FUNCTION fn_search_compound(p_query text, p_limit integer DEFAULT 20)
    RETURNS TABLE (id integer, public_id varchar, name varchar, similarity real)
    LANGUAGE sql STABLE AS
$$
SELECT m.id, m.public_id, m.name, max(m.sim) ::real
FROM (SELECT c.id, c.public_id, c.name, similarity(c.name, p_query) AS sim
      FROM compound c
      WHERE c.name % p_query
      UNION ALL
      SELECT c.id, c.public_id, c.name, similarity(s.synonym, p_query)
      FROM compound_synonym s
          JOIN compound c
      ON s.source_type = 'Compound' AND c.id = s.source_id
      WHERE s.synonym % p_query) m
GROUP BY m.id, m.public_id, m.name
ORDER BY max(m.sim) DESC, m.name LIMIT p_limit;
$$;

-- Content of a given compound across foods.
CREATE
OR REPLACE FUNCTION fn_compound_food_content(p_compound_id integer)
    RETURNS TABLE (food_id integer, food_name varchar, measurements bigint, avg_content double precision)
    LANGUAGE sql STABLE AS
$$
SELECT s.food_id, f.name, s.measurements, s.avg_content
FROM mv_food_content_stats s
         JOIN food f ON f.id = s.food_id
WHERE s.source_type = 'Compound'
  AND s.source_id = p_compound_id
ORDER BY s.avg_content DESC NULLS LAST;
$$;
