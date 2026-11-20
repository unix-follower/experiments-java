package org.example.experiments.lambda;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class EffectivelyFinalApp {
    private static int counter;

    private EffectivelyFinalApp() {
    }

    static void main() {
        System.out.println("counter before: " + counter);
        Runnable runnable = () -> counter++;
        runnable.run();
        System.out.println("counter after: " + counter);
        System.out.println();

        lambdaWithModificationOfExternalState();
        lambdaWithAnonymousClassModificationOfExternalState();
    }

    private static void lambdaWithModificationOfExternalState() {
        var truth = new AtomicBoolean(false);
        int[] count = {0};
        String[] str = {"Hello"};
        Runnable runnable = () -> {
            truth.set(true);
            count[0] += 1;
            str[0] += ", world";
        };
        runnable.run();
        System.out.println("truth after: " + truth);
        System.out.println("count after: " + count[0]);
        System.out.println("str after: " + str[0]);
    }

    private static void lambdaWithAnonymousClassModificationOfExternalState() {
        Object[] results = new Object[3];
        Runnable anonymousObj = () -> new Runnable() {
            AtomicBoolean truth = new AtomicBoolean(false);
            int count = 0;
            String str = "Hello";

            @Override
            public void run() {
                truth.set(true);
                results[0] = truth;
                count += 1;
                results[1] = count;
                str += ", world";
                results[2] = str;
            }
        }.run();
        anonymousObj.run();
        System.out.println("results after: " + Arrays.toString(results));
    }
}
