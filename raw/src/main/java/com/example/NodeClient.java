package com.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.example.grpc.ServiceProto.*;
import com.example.grpc.NodeServiceGrpc;

public class NodeClient {
    private final NodeServiceGrpc.NodeServiceBlockingStub blockingStub;

    public NodeClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = NodeServiceGrpc.newBlockingStub(channel);
    }

    public void sendMessage(String message) {
        MessageRequest request = MessageRequest.newBuilder().setMessage(message).build();
        MessageResponse response = blockingStub.sendMessage(request);
        System.out.println("Response: " + response.getResponse());
    }

    // public static void main(String[] args) {
    //     // When you build your channel and stub, use the correct class name:
    //     NodeServiceGrpc.NodeServiceBlockingStub stub = 
    //         NodeServiceGrpc.newBlockingStub(channel);
    //     // ...
    // }
} 