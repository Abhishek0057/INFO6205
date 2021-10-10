package edu.neu.coe.info6205.union_find;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.Scanner;

public class Ufind {
    public static int generateRandomPairs(int N){
        UF_HWQUPC uf = new UF_HWQUPC(N, true);
        Random random = new Random();
        int r = 0;
        while (uf.components() !=1){
            int n1 = random.nextInt(N);
            int n2 = random.nextInt(N);
            uf.connect(n1,n2);
            r++;
        }
        return r;
    }
    public static void main(String arg[]){
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        Deque<Integer> d = new ArrayDeque<>();
        for(int i = 1;i<= 10; i++){
            d.push(generateRandomPairs(N));
        }
        int count = 0;
        while(!d.isEmpty()){
            count += d.pop();
        }

        System.out.println("Number of connections generated for N :"+N+ "is " +count/10);
    }
}

