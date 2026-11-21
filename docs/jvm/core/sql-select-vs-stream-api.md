# SQL SELECT vs Java Stream API

Cheat sheet mapping SQL `SELECT` operations to the Java Stream API (Java 24+ for `Gatherers`).

## Sample model

```java
record Employee(long id, String name, String dept, double salary, Long managerId, LocalDate hired) {}
record Department(String code, String title) {}

List<Employee> employees = ...;      // table employees
List<Department> departments = ...;  // table departments
```

```java
import static java.util.stream.Collectors.*;
```

## Basic querying

| SQL | Stream API |
|-----|------------|
| `SELECT * FROM employees` | `employees.stream()` |
| `SELECT name, salary FROM employees` | `employees.stream().map(e -> new NameSalary(e.name(), e.salary()))` |
| `SELECT DISTINCT dept FROM employees` | `employees.stream().map(Employee::dept).distinct()` |
| `WHERE salary > 1000` | `.filter(e -> e.salary() > 1000)` |
| `WHERE dept = 'IT' AND salary > 1000` | `.filter(e -> e.dept().equals("IT") && e.salary() > 1000)` (or chain two `filter` calls) |
| `WHERE dept = 'IT' OR dept = 'HR'` | `.filter(e -> e.dept().equals("IT") \|\| e.dept().equals("HR"))` |
| `WHERE NOT dept = 'IT'` | `.filter(e -> !e.dept().equals("IT"))` or `.filter(Predicate.not(...))` |
| `WHERE dept IN ('IT', 'HR')` | `Set<String> s = Set.of("IT", "HR"); .filter(e -> s.contains(e.dept()))` |
| `WHERE salary BETWEEN 1000 AND 2000` | `.filter(e -> e.salary() >= 1000 && e.salary() <= 2000)` (BETWEEN is inclusive) |
| `WHERE name LIKE 'A%'` | `.filter(e -> e.name().startsWith("A"))` |
| `WHERE name LIKE '%an%'` | `.filter(e -> e.name().contains("an"))` (complex patterns: `Pattern.asPredicate()`) |
| `WHERE managerId IS NULL` | `.filter(e -> e.managerId() == null)` |
| `WHERE managerId IS NOT NULL` | `.filter(e -> e.managerId() != null)` or `.filter(Objects::nonNull)` on the mapped value |
| `COALESCE(managerId, 0)` | `.map(e -> Objects.requireNonNullElse(e.managerId(), 0L))` |
| `CASE WHEN salary > 1000 THEN 'HIGH' ELSE 'LOW' END` | `.map(e -> e.salary() > 1000 ? "HIGH" : "LOW")` (or `switch` expression with guards) |

## Sorting and paging

| SQL | Stream API |
|-----|------------|
| `ORDER BY salary` | `.sorted(Comparator.comparingDouble(Employee::salary))` |
| `ORDER BY salary DESC` | `.sorted(Comparator.comparingDouble(Employee::salary).reversed())` |
| `ORDER BY dept, salary DESC` | `.sorted(Comparator.comparing(Employee::dept).thenComparing(Employee::salary, Comparator.reverseOrder()))` |
| `NULLS LAST` | `Comparator.nullsLast(Comparator.naturalOrder())` |
| `LIMIT 10` / `FETCH FIRST 10 ROWS ONLY` | `.limit(10)` |
| `OFFSET 20` | `.skip(20)` |
| `LIMIT 10 OFFSET 20` | `.skip(20).limit(10)` (sort first, otherwise the page is arbitrary) |
| `ORDER BY ... LIMIT 1` (top row) | `.max(comparator)` / `.min(comparator)` returns `Optional` |
| `WHERE ... ORDER BY` stop at condition | `.takeWhile(p)` / `.dropWhile(p)` (ordered streams; no SQL equivalent) |

## Aggregation

