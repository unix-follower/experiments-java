package org.example.experiments.collections;

import java.util.LinkedHashMap;
import java.util.TreeMap;

public class MapNullability {
    private MapNullability() {
    }

    @SuppressWarnings({
            "java:S4030", "java:S106", "MismatchedQueryAndUpdateOfCollection", "PMD.AvoidCatchingGenericException"})
    static void main() {
        var strLinkedHashMap = new LinkedHashMap<String, Integer>();
        strLinkedHashMap.put("1", null);
        strLinkedHashMap.put(null, null);
        var intLinkedHashMap = new LinkedHashMap<Integer, Integer>();
        intLinkedHashMap.put(1, null);
        intLinkedHashMap.put(null, null);
        var longLinkedHashMap = new LinkedHashMap<Long, Integer>();
        longLinkedHashMap.put(1L, null);
        longLinkedHashMap.put(null, null);
        var doubleLinkedHashMap = new LinkedHashMap<Double, Integer>();
        doubleLinkedHashMap.put(1.0, null);
        doubleLinkedHashMap.put(null, null);
        var floatLinkedHashMap = new LinkedHashMap<Float, Integer>();
        floatLinkedHashMap.put(1.0F, null);
        floatLinkedHashMap.put(null, null);
        var boolLinkedHashMap = new LinkedHashMap<Boolean, Integer>();
        boolLinkedHashMap.put(Boolean.TRUE, null);
        boolLinkedHashMap.put(Boolean.FALSE, null);
        boolLinkedHashMap.put(null, null);

        var strTreeMap = new TreeMap<String, Integer>();
        strTreeMap.put("1", null);
        try {
            strTreeMap.put(null, null);
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }

        var intTreeMap = new TreeMap<Integer, Integer>();
        intTreeMap.put(1, null);
        try {
            intTreeMap.put(null, null);
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }

        var longTreeMap = new TreeMap<Long, Integer>();
        longTreeMap.put(1L, null);
        try {
            longTreeMap.put(null, null);
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }

        var doubleTreeMap = new TreeMap<Double, Integer>();
        doubleTreeMap.put(1.0, null);
        try {
            doubleTreeMap.put(null, null);
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }

        var floatTreeMap = new TreeMap<Float, Integer>();
        floatTreeMap.put(1.0F, null);
        try {
            floatTreeMap.put(null, null);
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }

        var boolTreeMap = new TreeMap<Boolean, Integer>();
        boolTreeMap.put(Boolean.TRUE, null);
        boolTreeMap.put(Boolean.FALSE, null);
        try {
            boolTreeMap.put(null, null);
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("All NPEs were handled!");
    }
}
