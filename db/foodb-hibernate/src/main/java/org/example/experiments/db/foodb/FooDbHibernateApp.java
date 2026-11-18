package org.example.experiments.db.foodb;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceConfiguration;
import org.example.experiments.db.foodb.model.AccessionNumber;
import org.example.experiments.db.foodb.repo.AccessionNumberRepository;
import org.example.experiments.db.foodb.repo.AccessionNumberRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Objects;

@Command(name = "foodb", mixinStandardHelpOptions = true, version = "1.0.0",
        description = "Execute various DB operations.")
public class FooDbHibernateApp implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FooDbHibernateApp.class);

    private FooDbHibernateApp() {
    }

    @Option(names = {"-s", "--scenario"}, description = "The scenario name")
    private String scenario = "default";

    static void main(String... args) {
        new CommandLine(new FooDbHibernateApp()).execute(args);
    }

    @Override
    public void run() {
        switch (scenario) {
            case "AccessionNumberFindById" -> executeAccessionNumberFindById();
            default -> throw new IllegalArgumentException("Unknown scenario: " + scenario);
        }
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        final var dbUser = System.getenv("FOODB_USERNAME");
        Objects.requireNonNull(dbUser, () -> "The environment variable FOODB_USERNAME is not set");
        final var dbPassword = System.getenv("FOODB_PASSWORD");
        Objects.requireNonNull(dbPassword, () -> "The environment variable FOODB_PASSWORD is not set");
        final var dbUrl = System.getenv("FOODB_URL");
        Objects.requireNonNull(dbUrl, () -> "The environment variable FOODB_URL is not set");

        final var persistenceConfiguration = new PersistenceConfiguration("foodb")
                .property("jakarta.persistence.jdbc.user", dbUser)
                .property("jakarta.persistence.jdbc.password", dbPassword)
                .property("jakarta.persistence.jdbc.url", dbUrl)
                .property("hibernate.show_sql", "true")
                .property("hibernate.format_sql", "true")
                .property("hibernate.highlight_sql", "true")
                .managedClass(AccessionNumber.class);
        return Persistence.createEntityManagerFactory(persistenceConfiguration);
    }

    private void executeAccessionNumberFindById() {
        final var emFactory = createEntityManagerFactory();

        try (emFactory) {
            final var em = emFactory.createEntityManager();
            try { // NOPMD
                em.getTransaction().begin();

                final AccessionNumberRepository repository = new AccessionNumberRepositoryImpl(em);
                final var accessionNumber = repository.findById(0);
                LOGGER.info("{}", accessionNumber);

                em.getTransaction().commit();
                // CHECKSTYLE:OFF
            } catch (Exception e) { // NOPMD
                // CHECKSTYLE:ON
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                LOGGER.error(e.getMessage(), e);
            } finally {
                em.close();
            }
        }
    }
}
