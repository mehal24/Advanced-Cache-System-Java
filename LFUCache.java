
package com.mehal.cache;

import java.util.HashMap;
import java.util.Map;

/** LFU cache with O(1) amortized get/put using freq buckets. */
public class LFUCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private int minFreq = 0;
    private final Map<K, Node<K, V>> keyToNode = new HashMap<>();
    private final Map<Integer, DoublyLinkedList<K, V>> freqToList = new HashMap<>();

    public LFUCache(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.capacity = capacity;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = keyToNode.get(key);
        if (node == null) return null;
        touch(node);
        return node.value;
    }

    @Override
    public void put(K key, V value) {
        if (capacity == 0) return;
        if (keyToNode.containsKey(key)) {
            Node<K, V> node = keyToNode.get(key);
            node.value = value;
            touch(node);
            return;
        }
        if (keyToNode.size() == capacity) {
            DoublyLinkedList<K, V> list = freqToList.get(minFreq);
            Node<K, V> toEvict = list.removeLast();
            if (toEvict != null) keyToNode.remove(toEvict.key);
        }
        Node<K, V> fresh = new Node<>(key, value);
        keyToNode.put(key, fresh);
        minFreq = 1;
        freqToList.computeIfAbsent(1, f -> new DoublyLinkedList<>()).addFirst(fresh);
    }

    private void touch(Node<K, V> node) {
        int f = node.freq;
        DoublyLinkedList<K, V> oldList = freqToList.get(f);
        oldList.remove(node);
        if (oldList.size() == 0 && f == minFreq) minFreq++;
        node.freq++;
        freqToList.computeIfAbsent(node.freq, k -> new DoublyLinkedList<>()).addFirst(node);
    }

    @Override
    public int size() { return keyToNode.size(); }

    @Override
    public void clear() {
        keyToNode.clear();
        freqToList.clear();
        minFreq = 0;
    }
}
