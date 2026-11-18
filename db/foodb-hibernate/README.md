# Run
```bash
export FOODB_USERNAME=scientist
export FOODB_PASSWORD=scientist
export FOODB_URL='jdbc:postgresql://localhost:5432/foodb?currentSchema=foodb'
../../gradlew :db:foodb-hibernate:clean :db:foodb-hibernate:build :db:foodb-hibernate:copyDependencies
java --module-path build/libs/lib:build/libs/foodb-hibernate-1.0.0.jar \
  --module experiments.foodb.hibernate/org.example.experiments.db.foodb.FooDbHibernateApp \
  -s AccessionNumberFindById
```
