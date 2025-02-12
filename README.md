# Distributed Cache System

## Overview
This project is a **Distributed Cache System** implemented in Java, designed to efficiently store and retrieve data using multi-threading techniques. The primary goal of this system is to explore and understand how **threading works** in a distributed environment.
## Technologies Used
- **Java** (Concurrency APIs, Executors, Synchronization)
- **Networking (gRPC)**
- **Data Structures** (HashMap, ConcurrentHashMap)
## Implementation
Currently implementing properly the CacheNode Class
Each CacheNode will:
 - Maintain its own cache (ConcurrentHashMap).
 - Act as a gRPC server to handle requests from other nodes.
 - Act as a gRPC client to communicate with other nodes.
