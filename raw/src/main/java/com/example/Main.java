package com.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Start the gRPC server
        NodeServer server = new NodeServer(50051);
        server.start();

        // Create a gRPC client and send a message
        NodeClient client = new NodeClient("localhost", 50051);
        client.sendMessage("Hello, gRPC!");

        // Keep the server running
        Thread.currentThread().join();
    }
} 