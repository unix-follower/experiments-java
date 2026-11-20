# Experiments Java

## Commands cheat-sheet
```bash
./gradlew dependencies --configuration runtimeClasspath
./gradlew clean build copyDependencies
ls ~/.gradle/caches/modules-2/files-2.1/
```
### Diagnostic command requests
```bash
jps
jcmd 83087 GC.heap_info
jcmd 83087 GC.class_histogram
```
