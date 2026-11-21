# Java `equals`, `hashCode` and `compare`: rules, pitfalls and collection behavior

## 1. General rules

### 1.1 `Object.equals(Object)`

| Rule | Meaning | Common way to break it |
|---|---|---|
| Reflexive | `x.equals(x)` is `true` | Comparing `float`/`double` with `==` (NaN) |
| Symmetric | `x.equals(y)` ⇔ `y.equals(x)` | Subclass adds a field, or a class tolerates comparison with a foreign type (e.g. case-insensitive `String` wrapper) |
| Transitive | `x=y` and `y=z` ⇒ `x=z` | Mixed `instanceof` hierarchies, tolerance-based comparison (`abs(a-b) < eps`) |
| Consistent | Repeated calls give the same result while the compared state is unchanged | Depending on mutable state, time, random values, or external resources |

### 1.2 `Object.hashCode()`

| Rule | Meaning |
|---|---|
| Internal consistency | Within one JVM run, the value must not change unless fields used by `equals` change |
| **Equal ⇒ same hash** | If `a.equals(b)` then `a.hashCode() == b.hashCode()` (the core contract) |
| Unequal ⇒ anything | Different hashes are not required, but better distribution means better performance |
| No cross-run stability | Not guaranteed between JVM runs (`Object.hashCode` is identity-based). `String`, `Integer`, `List` etc. have specified algorithms |
| Same fields | `hashCode` must use a subset of the fields used in `equals`, never more |
| Always override together | Override `hashCode` whenever you override `equals`, and vice versa |

### 1.3 `Comparable.compareTo` / `Comparator.compare`

| Rule | Meaning |
|---|---|
| Antisymmetry | `sgn(x.compareTo(y)) == -sgn(y.compareTo(x))`, including that an exception thrown in one direction must be thrown in the other |
| Transitivity | `x>y` and `y>z` ⇒ `x>z` |
| Substitutivity | `x.compareTo(y)==0` ⇒ `sgn(x.compareTo(z)) == sgn(y.compareTo(z))` for all `z` |
| Consistency with equals (*strongly recommended*) | `(x.compareTo(y)==0) == x.equals(y)`. Required for sorted sets and maps to honor the `Set`/`Map` contracts |
| Null | `x.compareTo(null)` must throw `NPE` (not return a value). `Comparator` may define its own null handling (`Comparator.nullsFirst/nullsLast`) |
| Type | May throw `ClassCastException` for incompatible types |
| Violation detection | `TimSort`/`List.sort` may throw `IllegalArgumentException: Comparison method violates its general contract!` (only when the data happens to trigger it) |

### 1.4 How the three relate

| Pair | Relationship |
|---|---|
| `equals` ↔ `hashCode` | **Mandatory**: equal objects must have equal hashes |
| `equals` ↔ `compareTo` | **Recommended**: `compareTo == 0` should mean `equals == true` |
| `hashCode` ↔ `compareTo` | No direct relation, but via equals, `compareTo == 0` should imply equal hashes |

### 1.5 Standard implementation template

```java
@Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Point p)) return false;   // or getClass() != o.getClass() (see §2)
    return x == p.x && Objects.equals(name, p.name);
}
@Override public int hashCode() { return Objects.hash(x, name); }
@Override public int compareTo(Point o) {
    int c = Integer.compare(x, o.x);             // never `x - o.x` (overflow)
    return c != 0 ? c : name.compareTo(o.name);
}
// Or: static final Comparator<Point> CMP = Comparator.comparingInt(Point::x).thenComparing(Point::name);
```

Records generate `equals`/`hashCode` from all components, with no specified hash formula.

---

## 2. Combinations and conditions that break the rules

### 2.1 Field modifiers and field types

