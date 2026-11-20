package org.example.experiments.multithreading;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

@Command(name = "multithreading", mixinStandardHelpOptions = true, version = "1.0.0",
        description = "Execute various multithreading scenarios.")
public class ExperimentsMultithreadingApp implements Callable<Integer> {
    private static final String SERVICE_NAME = "experiments-multithreading-java";
    private static final String SERVICE_VERSION = "1.0.0";

    private static final Logger LOGGER = Logger.getLogger(ExperimentsMultithreadingApp.class.getName());

    @Option(names = {"-s", "--scenario"}, description = "The scenario name")
    private String scenario = "default";

    private final OpenTelemetrySupport telemetry;

    public ExperimentsMultithreadingApp(OpenTelemetrySupport telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Integer call() throws Exception {
        if ("default".equals(scenario)) {
            InstrumentedWorkload.WorkloadSummary summary = new InstrumentedWorkload(telemetry.openTelemetry()).run();
            telemetry.flush();
            LOGGER.info(String.format(
                    "Submitted %d tasks, completed %d, failed %d.%n",
                    summary.submitted(),
                    summary.completed(),
                    summary.failed()));
        }
        return 0;
    }

    static void main() {
        try (OpenTelemetrySupport telemetry = OpenTelemetrySupport.initialize(SERVICE_NAME, SERVICE_VERSION)) {
            new CommandLine(new ExperimentsMultithreadingApp(telemetry)).execute();
        }
    }
}
