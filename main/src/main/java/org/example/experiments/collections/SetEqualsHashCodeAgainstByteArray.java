package org.example.experiments.collections;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class SetEqualsHashCodeAgainstByteArray {
    private SetEqualsHashCodeAgainstByteArray() {
    }

    @SuppressWarnings({"java:S106"})
    static void main() {
        final String msg = "Hello, world!";
        final byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        final Set<byte[]> hashSet = new HashSet<>();
        hashSet.add(data);
        final byte[] data1 = msg.getBytes(StandardCharsets.UTF_8);
        final byte[] data2 = msg.getBytes(StandardCharsets.UTF_16);
        final byte[] data3 = msg.getBytes(StandardCharsets.US_ASCII);
        System.out.println(hashSet.contains(data));
        System.out.println(hashSet.contains(data1));
        System.out.println(hashSet.contains(data2));
        System.out.println(hashSet.contains(data3));
    }
}
