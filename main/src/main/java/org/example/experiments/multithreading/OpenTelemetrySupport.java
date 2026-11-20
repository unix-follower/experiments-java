package org.example.experiments.multithreading;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

final class OpenTelemetrySupport implements AutoCloseable {
	private static final String DEFAULT_ENDPOINT = "http://localhost:4317";
	private static final Duration DEFAULT_EXPORT_INTERVAL = Duration.ofSeconds(2);

	private final OpenTelemetrySdk openTelemetry;
	private final SdkMeterProvider meterProvider;

	private OpenTelemetrySupport(OpenTelemetrySdk openTelemetry, SdkMeterProvider meterProvider) {
		this.openTelemetry = openTelemetry;
		this.meterProvider = meterProvider;
	}

	static OpenTelemetrySupport initialize(String serviceName, String serviceVersion) {
		final var endpoint = System.getenv().getOrDefault("OTEL_EXPORTER_OTLP_ENDPOINT", DEFAULT_ENDPOINT);
		final var environment = System.getenv()
                .getOrDefault("OTEL_RESOURCE_DEPLOYMENT_ENVIRONMENT", "development");

        final var metricExporter = OtlpGrpcMetricExporter.builder()
			.setEndpoint(endpoint)
			.build();

        final var metricReader = PeriodicMetricReader.builder(metricExporter)
			.setInterval(DEFAULT_EXPORT_INTERVAL)
			.build();

        final var resource = Resource.getDefault().merge(Resource.create(Attributes.builder()
			.put("service.name", serviceName)
			.put("service.version", serviceVersion)
			.put("deployment.environment", environment)
			.build()));

        final var meterProvider = SdkMeterProvider.builder()
			.setResource(resource)
			.registerMetricReader(metricReader)
			.build();

        final var openTelemetry = OpenTelemetrySdk.builder()
			.setMeterProvider(meterProvider)
			.build();

		return new OpenTelemetrySupport(openTelemetry, meterProvider);
	}

	OpenTelemetry openTelemetry() {
		return openTelemetry;
	}

	void flush() {
		meterProvider.forceFlush().join(10, TimeUnit.SECONDS);
	}

	@Override
	public void close() {
		flush();
		openTelemetry.close();
	}
}
