
package com.mehal.cache;

import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeCache<K, V> implements Cache<K, V> {
    private final Cache<K, V> inner;
    private final ReentrantLock lock = new ReentrantLock();

    public ThreadSafeCache(Cache<K, V> inner) {
        this.inner = inner;
    }

    @Override
    public V get(K key) {
        lock.lock();
        try { return inner.get(key); }
        finally { lock.unlock(); }
    }

    @Override
    public void put(K key, V value) {
        lock.lock();
        try { inner.put(key, value); }
        finally { lock.unlock(); }
    }

    @Override
    public int size() {
        lock.lock();
        try { return inner.size(); }
        finally { lock.unlock(); }
    }

    @Override
    public void clear() {
        lock.lock();
        try { inner.clear(); }
        finally { lock.unlock(); }
    }
}
