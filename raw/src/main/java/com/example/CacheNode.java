import java.util.concurrent.ConcurrentHashMap;

public class CacheNode {
    private ConcurrentHashMap<String, String> cache;
    private String nodeId;
    private final int port;
    private Server server;
    private ManagedChannel channel; // gRPC client channel
    private CacheServiceGrpc.CacheServiceBlockingStub blockingStub; 
    public CacheNode(String nodeId, int port) {
        this.nodeId = nodeId;
        this.port = port;
        this.cache = new ConcurrentHashMap<>();
    }

    public void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new CacheServiceImpl())
                .build()
                .start();
        System.out.println("Node " + nodeId + " started on port " + port);
    }


        public void connectToNode(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = CacheServiceGrpc.newBlockingStub(channel);
    }

    public CacheServiceGrpc.CacheServiceBlockingStub getBlockingStub() {
        return blockingStub;
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
    
    private class CacheServiceImpl extends CacheServiceGrpc.CacheServiceImplBase {
        @Override
        public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
            String key = request.getKey();
            String value = cache.get(key);
            GetResponse response = GetResponse.newBuilder().setValue(value).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void put(PutRequest request, StreamObserver<PutResponse> responseObserver) {
            String key = request.getKey();
            String value = request.getValue();
            cache.put(key, value);
            PutResponse response = PutResponse.newBuilder().setSuccess(true).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void remove(RemoveRequest request, StreamObserver<RemoveResponse> responseObserver) {
            String key = request.getKey();
            cache.remove(key);
            RemoveResponse response = RemoveResponse.newBuilder().setSuccess(true).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}