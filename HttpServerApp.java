
package com.mehal.cache;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpServerApp {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        Cache<String, String> cache = new ThreadSafeCache<>(new LRUCache<>(1024));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/get", new GetHandler(cache));
        server.createContext("/put", new PutHandler(cache));
        server.setExecutor(null);
        System.out.println("Cache HTTP server started on http://localhost:" + port);
        server.start();
    }

    static class GetHandler implements HttpHandler {
        private final Cache<String, String> cache;
        GetHandler(Cache<String, String> c) { this.cache = c; }
        @Override public void handle(HttpExchange ex) throws IOException {
            Map<String, String> q = parse(ex.getRequestURI());
            String key = q.get("key");
            String body;
            if (key == null) {
                ex.sendResponseHeaders(400, 0);
                body = "Missing key";
            } else {
                String val = cache.get(key);
                if (val == null) { ex.sendResponseHeaders(404, 0); body = "null"; }
                else { ex.sendResponseHeaders(200, 0); body = val; }
            }
            try (OutputStream os = ex.getResponseBody()) { os.write(body.getBytes(StandardCharsets.UTF_8)); }
        }
    }

    static class PutHandler implements HttpHandler {
        private final Cache<String, String> cache;
        PutHandler(Cache<String, String> c) { this.cache = c; }
        @Override public void handle(HttpExchange ex) throws IOException {
            Map<String, String> q = parse(ex.getRequestURI());
            String key = q.get("key");
            String value = q.get("value");
            if (key == null || value == null) {
                ex.sendResponseHeaders(400, 0);
                try (OutputStream os = ex.getResponseBody()) { os.write("Missing key/value".getBytes(StandardCharsets.UTF_8)); }
                return;
            }
            cache.put(key, value);
            ex.sendResponseHeaders(204, -1);
        }
    }

    static Map<String, String> parse(URI uri) {
        Map<String, String> map = new HashMap<>();
        String q = uri.getQuery();
        if (q == null) return map;
        for (String p : q.split("&")) {
            String[] kv = p.split("=", 2);
            if (kv.length == 2) map.put(kv[0], kv[1]);
        }
        return map;
    }
}
