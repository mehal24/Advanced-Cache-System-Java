
package com.mehal.cache;

import java.util.Random;

public class Benchmark {
    public static void main(String[] args) {
        final int OPS = 100_000;
        final int CAP = 10_000;
        final int KEY_SPACE = 50_000;
        Random rnd = new Random(42);

        benchmark("LRUCache", new LRUCache<Integer, Integer>(CAP), OPS, KEY_SPACE, rnd);
        benchmark("LinkedHashMapLRU", new LinkedHashMapLRU<Integer, Integer>(CAP), OPS, KEY_SPACE, rnd);
        benchmark("LFUCache", new LFUCache<Integer, Integer>(CAP), OPS, KEY_SPACE, rnd);
        benchmark("MRUCache", new MRUCache<Integer, Integer>(CAP), OPS, KEY_SPACE, rnd);
    }

    static void benchmark(String name, Cache<Integer, Integer> cache, int ops, int keySpace, Random rnd) {
        long start = System.nanoTime();
        int hits = 0;
        for (int i = 0; i < ops; i++) {
            int k = rnd.nextInt(keySpace);
            if (rnd.nextBoolean()) {
                Integer v = cache.get(k);
                if (v != null) hits++;
            } else {
                cache.put(k, k);
            }
        }
        long end = System.nanoTime();
        double ms = (end - start) / 1_000_000.0;
        System.out.printf("%s: time=%.2f ms, size=%d, hits=%d%n", name, ms, cache.size(), hits);
    }
}
