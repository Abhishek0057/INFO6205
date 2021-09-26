package edu.neu.coe.info6205.sort.simple;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.GenericSort;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.util.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;
import java.util.Collections;

/**
 * Unit tests which are in fact benchmarks of the various sort methods.
 * Keep in mind that we are sorting objects here (Integers). not primitives.
 */
public class Benchmarks {

    @BeforeClass
    public static void setupClass() {
        try {
            config = Config.load(Benchmarks.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test // Slow
    public void testBubbleSortBenchmark() {
        String description = "BubbleSort";
        Helper<Integer> helper = new BaseHelper<>(description, N, config);
        final GenericSort<Integer> sort = new BubbleSort<>(helper);
        runBenchmark(description, sort, helper);
    }

    @Test
    public void testInsertionSortBenchmark() {
        String description = "Insertion sort";
        Helper<Integer> helper = new BaseHelper<>(description, N, config);
        final GenericSort<Integer> sort = new InsertionSort<>(helper);
        runBenchmark(description, sort, helper);
    }

    @Test
    public void testInsertionSortOptBenchmark() {
        String description = "Optimized Insertion sort";
        Helper<Integer> helper = new BaseHelper<>(description, N, config);
        final GenericSort<Integer> sort = new InsertionSortOpt<>(helper);
        runBenchmark(description, sort, helper);
    }

    @Test
    public void testIntroSortBenchmark() {
        String description = "Intro sort";
        final Helper<Integer> helper = new BaseHelper<>(description, N, config);
        final GenericSort<Integer> sort = new IntroSort<>(helper);
        runBenchmark(description, sort, helper);
    }

    @Test
    public void testMergeSortBenchmark() {
        String description = "Merge sort";
        final Helper<Integer> helper = new BaseHelper<>(description, N, config);
        final GenericSort<Integer> sort = new MergeSort<>(helper);
        runBenchmark(description, sort, helper);
    }

    @Test
    public void testQuickSort3WayBenchmark() {
        String description = "3-way Quick sort";
        final Helper<Integer> helper = new BaseHelper<>(description, N, config);
        final GenericSort<Integer> sort = new QuickSort_3way<>(helper);
        runBenchmark(description, sort, helper);
    }

    @Test
    public void testQuickSortDualPivotSortBenchmark() {
        String description = "Dual-pivot Quick sort";
        final Helper<Integer> helper = new BaseHelper<>(description, N, config);
        final GenericSort<Integer> sort = new QuickSort_DualPivot<>(helper);
        runBenchmark(description, sort, helper);
    }

    @Test
    public void testSelectionSortBenchmark() {
        String description = "Selection sort";
        Helper<Integer> helper = new BaseHelper<>(description, N, config);
        final GenericSort<Integer> sort = new SelectionSort<>(helper);
        runBenchmark(description, sort, helper);
    }

    @Test
    public void testShellSortBenchmark() {
        String description = "3Shell sort";
        final Helper<Integer> helper = new BaseHelper<>(description, N, config);
        final GenericSort<Integer> sort = new ShellSort<>(3, helper);
        runBenchmark(description, sort, helper);
    }

    @Test
    public void testInsertionSortWithFourDifferentArraysBenchmark() {
        String description = "Insertion sort for Four Different Arrays";
        Helper<Integer> helper = new BaseHelper<>(description, N, config);
        final GenericSort<Integer> sort = new InsertionSortOpt<>(helper);
        runBenchmarkForInsertionSort(description, sort, helper);
    }

    public void runBenchmark(String description, GenericSort<Integer> sort, Helper<Integer> helper) {
        helper.init(N);
        Supplier<Integer[]> supplier = () -> helper.random(Integer.class, Random::nextInt);

        final Benchmark<Integer[]> benchmark = new Benchmark_Timer<>(
                description + " for " + N + " Integers",
                (xs) -> Arrays.copyOf(xs, xs.length),
                sort::mutatingSort,
                null
        );
        logger.info(Utilities.formatDecimal3Places(benchmark.runFromSupplier(supplier, 500)) + " ms");
    }

    public void runBenchmarkForInsertionSort(String description, GenericSort<Integer> sort, Helper<Integer> helper) {
        helper.init(N);

        // Random Array
        Random rd = new Random();
        Integer[] random_array = new Integer[N];
        for (int i = 0; i < random_array.length; i++)
            random_array[i] = rd.nextInt();
        Supplier<Integer[]> supplier = () -> random_array;

        //  Sorted / Ordered Array
        GenericSort<Integer> sorter = new InsertionSort<Integer>(helper);
        Integer[] sorted_array = sorter.sort(random_array);
        Supplier<Integer[]> sorted_supplier = () -> sorted_array;

        // Partial Sorted / Ordered Array
        Integer[] partial_array = new Integer[N];
        for (int i = 0; i < partial_array.length; i++) {
            if (i >= N / 2) {
                partial_array[i] = sorted_array[i];
            }
            partial_array[i] = random_array[i];
        }
        Supplier<Integer[]> partial_sorted_supplier = () -> partial_array;

        // Reverse Sorted Array
        Collections.reverse(Arrays.asList(random_array));
        Supplier<Integer[]> reverse_sorted_supplier = () -> random_array;

        final Benchmark<Integer[]> benchmark = new Benchmark_Timer<>(
                description + " for " + N + " Integers",
                (xs) -> Arrays.copyOf(xs, xs.length),
                sort::mutatingSort,
                null
        );
        logger.info(Utilities.formatDecimal3Places(benchmark.runFromSupplier(sorted_supplier, 500)) + " ms");
        logger.info(Utilities.formatDecimal3Places(benchmark.runFromSupplier(partial_sorted_supplier, 500)) + " ms");
        logger.info(Utilities.formatDecimal3Places(benchmark.runFromSupplier(supplier, 500)) + " ms");
        logger.info(Utilities.formatDecimal3Places(benchmark.runFromSupplier(reverse_sorted_supplier, 500)) + " ms");
    }

    final static LazyLogger logger = new LazyLogger(Benchmarks.class);

    public static final int N = 1000;

    private static Config config;

}
