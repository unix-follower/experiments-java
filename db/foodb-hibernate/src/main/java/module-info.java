open module experiments.foodb.hibernate {
    requires info.picocli;
    requires org.slf4j;
    requires org.example.foodb.db.foodb;
    requires jakarta.persistence;
    requires jakarta.transaction;
    requires jakarta.xml.bind;
    requires org.jboss.logging;
    requires org.hibernate.orm.core;
    requires net.bytebuddy;
}