| Situation | What goes wrong | Recommendation |
|---|---|---|
| **`transient` field used in `equals`/`hashCode`** | After deserialization it is `0`/`null`/`false`. The deserialized copy is no longer equal to the original, and a hash computed before serialization differs after. `HashMap`/`HashSet` re-hash keys on deserialization, so a key whose hash depends on transient or not-yet-restored state ends up in the wrong bucket | Don't use `transient` fields in `equals`/`hashCode`, or restore them in `readObject`/`readResolve`. Caches of a computed hash may be `transient` (that is the standard idiom, as in `String`) |
| **`transient` as such** | It has no effect on `equals`/`hashCode`. `equals` doesn't "skip" it automatically. Reflection-based generators (Lombok, Apache `EqualsBuilder.reflectionEquals`) skip it by default, which can differ from your handwritten code | Be explicit about which fields participate |
| **`volatile` field** | Only guarantees visibility and ordering for single reads/writes, **not** immutability. If another thread changes it, `hashCode` changes and the key is "lost" in a hash collection. Two volatile reads in `equals` aren't atomic | Use only immutable (`final`) state for identity. Take a snapshot into a local variable if you must read volatile state |
| **Non-final / mutable field** | The hash changes after insertion into `HashMap`/`HashSet`, so `contains`/`remove` fail and the entry leaks. In `TreeMap`/`TreeSet` the ordering invariant breaks | Use immutable keys; never mutate fields that participate in equality while the object is in a collection |
| **`null` fields** | NPE from `this.name.equals(...)` | `Objects.equals`, `Objects.hashCode`, `Objects.hash` (`null` hashes to `0`) |
| **`null` as a key/element** | `HashMap`/`HashSet`/`LinkedHashMap`: one `null` key allowed (bucket 0). `TreeMap`/`TreeSet` (natural ordering): NPE. `Hashtable`, `ConcurrentHashMap`, `ConcurrentSkipListMap`, `PriorityQueue`, `ArrayDeque`, `List.of`/`Set.of`/`Map.of`, blocking queues: NPE | Check the collection's null policy. Use `Comparator.nullsFirst(...)` for sorted collections |
| **`float`/`double`** | `==` breaks reflexivity for `NaN`. `Double.equals`/`compare` distinguish `0.0` from `-0.0` and treat `NaN` as equal to itself | Use `Double.compare` and `Double.hashCode`. Avoid floating-point keys, and never use epsilon equality for `equals` (non-transitive) |
| **`BigDecimal`** | `equals` compares scale (`1.0` ≠ `1.00`), `compareTo` does not. `HashSet` and `TreeSet` therefore disagree | `stripTrailingZeros()` before hashing/equality, or use a consistent comparator |
| **Arrays as fields or keys** | `array.equals` is identity, `array.hashCode` is identity | `Arrays.equals/deepEquals/hashCode/deepHashCode`. Never use arrays as map keys |
| **Collections as fields** | Mutable contents change the hash. Equality across `List`/`Set` types differs (`List` ≠ `Set`) | Defensive copies, or immutable collections |
| **Cyclic references** (parent ↔ child) | `StackOverflowError` | Exclude the back-reference from `equals`/`hashCode`/`toString` |
| **Lazy-loaded proxies (e.g. JPA entities)** | `getClass()` comparison fails for proxy subclasses. Fields may be uninitialized | Use `instanceof` and accessors, or a business key |

### 2.2 Inheritance and design

