
package com.mehal.cache;

// Doubly linked list node used by several caches
class Node<K, V> {
    K key;
    V value;
    int freq = 1;
    Node<K, V> prev, next;
    Node(K k, V v) { key = k; value = v; }
}
