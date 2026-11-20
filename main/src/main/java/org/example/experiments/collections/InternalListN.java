package org.example.experiments.collections;

//import java.util.ImmutableCollections; // java:S125

import java.util.List;

public class InternalListN {
    private InternalListN() {
    }

    @SuppressWarnings({"java:S106", "java:S125"})
    static void main() {
        List<Integer> list = List.of(1, 2, 3, 4, 5); // returns ImmutableCollections>ListN

        // Compile error due to it's package-private
        // ImmutableCollections.listFromTrustedArray(1,2,3,4,5);
        System.out.println(list);
    }
}