| Situation | What goes wrong | Recommendation |
|---|---|---|
| `instanceof` + subclass adds state | Symmetry breaks: `parent.equals(child)` is true, `child.equals(parent)` false | Make the class `final`, or define equality only in the base class (final method), or use composition |
| `getClass() ==` comparison | Symmetric and transitive but violates Liskov substitution (anonymous subclasses, proxies are "different") | Acceptable when subclassing isn't a use case. Document the choice |
| Overriding only `equals` or only `hashCode` | Hash collections can't find equal objects | Always override both (static analysis flags this) |
| Overloading `equals(Foo)` | The collection calls `equals(Object)`, so the default identity version runs | Use `@Override` |
| `compareTo` using subtraction | `Integer.MIN_VALUE - 1` overflows, breaking transitivity | `Integer.compare`, `Long.compare`, `Double.compare` |
| `compareTo` inconsistent with `equals` | `TreeSet` treats `compare == 0` as a duplicate even if `equals` says false. `HashSet` behaves differently from `TreeSet` | Add tie-breaker fields until `compare == 0` ⇔ `equals` |
| Comparator based on random/time/changing state | `IllegalArgumentException` in sort, corrupted trees | Comparators must be pure and deterministic |
| Case-insensitive/locale-dependent comparisons | `equals` may disagree with the comparator (`String.CASE_INSENSITIVE_ORDER`) | Normalize into a canonical key, or use a `TreeMap` with that comparator knowingly |
| Mixed-type collection with `Comparable` | `ClassCastException` | Typed collections, or a custom `Comparator` |
| Using `Object.hashCode` ordering | `Set.of`/`Map.of` iteration order is randomized per JVM run, and identity hashes vary | Never rely on iteration order of hash collections. Use `LinkedHash*` or `Tree*` |
| Argument direction in collections | `ArrayList.contains(o)` calls `o.equals(element)` | A non-symmetric `equals` gives different results depending on the collection |

---

## 3. Hash collision mitigation

### 3.1 General techniques

| Technique | Description | Trade-off |
|---|---|---|
| Good mixing function | Combine fields with a multiplier and shifts (`31*h + f`; `h ^ (h>>>16)`), avalanche properties | Cheap, but weak mixing causes clustering |
| Odd/prime multipliers | Avoid patterns of power-of-two collisions | `31` is cheap (`(h<<5)-h`) |
| Use all significant fields | Don't hash just one low-entropy field | Slightly higher cost |
| Lower load factor / resize | Fewer elements per bucket | More memory |
| Separate chaining | Colliding entries in a list or tree per bucket | Pointer overhead, cache-unfriendly |
| Open addressing (linear/quadratic probing, double hashing) | Collisions stored in other slots of the table | Clustering, needs lower load factor, deletion is harder |
| Cuckoo / hopscotch / Robin Hood hashing | Bounded lookup cost via multiple candidate slots | More complex |
| Tree buckets | Collision chains become balanced trees: O(n) → O(log n) | Needs ordering (`Comparable`) for efficiency |
| Randomized / seeded hash (SipHash, per-process salt) | Defeats hash-flooding DoS attacks | Not stable across runs |
| Universal hashing | Randomly chosen hash function from a family | Overhead |
| Cryptographic hash | Collision resistance for identity (not for tables) | Slow, not meant for bucket indexing |
| Cache the hash | Compute once for immutable objects | Extra field |

### 3.2 Java-specific mechanisms

| Mechanism | Where | Details |
|---|---|---|
| **Hash spreading** | `HashMap`, `ConcurrentHashMap` | `h ^ (h >>> 16)` (CHM additionally `& 0x7fffffff`) so high bits influence the index in power-of-two tables |
| **Power-of-two table** | `HashMap`, `HashSet`, `LinkedHashMap` | `index = (n-1) & hash`, cheap, but needs spreading. Resize splits buckets without rehashing |
| **Load factor 0.75** | `HashMap` and friends | Resize when `size > capacity × 0.75`. Presize with `HashMap.newHashMap(n)` (Java 19+) or `new HashMap<>((int)(n/0.75f)+1)` |
| **Treeification** | `HashMap`, `ConcurrentHashMap` | A bin with ≥ 8 nodes becomes a red-black tree **only if** table size ≥ 64 (otherwise the table resizes). Untreeify at ≤ 6. Tree nodes are ordered by hash, then by `Comparable` (if the key class implements `Comparable<itself>`), then by tie-breaking (`identityHashCode`/class name), so **implementing `Comparable` on keys keeps worst-case lookup at O(log n)** |
| **Cached `String` hash** | `String` | `hash` field plus `hashIsZero` flag. Algorithm specified: `s[0]*31^(n-1)+…` |
| **Helper utilities** | `Objects.hash`, `Arrays.hashCode`, `Double.hashCode`, `Long.hashCode`, `List.hashCode` | `Objects.hash(...)` allocates a varargs array and boxes primitives. Prefer hand-written code in hot paths |
| **Identity-based maps** | `IdentityHashMap` | `System.identityHashCode` + `==`, linear probing in a single array (no buckets) |
| **Legacy `Hashtable`** | `Hashtable` | `(hash & 0x7FFFFFFF) % length`, odd sizes (11, 2n+1), chains only, no treeification, synchronized |
| **Removed alternative hashing** | Java 7 only | `jdk.map.althashing.threshold` for `String` keys was removed in Java 8 in favor of tree bins |
| **No hashing** | `EnumMap`, `EnumSet` | Indexed by ordinal |
| **Third-party** | Guava `Hashing`, Apache `HashCodeBuilder`, Eclipse Collections | Better mixing (Murmur3, SipHash) |