| SQL | Stream API |
|-----|------------|
| `COUNT(*)` | `.count()` |
| `COUNT(DISTINCT dept)` | `.map(Employee::dept).distinct().count()` |
| `SUM(salary)` | `.mapToDouble(Employee::salary).sum()` |
| `AVG(salary)` | `.mapToDouble(Employee::salary).average()` returns `OptionalDouble` (SQL gives `NULL` on empty) |
| `MIN(salary)` / `MAX(salary)` | `.mapToDouble(Employee::salary).min()` / `.max()` |
| `COUNT, SUM, AVG, MIN, MAX` in one pass | `.mapToDouble(Employee::salary).summaryStatistics()` |
| `EXISTS (SELECT 1 ... WHERE p)` | `.anyMatch(p)` |
| `NOT EXISTS (...)` | `.noneMatch(p)` |
| `x >= ALL (...)` | `.allMatch(p)` |
| `STRING_AGG(name, ', ')` | `.map(Employee::name).collect(joining(", "))` |

## GROUP BY and HAVING

| SQL | Stream API |
|-----|------------|
| `GROUP BY dept` (rows per group) | `.collect(groupingBy(Employee::dept))` returns `Map<String, List<Employee>>` |
| `SELECT dept, COUNT(*) GROUP BY dept` | `.collect(groupingBy(Employee::dept, counting()))` |
| `SELECT dept, SUM(salary) GROUP BY dept` | `.collect(groupingBy(Employee::dept, summingDouble(Employee::salary)))` |
| `SELECT dept, AVG(salary) GROUP BY dept` | `.collect(groupingBy(Employee::dept, averagingDouble(Employee::salary)))` |
| `SELECT dept, MAX(salary) GROUP BY dept` | `.collect(groupingBy(Employee::dept, maxBy(Comparator.comparingDouble(Employee::salary))))` |
| `SELECT dept, STRING_AGG(name, ',')` | `.collect(groupingBy(Employee::dept, mapping(Employee::name, joining(","))))` |
| `GROUP BY dept, YEAR(hired)` | `groupingBy(e -> List.of(e.dept(), e.hired().getYear()))` or a key record |
| `GROUP BY ... ORDER BY` | `groupingBy(f, TreeMap::new, downstream)` or sort `entrySet()` afterwards |
| `HAVING COUNT(*) > 5` | `.collect(groupingBy(Employee::dept, counting())).entrySet().stream().filter(en -> en.getValue() > 5)` |
| Multiple aggregates per group | `groupingBy(Employee::dept, teeing(counting(), averagingDouble(Employee::salary), Stats::new))` |
| `PIVOT` (conditional counts) | `partitioningBy(e -> e.salary() > 1000, counting())` |

## Joins

| SQL | Stream API |
|-----|------------|
| `INNER JOIN departments d ON e.dept = d.code` | `Map<String, Department> byCode = departments.stream().collect(toMap(Department::code, identity()));`<br>`employees.stream().filter(e -> byCode.containsKey(e.dept())).map(e -> new Row(e, byCode.get(e.dept())))` |
| `LEFT JOIN` | `employees.stream().map(e -> new Row(e, byCode.get(e.dept())))` (missing side is `null`, or use `Optional.ofNullable`) |
| `RIGHT JOIN` | swap the two sides and run the left join |
| `FULL OUTER JOIN` | left join, plus the right-side rows that matched nothing, concatenated with `Stream.concat` |
| One-to-many join | `departments.stream().flatMap(d -> employees.stream().filter(e -> e.dept().equals(d.code())).map(e -> new Row(e, d)))` (O(n*m), prefer a pre-grouped map) |
| Self join (`e JOIN employees m ON e.managerId = m.id`) | `Map<Long, Employee> byId = ...; .map(e -> new Row(e, byId.get(e.managerId())))` |
| `CROSS JOIN` / cartesian product | `employees.stream().flatMap(e -> departments.stream().map(d -> new Row(e, d)))` |
| Cartesian product of 3 sets | `a.stream().flatMap(x -> b.stream().flatMap(y -> c.stream().map(z -> new Triple(x, y, z))))` |
| `JOIN ... ON a.x < b.x` (non-equi join) | nested `flatMap` with `filter`; no hash lookup possible, always O(n*m) |
| `SEMI JOIN` (`WHERE EXISTS` / `IN (subquery)`) | `Set<String> codes = ...; employees.stream().filter(e -> codes.contains(e.dept()))` |
| `ANTI JOIN` (`NOT EXISTS` / `NOT IN`) | same as above with `!codes.contains(...)` |

