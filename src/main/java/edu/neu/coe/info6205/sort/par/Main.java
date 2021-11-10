package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.Scanner;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        processArgs(args);

        System.out.println("Enter cutoff:");
        Scanner scanner = new Scanner(System.in);
        int cut_off = scanner.nextInt();

        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        Random random = new Random();
        int array_size = 4000000;
        int[] array = new int[array_size];
        ArrayList<Long> timeList = new ArrayList<>();

        for (int threadCount=1; threadCount< 10; threadCount= threadCount*2){
            ForkJoinPool myPool = new ForkJoinPool(threadCount);
            double avg = 0.0;
            double min = 99999;
            int best_estimated_cutoff = 0;
            for (int j = 50; j < 100; j++) {
                ParSort.cutoff = cut_off * (j + 1);
                // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                long time;
                long startTime = System.currentTimeMillis();
                for (int t = 0; t < 10; t++) {
                    for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                    ParSort.sort(array, 0, array.length, myPool);
                }
                long endTime = System.currentTimeMillis();
                time = (endTime - startTime);
                avg = (avg + time)/ 10;
                if (time/10 < min){
                    min = time/10;
                    best_estimated_cutoff = cut_off * (j + 1);
                }
                timeList.add(time);

            }
            System.out.println("Thread Number: "+threadCount+ " cutoff：" + best_estimated_cutoff + "\t\t Average time:" + avg/50 + "ms");
        }


        try {
            FileOutputStream fis = new FileOutputStream("./src/result.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 50;
            int th = 0;
            for (long i : timeList) {
                String content = (double) cut_off * (j + 1) / array_size + "," + (double) i / 10 + ","+ th+ "\n";
                j++;

                if (j==100){
                    j = 50;
                    th++;
                }
                bw.write(content);
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}
