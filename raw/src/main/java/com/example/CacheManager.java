import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheManager {
    private final List<CacheNode> nodes;
    private final ReentrantReadWriteLock lock; // For thread safety

    public CacheManager() {
        this.nodes = new ArrayList<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public void addNode(CacheNode node) {
        lock.writeLock().lock();
        try {
            nodes.add(node);
            System.out.println("Added node: " + node.getNodeId());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeNode(String nodeId) {
        lock.writeLock().lock();
        try {
            nodes.removeIf(node -> node.getNodeId().equals(nodeId));
            System.out.println("Removed node: " + nodeId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String get(String key) {
        lock.readLock().lock();
        try {
            if (nodes.isEmpty()) {
                throw new IllegalStateException("No nodes available in the cache system.");
            }
            CacheNode node = getNodeForKey(key);
            String value = node.get(key);
            if (value == null) {
                throw new IllegalArgumentException("Key not found: " + key);
            }
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void put(String key, String value) {
        lock.readLock().lock();
        try {
            if (nodes.isEmpty()) {
                throw new IllegalStateException("No nodes available in the cache system.");
            }
            CacheNode node = getNodeForKey(key);
            node.put(key, value);
            System.out.println("Put key=" + key + " in node=" + node.getNodeId());
        } finally {
            lock.readLock().unlock();
        }
    }

    public void remove(String key) {
        lock.readLock().lock();
        try {
            if (nodes.isEmpty()) {
                throw new IllegalStateException("No nodes available in the cache system.");
            }
            CacheNode node = getNodeForKey(key);
            node.remove(key);
            System.out.println("Removed key=" + key + " from node=" + node.getNodeId());
        } finally {
            lock.readLock().unlock();
        }
    }

    private CacheNode getNodeForKey(String key) {
        int index = Math.abs(key.hashCode() % nodes.size());
        return nodes.get(index);
    }

    public void printNodes() {
        lock.readLock().lock();
        try {
            System.out.println("Current nodes in the cache system:");
            for (CacheNode node : nodes) {
                System.out.println(node);
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}