## Set operations

| SQL | Stream API |
|-----|------------|
| `UNION ALL` | `Stream.concat(a.stream(), b.stream())` |
| `UNION` | `Stream.concat(a.stream(), b.stream()).distinct()` |
| `INTERSECT` | `Set<T> s = new HashSet<>(b); a.stream().filter(s::contains).distinct()` |
| `EXCEPT` / `MINUS` | `Set<T> s = new HashSet<>(b); a.stream().filter(x -> !s.contains(x)).distinct()` |

## Subqueries and CTEs

| SQL | Stream API |
|-----|------------|
| Scalar subquery `WHERE salary > (SELECT AVG(salary) ...)` | `double avg = employees.stream().mapToDouble(Employee::salary).average().orElse(0);`<br>`employees.stream().filter(e -> e.salary() > avg)` |
| `IN (SELECT ...)` | build a `Set` from the inner stream, then `filter(set::contains)` |
| Correlated subquery | `filter(e -> others.stream().anyMatch(o -> o.dept().equals(e.dept()) && ...))` (O(n*m); pre-group for speed) |
| Derived table `FROM (SELECT ...) t` | chain another stage on the result: `stream.filter(...).collect(...).stream()...` or `mapMulti` / `flatMap` |
| CTE `WITH high AS (SELECT ... WHERE salary > 1000) SELECT ... FROM high` | `List<Employee> high = employees.stream().filter(e -> e.salary() > 1000).toList();` then `high.stream()...` (a named, reusable collection; a Stream can be consumed only once) |
| Multiple CTEs referencing each other | one `var` per CTE, each built from the previous: `var a = ...toList(); var b = a.stream()....toList();` |
| CTE used twice (materialized) | the `toList()` result is reused; use `Supplier<Stream<T>>` if you need a lazy re-evaluated "view" |
| `WITH RECURSIVE` (hierarchy walk) | `Stream.iterate(List.of(root), l -> !l.isEmpty(), l -> l.stream().flatMap(e -> childrenOf(e).stream()).toList()).flatMap(List::stream)` (level by level) |
| `WITH RECURSIVE` (counter 1..N) | `Stream.iterate(1, i -> i <= N, i -> i + 1)` or `IntStream.rangeClosed(1, N)` |

## Window functions

Window functions keep every row and add a computed column. In Streams: group (partition), compute inside the group, then flatten back.

