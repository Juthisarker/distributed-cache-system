package com.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import com.example.grpc.ServiceProto.*;
import com.example.grpc.NodeServiceGrpc;
import com.example.grpc.NodeServiceGrpc.NodeServiceImplBase;

import java.io.IOException;

public class NodeServer {
    private final int port;
    private final Server server;

    public NodeServer(int port) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(new NodeServiceImpl())
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            NodeServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private static class NodeServiceImpl extends NodeServiceImplBase {
        @Override
        public void sendMessage(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
            String message = request.getMessage();
            System.out.println("Received message: " + message);

            MessageResponse response = MessageResponse.newBuilder()
                    .setResponse("Message received: " + message)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        // Remove the following method since SomeRequest/SomeResponse are not defined:
        // @Override
        // public void someMethod(SomeRequest request, StreamObserver<SomeResponse> responseObserver) {
        //     // Your implementation goes here
        // }
    }

    public static void main(String[] args) throws Exception {
        io.grpc.Server server = io.grpc.ServerBuilder.forPort(50051)
                .addService(new NodeServiceImpl())
                .build()
                .start();
        // ...
    }
}