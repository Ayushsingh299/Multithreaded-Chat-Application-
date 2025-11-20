package com.chat.server;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 9999;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private Map<String, PrintWriter> clients;

    public ChatServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        threadPool = Executors.newFixedThreadPool(50);
        clients = new ConcurrentHashMap<>();
        System.out.println("✓ Chat Server started on port " + PORT);
    }

    public void start() {
        System.out.println("✓ Waiting for connections...");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("→ New client: " + socket.getInetAddress());
                threadPool.execute(new ClientHandler(socket, this));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addClient(String username, PrintWriter out) {
        clients.put(username, out);
        broadcast(username + " joined the chat", null);
        System.out.println("✓ " + username + " joined. Total: " + clients.size());
    }

    public synchronized void removeClient(String username) {
        clients.remove(username);
        broadcast(username + " left the chat", null);
        System.out.println("✗ " + username + " left. Total: " + clients.size());
    }

    public synchronized void broadcast(String message, String sender) {
        for (Map.Entry<String, PrintWriter> entry : clients.entrySet()) {
            if (sender == null || !entry.getKey().equals(sender)) {
                entry.getValue().println(message);
            }
        }
    }

    public synchronized void sendPrivate(String message, String recipient) {
        PrintWriter out = clients.get(recipient);
        if (out != null) {
            out.println(message);
        }
    }

    public static void main(String[] args) {
        try {
            new ChatServer().start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private ChatServer server;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter your username:");
            username = in.readLine();
            
            if (username != null && !username.trim().isEmpty()) {
                server.addClient(username, out);
                out.println("Welcome " + username + "! Type /help for commands.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("/quit")) {
                        break;
                    } else if (message.startsWith("/private ")) {
                        String[] parts = message.split(" ", 3);
                        if (parts.length == 3) {
                            server.sendPrivate("[Private from " + username + "]: " + parts[2], parts[1]);
                        }
                    } else if (message.equals("/help")) {
                        out.println("Commands: /private <user> <msg>, /quit");
                    } else {
                        server.broadcast(username + ": " + message, username);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        try {
            if (username != null) server.removeClient(username);
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
