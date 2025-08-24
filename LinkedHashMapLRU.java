
package com.mehal.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapLRU<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {
    private final int capacity;

    public LinkedHashMapLRU(int capacity) {
        super(16, 0.75f, true); // access-order
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    @Override
    public V get(Object key) { return super.get(key); }

    @Override
    public void put(K key, V value) { super.put(key, value); }

    @Override
    public int size() { return super.size(); }

    @Override
    public void clear() { super.clear(); }
}