| SQL | Stream API |
|-----|------------|
| `ROW_NUMBER() OVER (ORDER BY salary DESC)` | `var sorted = employees.stream().sorted(bySalaryDesc).toList(); IntStream.range(0, sorted.size()).mapToObj(i -> new Ranked(i + 1, sorted.get(i)))` |
| `ROW_NUMBER() OVER (PARTITION BY dept ORDER BY salary DESC)` | `.collect(groupingBy(Employee::dept)).values().stream().flatMap(g -> { var s = g.stream().sorted(bySalaryDesc).toList(); return IntStream.range(0, s.size()).mapToObj(i -> new Ranked(i + 1, s.get(i))); })` |
| Top-N per group (`WHERE rn <= 3`) | `.collect(groupingBy(Employee::dept, collectingAndThen(toList(), g -> g.stream().sorted(bySalaryDesc).limit(3).toList())))` |
| `RANK()` / `DENSE_RANK()` | sort, then compute rank in a loop: `RANK` = index of the first row with an equal value + 1, `DENSE_RANK` = number of distinct values seen so far (no built-in collector) |
| `SUM(salary) OVER (PARTITION BY dept)` (group total on each row) | `var totals = employees.stream().collect(groupingBy(Employee::dept, summingDouble(Employee::salary)));`<br>`employees.stream().map(e -> new WithTotal(e, totals.get(e.dept())))` |
| `AVG(salary) OVER ()` (overall on each row) | `double avg = ...;` then `.map(e -> new WithAvg(e, avg))` |
| Running total `SUM(salary) OVER (ORDER BY hired)` | `employees.stream().sorted(comparing(Employee::hired)).map(Employee::salary).gather(Gatherers.scan(() -> 0.0, Double::sum))` (Java 24+) |
| Moving average `ROWS BETWEEN 2 PRECEDING AND CURRENT ROW` | `.gather(Gatherers.windowSliding(3)).map(w -> w.stream().mapToDouble(Employee::salary).average().orElse(0))` (emits only full windows; no partial window at the start) |
| Fixed-size buckets `NTILE(n)` | `.gather(Gatherers.windowFixed(size))` (buckets of equal size, the last may be smaller) |
| `LAG(salary, 1) OVER (ORDER BY hired)` | `.gather(Gatherers.windowSliding(2)).map(w -> new Delta(w.get(1), w.get(0)))` (the first row, where `LAG` is `NULL`, is dropped), or index into the sorted list: `i > 0 ? list.get(i - 1) : null` |
| `LEAD(salary, 1) OVER (ORDER BY hired)` | same as `LAG` but use `list.get(i + 1)` when `i + 1 < list.size()` |
| `FIRST_VALUE` / `LAST_VALUE` per partition | `groupingBy(f, collectingAndThen(toList(), g -> g.stream().sorted(c).toList()))`, then take `getFirst()` / `getLast()` of each group |
| `PERCENT_RANK`, `CUME_DIST` | `(rank - 1) / (n - 1)` and `rowsUpToAndIncludingPeers / n` computed on the sorted list |

## Clause evaluation order

| SQL clause | Evaluated order in SQL | Stream equivalent order |
|-----------|-----------------------|-------------------------|
| `FROM` / `JOIN` | 1 | source stream, `flatMap` / lookup |
| `WHERE` | 2 | `filter` (do it as early as possible) |
| `GROUP BY` | 3 | `collect(groupingBy(...))` |
| `HAVING` | 4 | `filter` on the grouped `entrySet()` stream |
| `SELECT` / window functions | 5 | `map` / post-processing of groups |
| `DISTINCT` | 6 | `distinct()` |
| `ORDER BY` | 7 | `sorted` |
| `LIMIT` / `OFFSET` | 8 | `skip` + `limit` |

## Caveats

- A `Stream` is single-use; materialize with `toList()` where SQL would reuse a CTE or view.
- No query planner or indexes: joins are what you build. Use a `Map` (hash join) instead of nested loops where possible.
- `NULL` semantics differ: SQL comparisons with `NULL` are unknown (filtered out), Java throws `NullPointerException` on `e.dept().equals(...)` when the value is `null`. Use `Objects.equals` or `"IT".equals(e.dept())`.
- `groupingBy` drops groups with no rows and does not return groups in a defined order (use `TreeMap::new` or `LinkedHashMap::new`).
- `toMap` throws `IllegalStateException` on duplicate keys; pass a merge function.
- `sorted` is stable for ordered streams, matching a stable `ORDER BY` only when you add tiebreakers.
- `AVG` on empty input is `NULL` in SQL; `average()` returns an empty `OptionalDouble`; `averagingDouble` returns `0.0`.
- `parallelStream()` is rarely worthwhile for in-memory data of this size; measure first.
- For large data keep the work in the database; streams are for in-memory collections.
