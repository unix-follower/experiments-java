open module org.example.foodb.db.foodb {
    exports org.example.experiments.db.foodb.model;

    requires jakarta.persistence;
    requires static org.hibernate.orm.core;
}
