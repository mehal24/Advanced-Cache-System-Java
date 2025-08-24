
package com.mehal.cache;

import java.util.HashMap;
import java.util.Map;

/** MRU: evict the most recently used (head) */
public class MRUCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final DoublyLinkedList<K, V> dll;

    public MRUCache(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.dll = new DoublyLinkedList<>();
    }

    @Override
    public V get(K key) {
        Node<K, V> node = map.get(key);
        if (node == null) return null;
        moveToFront(node);
        return node.value;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = map.get(key);
        if (node != null) {
            node.value = value;
            moveToFront(node);
            return;
        }
        if (map.size() == capacity) {
            Node<K, V> mru = dll.removeFirst();
            if (mru != null) map.remove(mru.key);
        }
        Node<K, V> fresh = new Node<>(key, value);
        dll.addFirst(fresh);
        map.put(key, fresh);
    }

    private void moveToFront(Node<K, V> node) {
        dll.remove(node);
        dll.addFirst(node);
    }

    @Override
    public int size() { return map.size(); }

    @Override
    public void clear() {
        map.clear();
    }
}
