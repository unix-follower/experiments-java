# Commands cheat-sheet
```bash
./gradlew main:clean main:build main:copyDependencies
```
## Decompile
```bash
javap -p -v build/classes/java/main/org/example/experiments/multithreading/ratelimiter/RateLimiterWithThreadInterruptApp.class \
  > build/tmp/RateLimiterWithThreadInterruptApp.class.txt
```

# ExperimentsMultithreadingApp
## Prerequisites:
```bash
export OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:4317"
```

# RateLimiterWithThreadInterruptApp
```bash
java --module-path build/libs/lib:build/libs/experiments-java-1.0.0.jar \
  --module experiments.java/org.example.experiments.multithreading.ratelimiter.RateLimiterWithThreadInterruptApp
```
# DaemonThreadApp
```bash
java --module-path build/libs/lib:build/libs/experiments-java-1.0.0.jar \
  --module experiments.java/org.example.experiments.multithreading.DaemonThreadApp
```
# ActiveObjectApp
```bash
java --module-path build/libs/lib:build/libs/experiments-java-1.0.0.jar \
  --module experiments.java/org.example.experiments.multithreading.activeobject.ActiveObjectApp -s sync-on-this
```
