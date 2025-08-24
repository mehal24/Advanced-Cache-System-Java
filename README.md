
# Advanced Cache System (Java)

A production-style, **generic caching library** in Java with multiple eviction policies, thread-safe wrapper, a minimal HTTP API, and a micro-benchmark.

## ✨ Features
- Generic API: `Cache<K,V>` interface.
- Policies implemented:
  - **LRU** (Least Recently Used) — O(1) get/put
  - **LFU** (Least Frequently Used) — O(1) amortized get/put
  - **MRU** (Most Recently Used) — O(1) get/put
- **Thread-safe** wrapper using `ReentrantLock`.
- **Benchmark** comparing:
  - This library's `LRUCache`
  - Java's `LinkedHashMap`-based LRU
  - `LFUCache` and `MRUCache`
- **Minimal HTTP server** (no external deps) to interact with a cache:
  - `GET /get?key=K`
  - `POST /put?key=K&value=V`

## 📦 Build & Run (JDK 11+)
```bash
# Compile
javac -d out $(find src/main/java -name "*.java")

# Run demo
java -cp out com.mehal.cache.Demo

# Run benchmark (adjust sizes inside Benchmark.java if needed)
java -cp out com.mehal.cache.Benchmark

# Run HTTP server (localhost:8080)
java -cp out com.mehal.cache.HttpServerApp
```

## 📚 Usage (Code)
```java
Cache<Integer, String> cache = new LRUCache<>(3);
cache.put(1, "A");
cache.put(2, "B");
cache.put(3, "C");
cache.get(1);            // promotes 1
cache.put(4, "D");       // evicts 2 (LRU)
System.out.println(cache.get(2)); // null
```

## 🔬 Complexity
- LRU: O(1) get/put using HashMap + DoublyLinkedList
- LFU: O(1) amortized get/put using (key->node) and (freq->DLL) buckets
- MRU: O(1) get/put, evicts most recently used (head)

## 🧪 Benchmark Methodology
Random sequence of `get` & `put` over 100k operations on cache capacity ~10k. Compares average op times using `System.nanoTime()`. Numbers vary by machine; use to compare policies relatively.

## 🔒 Thread-Safety
Use `ThreadSafeCache` to wrap any cache implementation:
```java
Cache<Integer, String> safe = new ThreadSafeCache<>(new LRUCache<>(1024));
```

## 🌐 HTTP API
A minimal server using `com.sun.net.httpserver.HttpServer`:
- `GET /get?key=K` -> returns value or 404
- `POST /put?key=K&value=V` -> 204 on success
Change the underlying policy in `HttpServerApp` easily.

## 📄 License
MIT
