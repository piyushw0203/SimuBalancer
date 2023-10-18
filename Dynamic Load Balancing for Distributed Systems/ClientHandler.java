import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.MemoryMXBean;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {
    private static final AtomicInteger clientCount = new AtomicInteger(0);
    private static final AtomicInteger activeConnections = new AtomicInteger(0);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);  // Create an ExecutorService
    private final int clientID;
    private final Socket clientSocket;
    private long responseTime;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.clientID = clientCount.incrementAndGet();
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();  // Capture start time
        activeConnections.incrementAndGet();  // Increment active connections
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String message = in.readLine();
            if ("Ready".equals(message)) {
                System.out.println("Client " + clientSocket + " is ready.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing client request", e);
        } finally {
            activeConnections.decrementAndGet();  // Decrement active connections
            long endTime = System.currentTimeMillis();  // Capture end time
            responseTime = endTime - startTime;  // Calculate response time
        }
    }

    public long getResponseTime() {
        return responseTime;
    }

    public static int getActiveConnections() {
        return activeConnections.get();
    }

    // Simulated methods for statistics
    public double getCpuUtilization() {
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    return osBean.getSystemCpuLoad() * 100;
}

public long getMemoryUsage() {
    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    return memoryBean.getHeapMemoryUsage().getUsed() + memoryBean.getNonHeapMemoryUsage().getUsed();
}

    

    public int getClientID() {
        return clientID;
    }

     public void assignTask(Runnable task) {
        System.out.println("Task assigned");
        executorService.execute(task);
    }
}
