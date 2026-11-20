package org.example.experiments.multithreading;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.LongUpDownCounter;
import io.opentelemetry.api.metrics.Meter;

import java.util.logging.Logger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class InstrumentedWorkload {
    private static final Logger LOGGER = Logger.getLogger(InstrumentedWorkload.class.getName());

    private final Meter meter;
	private final LongCounter submittedTasks;
	private final LongCounter completedTasks;
	private final LongCounter failedTasks;
	private final LongUpDownCounter inFlightTasks;
	private final LongHistogram taskLatency;

	InstrumentedWorkload(OpenTelemetry openTelemetry) {
		meter = openTelemetry.getMeter("com.example.experiments.multithreading.workload");
		submittedTasks = meter.counterBuilder("multithreading.tasks.submitted")
			.setDescription("Number of submitted tasks")
			.setUnit("{task}")
			.build();
		completedTasks = meter.counterBuilder("multithreading.tasks.completed")
			.setDescription("Number of successfully completed tasks")
			.setUnit("{task}")
			.build();
		failedTasks = meter.counterBuilder("multithreading.tasks.failed")
			.setDescription("Number of tasks that failed")
			.setUnit("{task}")
			.build();
		inFlightTasks = meter.upDownCounterBuilder("multithreading.tasks.in_flight")
			.setDescription("Tasks currently executing")
			.setUnit("{task}")
			.build();
		taskLatency = meter.histogramBuilder("multithreading.task.duration")
			.ofLongs()
			.setDescription("Task execution time")
			.setUnit("ms")
			.build();
	}

	WorkloadSummary run() throws InterruptedException {
		final int workerCount = 4;
		final int taskCount = 24;
        final var executorAttributes = Attributes.builder()
			.put("executor.type", "fixed")
			.put("executor.name", "practice-pool")
			.build();

        final var executor = new ThreadPoolExecutor(
			workerCount,
			workerCount,
			0L,
			TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>());

		openQueueDepthGauge(executor, executorAttributes);

		final var futures = new ArrayList<Future<?>>();
		for (int taskNumber = 0; taskNumber < taskCount; taskNumber++) {
			submittedTasks.add(1, executorAttributes);
            final int currentTask = taskNumber;
			futures.add(executor.submit(() -> executeTask(currentTask, executorAttributes)));
		}

        int failedCount = 0;
		for (final var future : futures) {
			try {
				future.get();
			} catch (ExecutionException exception) {
				failedCount++;
			}
		}

		executor.shutdown();
        final boolean awaited = executor.awaitTermination(5, TimeUnit.SECONDS);
        if  (!awaited) {
            LOGGER.warning("Timeout elapsed before executor termination");
        }

        return new WorkloadSummary(taskCount, taskCount - failedCount, failedCount);
	}

	private void openQueueDepthGauge(ThreadPoolExecutor executor, Attributes executorAttributes) {
		meter.gaugeBuilder("multithreading.executor.queue.depth")
			.ofLongs()
			.setDescription("Tasks waiting in the executor queue")
			.setUnit("{task}")
			.buildWithCallback(measurement ->
                    measurement.record(executor.getQueue().size(), executorAttributes));
	}

	private void executeTask(int taskNumber, Attributes executorAttributes) {
        final long startedAt = System.nanoTime();
		inFlightTasks.add(1, executorAttributes);
		try {
			Thread.sleep(ThreadLocalRandom.current().nextLong(75, 250));
			if (taskNumber % 8 == 0) {
				throw new IllegalStateException("Simulated task failure");
			}
			completedTasks.add(1, executorAttributes);
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			failedTasks.add(1, executorAttributes);
			throw new IllegalStateException("Task interrupted", exception);
            // CHECKSTYLE:OFF
		} catch (RuntimeException exception) { // NOPMD
            // CHECKSTYLE:ON
			failedTasks.add(1, executorAttributes);
			throw exception;
		} finally {
			inFlightTasks.add(-1, executorAttributes);
			taskLatency.record(Duration.ofNanos(System.nanoTime() - startedAt).toMillis(), executorAttributes);
		}
	}

	record WorkloadSummary(int submitted, int completed, int failed) {
	}
}
