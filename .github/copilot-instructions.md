# Copilot instructions for experiments-java

## Repository layout

This repo contains two unrelated things, do not conflate them:

- **Root Gradle project** (`../main/src`) — the only part built/tested by Gradle. Currently a single
  application: `ExperimentsMultithreadingApp` under package `org.example.experiments.multithreading`
  (note: differs from the Gradle `group`, `com.example.experiments`, and from the `application.mainClass`
  setting in `../main/build.gradle`, which still points at the old `com.example.experiments.multithreading` package —
  check both when wiring up a new entry point).
- **`examples/`** — standalone Java demos (`chemistry`, `class_loader_app`) built manually with `javac`/`jar`,
  *not* part of the Gradle build. See `../examples/class_loader_app/README.md` for the exact `javac`/`jar`/`java --module-path`
  invocations used to compile and run each demo (plain classpath vs. JPMS module variants).

## Build, test, lint

Requires JDK 26 (configured via Gradle toolchain, auto-provisioned by Gradle).

```bash
./gradlew build              # compile + test + checkstyle + pmd + jacoco
./gradlew test               # run tests only (JUnit Platform)
./gradlew test --tests "org.example.experiments.multithreading.SomeTest"   # single test class
./gradlew test --tests "org.example.experiments.multithreading.SomeTest.someMethod"  # single test method
./gradlew checkstyleMain checkstyleTest   # style check (config/checkstyle.xml)
./gradlew pmdMain pmdTest                 # static analysis (config/pmd-ruleset.xml)
./gradlew jacocoTestReport                # coverage report (html at build/reports/jacoco)
```

Checkstyle/PMD run as part of `check`/`build` and fail the build on violations (`ignoreFailures = false`,
`maxWarnings = 1`, `maxErrors = 1`). `jacocoTestCoverageVerification` enforces 80% overall line coverage.
There are currently no test sources in the project — add them under `../main/src` mirroring the main
package structure.

## OpenTelemetry integration

`OpenTelemetrySupport` wires metrics export via OTLP gRPC. It reads `OTEL_EXPORTER_OTLP_ENDPOINT`
(default `http://localhost:4317`) and `OTEL_RESOURCE_DEPLOYMENT_ENVIRONMENT` (default `development`) from
the environment — set these before running the app (see `README.md`). A local collector/Prometheus/Kibana
stack for manual verification lives in `docker/docker-compose.yml` (cheat sheet in `docker/README.md`).

## Conventions

- Indentation is tabs in some existing files (`OpenTelemetrySupport.java`) and spaces in others
  (`ExperimentsMultithreadingApp.java`); `.editorconfig` specifies 4-space indent — follow it for new code.
- CLI entry points use picocli (`@Command`/`@Option`), with a static `main()` (no-arg, picocli handles args
  via `CommandLine`) rather than the usual `main(String[] args)`.
