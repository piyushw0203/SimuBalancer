import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.sun.management.OperatingSystemMXBean;

public class LoadBalancer {
    public static void main(String[] args) throws IOException, InterruptedException {
        try (Scanner scanner = new Scanner(System.in)) {
            // ...

System.out.print("Enter the number of clients to connect: ");
int numClients = scanner.nextInt();

List<ClientHandler> clients = new ArrayList<>();

try (ServerSocket serverSocket = new ServerSocket(12345)) {
    System.out.println("Load Balancer started. Waiting for clients...");

    for (int i = 0; i < numClients; i++) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected: " + clientSocket);

        ClientHandler clientHandler = new ClientHandler(clientSocket);
        clients.add(clientHandler);

        Thread clientThread = new Thread(clientHandler);
        clientThread.start();
    }

    // Generate tasks and distribute them
    List<Runnable> subtasks = generateSubtasks();
    distributeTasks(clients, subtasks);

    // Delay the start of statistics thread
    Thread.sleep(1000); // Add this delay to allow clients to start

    // Start statistics thread
    Thread statisticsThread = new Thread(() -> {
        while (true) {
            displayStatistics(clients);
            try {
                Thread.sleep(5000); // Display stats every 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    statisticsThread.setDaemon(true);
    statisticsThread.start();
}

// ...

            try {
                try (ServerSocket serverSocket = new ServerSocket(12345)) {
                    System.out.println("Load Balancer started. Waiting for clients...");

                    // Accept client connections and store them
                    for (int i = 0; i < numClients; i++) {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Client connected: " + clientSocket);

                        ClientHandler clientHandler = new ClientHandler(clientSocket);
                        clients.add(clientHandler);

                        Thread clientThread = new Thread(clientHandler);
                        clientThread.start();
                    }

                    // Generate tasks and distribute them
                    List<Runnable> subtasks = generateSubtasks();
                    distributeTasks(clients, subtasks);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void distributeTasks(List<ClientHandler> clients, List<Runnable> subtasks) {
        int numClients = clients.size();
        int numTasks = subtasks.size();
    
        for (int i = 0; i < numTasks; i++) {
            ClientHandler client = clients.get(i % numClients);
            client.assignTask(subtasks.get(i));
        }
    }

    private static List<Runnable> generateSubtasks() {
        List<Runnable> subtasks = new ArrayList<>();
        String inputImagePath = "input.jpg";
        String outputImagePath = "output.jpg";
        subtasks.add(new ImageProcessingTask(inputImagePath, outputImagePath));
        // Add more tasks if desired
        return subtasks;
    }
    
    private static void displayStatistics(List<ClientHandler> clients) {
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    System.out.println("----- Statistics -----");
    for (ClientHandler client : clients) {
        System.out.println("Client ID: " + client.getClientID());
        System.out.println("CPU Utilization: " + (int) (osBean.getSystemCpuLoad() * 100) + "%");
        System.out.println("Memory Usage: " + (osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize()) + " bytes");
        System.out.println("Response Time: " + client.getResponseTime() + " ms");
        System.out.println("Active Connections: " + client.getActiveConnections());
        System.out.println("-----------------------");
    }
}
}