Java does **not** salt hashes for `String`, so hash-flooding on `String` keys is mitigated only by tree bins, which is why `Comparable` keys matter.

---

## 4. Effect on collection families

### 4.1 Overview by access structure

| Family | Examples | `equals` | `hashCode` | `compare` | Lookup cost |
|---|---|---|---|---|---|
| **Random access (array-backed)** | `ArrayList`, `Vector`, `ArrayDeque`, `Arrays.asList`, `CopyOnWriteArrayList` | Used by `contains`, `indexOf`, `lastIndexOf`, `remove(Object)` | Not used (only by the collection's own `hashCode()`) | Used only by sorting and `binarySearch` | `get(i)` O(1). By value O(n) via `equals`. Binary search O(log n) via `compare` |
| **Bucket-based (hash)** | `HashMap`, `HashSet`, `LinkedHashMap/Set`, `Hashtable`, `WeakHashMap`, `ConcurrentHashMap` | Final check after hash match within the bucket | **Decisive**: picks the bucket | Only as a tree-bin tie-breaker (`Comparable`) | O(1) average. O(log n) worst case with tree bins. O(n) without them |
| **Linked-list based** | `LinkedList`, `ConcurrentLinkedQueue/Deque`, `LinkedBlockingQueue/Deque` | Used by `contains`, `indexOf`, `remove(Object)` (O(n)) | Not used | Only when sorted explicitly | `get(i)` O(n). By value O(n) |
| **Tree-based (sorted)** | `TreeMap`, `TreeSet`, `ConcurrentSkipListMap/Set` | **Not used**: `compare == 0` means "same key" | Not used | **Decisive**: ordering, lookup, duplicates | O(log n) |
| **Heap-based** | `PriorityQueue`, `PriorityBlockingQueue`, `DelayQueue` | `contains`, `remove(Object)` O(n) | Not used | **Decisive** for poll/peek order. Iteration order is *not* sorted | `peek` O(1), `offer`/`poll` O(log n) |
| **Identity-based** | `IdentityHashMap` | Ignored (`==` is used) | Ignored (`identityHashCode` is used) | Not used | O(1) |
| **Ordinal-based** | `EnumMap`, `EnumSet` | Not used | Not used | Not used (enum natural order) | O(1) |
| **Immutable factories** | `List.of`, `Set.of`, `Map.of` | `Set.of`/`Map.of` reject duplicates by `equals` (`IllegalArgumentException`) | Used for the probe table in `Set.of`/`Map.of` | Not used | O(1) for sets/maps, nulls rejected |

### 4.2 What goes wrong per family when the contract is violated

| Family | `equals` broken | `hashCode` broken (equal objects, different hashes) | `compare` broken or inconsistent |
|---|---|---|---|
| Array-backed list | `contains`/`remove` give wrong results | No effect | Wrong sort order, `binarySearch` returns garbage if not sorted by the same comparator, possible `IllegalArgumentException` from TimSort |
| Hash collections | Duplicate or missing entries | **Entries unfindable**, duplicates in sets, silent memory leaks | Only tree-bin ordering degrades (still correct, but slower) |
| Linked list | Same as array list | No effect | Same as array list |
| `TreeMap`/`TreeSet` | No effect | No effect | **Missing or duplicate entries**, corrupted ordering, `get` fails, inconsistent with `equals` ⇒ `Set` contract violated |
| `PriorityQueue` | `remove(Object)`/`contains` wrong | No effect | Wrong poll order |
| Mutable key (any hash/tree) | Lost entries | Lost entries | Lost entries |

### 4.3 Operation matrix

| Operation | Array list | Linked list | Hash map/set | Tree map/set | Priority queue |
|---|---|---|---|---|---|
| `add`/`put` duplicate detection | None for lists | None for lists | hash then `equals` | `compare == 0` | None (duplicates allowed) |
| `contains`/`containsKey` | `equals`, O(n) | `equals`, O(n) | hash + `equals` | `compare` | `equals`, O(n) |
| `remove(Object)` | `equals`, O(n) | `equals`, O(n) | hash + `equals` | `compare` | `equals`, O(n) |
| `containsValue` (maps) | n/a | n/a | `equals`, O(n) | `equals`, O(n) | n/a |
| Collection `equals(Object)` | Element-wise, ordered | Element-wise, ordered | Set: size + `containsAll`. Map: entry-wise | Same as hash (type-agnostic) | Not overridden (identity) |
| Collection `hashCode()` | `31*h + e.hashCode()` | Same | Set: sum of element hashes. Map: sum of `key^value` hashes | Same | Not overridden (identity) |
| Sort | `compare` (stable) | `compare` (copies to array first) | n/a | Sorted by definition | n/a |

---

## 5. `java.util.concurrent` collections

### 5.1 Behavior per class

| Class | Structure | `equals` usage | `hashCode` usage | `compare` usage | Nulls | Concurrency notes |
|---|---|---|---|---|---|---|
| **`ConcurrentHashMap`** | Bucket array, tree bins | Key match inside bin. Value `equals` in `replace(k,old,new)`, `remove(k,v)`, `containsValue` | Chooses bin (spread `h ^ h>>>16`, `& HASH_BITS`) | Tree-bin ordering (`Comparable` keys) | **No null keys/values** | Lock-free reads. Writes use CAS for an empty bin, otherwise `synchronized` on the bin's first node. Iterators are weakly consistent. `size()` is approximate under concurrent updates |
| `ConcurrentHashMap.newKeySet()` | Set view over CHM | As above | As above | As above | No nulls | Preferred concurrent `HashSet` |
| **`ConcurrentSkipListMap` / `Set`** | Skip list (lock-free, CAS) | Only `containsValue` and value-based `replace`/`remove`. `Set.equals` is the standard one | Not used | **Everything**: ordering and key identity | No null keys/values | O(log n) expected. `size()` is O(n). The comparator must be thread-safe, pure and consistent |
| **`CopyOnWriteArrayList`** | Array snapshot copied on every write | `contains`, `indexOf`, `remove(Object)`, `addIfAbsent` (O(n)) | Not used (list-level hash only) | `sort` only | Nulls allowed | Iterators are snapshots (never throw `ConcurrentModificationException`), writes are O(n) |
| **`CopyOnWriteArraySet`** | Backed by `CopyOnWriteArrayList` | Duplicate detection via `addIfAbsent` (O(n)) | Not used | Not used | Nulls allowed | Only for small, read-mostly sets. **Not** hash-based despite the name |
| **`ConcurrentLinkedQueue` / `Deque`** | Lock-free linked nodes | `contains`, `remove(Object)`: O(n) | Not used | Not used | No nulls | Weakly consistent iterators. `size()` is O(n) |
| **`LinkedBlockingQueue` / `Deque`** | Linked nodes, two locks (queue) or one lock (deque) | `contains`, `remove(Object)` under full lock | Not used | Not used | No nulls | Searching blocks producers and consumers while `equals` runs |
| **`ArrayBlockingQueue`** | Circular array, single lock | `contains`, `remove(Object)` under lock | Not used | Not used | No nulls | Same lock-hold caveat |
| **`PriorityBlockingQueue`** | Binary heap array, single lock | `contains`, `remove(Object)` | Not used | **Heap order** (`compare` is called under the lock). Elements must be mutually comparable | No nulls | Unbounded. Iteration order is not sorted. Slow comparators block all threads |
| **`DelayQueue`** | Heap (wraps a `PriorityQueue`) | `remove(Object)` | Not used | `Delayed extends Comparable<Delayed>`: `compareTo` should order by expiry, consistent with `getDelay` | No nulls | A comparator inconsistent with `getDelay` delays elements incorrectly |
| **`SynchronousQueue` / `LinkedTransferQueue`** | Handoff / lock-free linked | `contains`/`remove(Object)` (`SynchronousQueue` always reports empty/false) | Not used | Not used | No nulls | n/a |
| **`ConcurrentMap` default methods** (`putIfAbsent`, `replace(k,old,new)`, `remove(k,v)`) | n/a | **Value `equals`** decides whether the atomic conditional applies | Key hashing as per implementation | n/a | n/a | An inconsistent `equals` makes conditional updates fail or succeed wrongly |
| `AtomicReference.compareAndSet` | n/a | **Not used. Identity (`==`)** | n/a | n/a | n/a | Common mistake: expecting `equals` semantics |

### 5.2 Concurrency-specific rules

| Concern | Rule |
|---|---|
| Immutability | Keys and elements should be effectively immutable, since a concurrent mutation changes the hash or order while another thread is probing |
| Thread safety of the methods themselves | `equals`/`hashCode`/`compareTo` must be side-effect free and thread-safe. They are called concurrently from many threads |
| Locks held during callbacks | In `ConcurrentHashMap.compute/merge/computeIfAbsent`, the bin lock is held while your lambda and the key's `equals`/`hashCode` run. Keep them short, never modify the same map inside the lambda (this can deadlock or throw `IllegalStateException`), and never call blocking code |
| Same for blocking queues | `contains`/`remove(Object)`/`PriorityBlockingQueue` comparisons run under the queue lock, so slow `equals`/`compare` hurts throughput |
| Weak consistency | Iterators of CHM, skip-list and concurrent queues reflect some state at or after creation and don't throw `ConcurrentModificationException`. `size()` and `isEmpty()` may be stale |
| Collision storms | CHM treeifies bins as `HashMap` does, so `Comparable` keys keep worst-case O(log n). Skip lists are unaffected by hashes |
| Null policy | Almost all concurrent collections reject `null` (ambiguity between "absent" and `null` in lock-free code). Only the `CopyOnWrite*` classes accept it |
| Synchronized wrappers | `Collections.synchronizedMap/List` delegate to the wrapped collection. Equality and hash rules are the same, but iteration still needs manual synchronization |

---

## 6. Quick checklist

| # | Check |
|---|---|
| 1 | Override `equals` and `hashCode` together, using the same fields |
| 2 | Use immutable (`final`) fields for anything participating in equality or ordering |
| 3 | Use `Objects.equals`/`Objects.hash`, `Double.compare`, `Arrays.*` helpers |
| 4 | Don't use `transient`, `volatile` or lazily initialized fields in `equals`/`hashCode` |
| 5 | Choose `getClass()` vs `instanceof` deliberately, or make the class `final`/a record |
| 6 | For sorted collections, make `compare` consistent with `equals`, never use subtraction, and add tie-breakers |
| 7 | Implement `Comparable` on hash keys to keep tree bins efficient under collisions |
| 8 | Presize hash collections and avoid mutable or array keys |
| 9 | In concurrent code, keep `equals`/`hashCode`/`compare` fast, pure and thread-safe, because they may run under internal locks |
