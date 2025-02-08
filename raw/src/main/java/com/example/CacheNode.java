import java.util.concurrent.ConcurrentHashMap;

public class CacheNode {
    private ConcurrentHashMap<String, String> cache;
    private String nodeId;

    public CacheNode(String nodeId) {
        this.nodeId = nodeId;
        this.cache = new ConcurrentHashMap<>();
    }

    public String get(String key) {
        return cache.get(key);
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public String getNodeId() {
        return nodeId;
    }
}