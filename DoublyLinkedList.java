
package com.mehal.cache;

// Minimal doubly-linked list with head/tail sentinels for O(1) ops
class DoublyLinkedList<K, V> {
    private final Node<K, V> head;
    private final Node<K, V> tail;
    private int size;

    DoublyLinkedList() {
        head = new Node<>(null, null);
        tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    void addFirst(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
        size++;
    }

    void remove(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = node.next = null;
        size--;
    }

    Node<K, V> removeLast() {
        if (size == 0) return null;
        Node<K, V> last = tail.prev;
        remove(last);
        return last;
    }

    Node<K, V> removeFirst() {
        if (size == 0) return null;
        Node<K, V> first = head.next;
        remove(first);
        return first;
    }

    boolean isEmpty() { return size == 0; }
    int size() { return size; }
}
