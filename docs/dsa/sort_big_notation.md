| Notation           | Name            | Description                                                                 | Example Java Algorithm      |
|--------------------|-----------------|-----------------------------------------------------------------------------|----------------------------|
| O(n²)              | Bubble Sort     | Compares adjacent elements and swaps them if out of order; simple but slow. | `java.util.Arrays.sort()` (for small arrays, uses insertion sort) |
| O(n²)              | Selection Sort  | Repeatedly selects the minimum element and moves it to the sorted portion.  | Custom implementation      |
| O(n²)              | Insertion Sort  | Builds the sorted array one item at a time by inserting elements.           | `java.util.Arrays.sort()` (for small arrays) |
| O(n log n)         | Merge Sort      | Divides the array into halves, sorts, and merges them.                      | `java.util.Arrays.sort()` (for objects)      |
| O(n log n)         | Heap Sort       | Builds a heap and repeatedly extracts the maximum element.                  | Custom implementation      |
| O(n log n) average | Quick Sort      | Divides and conquers by partitioning; fast in practice, but O(n²) worst-case| `java.util.Arrays.sort()` (for primitives)   |
| O(n)               | Counting Sort   | Counts occurrences of each value; only for integers in a known range.       | Custom implementation      |
| O(n)               | Radix Sort      | Sorts numbers by processing individual digits.                              | Custom implementation      |
| O(n)               | Bucket Sort     | Distributes elements into buckets, sorts each, and concatenates.            | Custom implementation      |

---

### **References**

- [Big-O Cheat Sheet](https://www.bigocheatsheet.com/)
- [GeeksforGeeks: Sorting Algorithms](https://www.geeksforgeeks.org/sorting-algorithms/)
- [Oracle Java Documentation: Arrays.sort()](https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#sort-java.lang.Object:A-)
- [Wikipedia: Sorting Algorithm](https://en.wikipedia.org/wiki/Sorting_algorithm)
