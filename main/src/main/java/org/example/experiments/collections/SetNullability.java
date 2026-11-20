package org.example.experiments.collections;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class SetNullability {
    private SetNullability() {
    }

    @SuppressWarnings({"java:S4030", "java:S106", "MismatchedQueryAndUpdateOfCollection"})
    static void main() {
        var strHashSet = new HashSet<String>();
        strHashSet.add(null);
        var intHashSet = new HashSet<Integer>();
        intHashSet.add(null);
        var longHashSet = new HashSet<Long>();
        longHashSet.add(null);
        var doubleHashSet = new HashSet<Double>();
        doubleHashSet.add(null);
        var floatHashSet = new HashSet<Float>();
        floatHashSet.add(null);
        var boolHashSet = new HashSet<Boolean>();
        boolHashSet.add(null);

        var strLinkedHashSet = new LinkedHashSet<String>();
        strLinkedHashSet.add(null);
        var intLinkedHashSet = new LinkedHashSet<Integer>();
        intLinkedHashSet.add(null);
        var longLinkedHashSet = new LinkedHashSet<Long>();
        longLinkedHashSet.add(null);
        var doubleLinkedHashSet = new LinkedHashSet<Double>();
        doubleLinkedHashSet.add(null);
        var floatLinkedHashSet = new LinkedHashSet<Float>();
        floatLinkedHashSet.add(null);
        var boolLinkedHashSet = new LinkedHashSet<Boolean>();
        boolLinkedHashSet.add(null);

        System.out.println("No NPE was thrown!");
    }
}
