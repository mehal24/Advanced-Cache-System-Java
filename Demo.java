
package com.mehal.cache;

public class Demo {
    public static void main(String[] args) {
        Cache<Integer, String> lru = new LRUCache<>(2);
        lru.put(1, "A");
        lru.put(2, "B");
        System.out.println(lru.get(1)); // A
        lru.put(3, "C"); // evicts 2
        System.out.println(lru.get(2)); // null

        Cache<Integer, String> lfu = new LFUCache<>(2);
        lfu.put(1, "A");
        lfu.put(2, "B");
        lfu.get(1); // freq(1)=2
        lfu.put(3, "C"); // evicts key 2 (lower freq)
        System.out.println(lfu.get(2)); // null
        System.out.println(lfu.get(1)); // A
        System.out.println(lfu.get(3)); // C

        Cache<Integer, String> mru = new MRUCache<>(2);
        mru.put(1, "A");
        mru.put(2, "B");
        mru.get(2); // 2 is MRU
        mru.put(3, "C"); // evicts 2
        System.out.println(mru.get(2)); // null
        System.out.println(mru.get(1)); // A or null depending on last access
        System.out.println(mru.get(3)); // C

        System.out.println("Demo complete.");
    }
}
