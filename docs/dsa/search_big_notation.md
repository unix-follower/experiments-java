| Notation         | Name             | Description                                                                 | Example Java Algorithm           |
|------------------|------------------|-----------------------------------------------------------------------------|----------------------------------|
| O(n)             | Linear Search    | Checks each element in sequence until the target is found or end is reached.| Custom implementation, `Arrays.asList(array).contains(value)` |
| O(log n)         | Binary Search    | Repeatedly divides a sorted array in half to find the target.               | `Arrays.binarySearch(array, value)` |
| O(1) average     | Hash Table Lookup| Uses a hash function to directly access the value.                          | `HashMap.get(key)`               |
| O(n)             | Breadth-First Search (BFS) | Explores all neighbors at the present depth before moving to the next level (used in graphs/trees). | Custom implementation using `Queue` |
| O(n)             | Depth-First Search (DFS)   | Explores as far as possible along each branch before backtracking (used in graphs/trees). | Custom implementation using `Stack` or recursion |
| O(log n) average | Search in Balanced BST     | Searches in a balanced binary search tree (e.g., AVL, Red-Black Tree).     | `TreeSet.contains(value)`, `TreeMap.get(key)` |

---

### **References**

- [GeeksforGeeks: Search Algorithms](https://www.geeksforgeeks.org/searching-algorithms/)
- [Big-O Cheat Sheet](https://www.bigocheatsheet.com/)
- [Oracle Java Documentation: Arrays.binarySearch()](https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#binarySearch-int:A-int-)
- [Oracle Java Documentation: HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)
- [Wikipedia: Search Algorithm](https://en.wikipedia.org/wiki/Search_algorithm)
