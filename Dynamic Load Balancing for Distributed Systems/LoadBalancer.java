import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.sun.management.OperatingSystemMXBean;

public class LoadBalancer {

    public static void main(String[] args) throws IOException, InterruptedException {
        //String host = (args.length > 0) ? args[0] : "localhost";  // default to "localhost" if no argument
        int port = (args.length > 1) ? Integer.parseInt(args[1]) : 12345;  // default to 12345 if no argument
        
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the number of clients to connect: ");
            int numClients = scanner.nextInt();

            List<ClientHandler> clients = new ArrayList<>();

            try (ServerSocket serverSocket = new ServerSocket(port)) {
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

                Thread.sleep(1000); // Delay to allow clients to start

                // Start statistics thread
                Thread statisticsThread = new Thread(() -> {
                    while (true) {
                        displayStatistics(clients, numClients);
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
        }
    }

    private static List<Runnable> generateSubtasks() {
        List<Runnable> subtasks = new ArrayList<>();
        
        String[] inputImagePaths = {
        "E:\\\\College\\\\TY\\\\CA\\\\Dynamic Load Balancing for Distributed Systems\\\\input0.jpg"
        
                };  
        
        String[] outputImagePaths = {"output.jpg"};

        for (int i = 0; i < inputImagePaths.length; i++) {
            subtasks.add(new ImageGrayscaleTask(inputImagePaths[i], "GRAYSCALE"+ outputImagePaths[i])); 
            //subtasks.add(new ImageProcessingTask(inputImagePaths[i], "BLUR" + outputImagePaths[i]));
            subtasks.add(new ImageResizingTask(inputImagePaths[i], "RESIZE"+ outputImagePaths[i], 200, 200));
            subtasks.add(new ImageRotationTask(inputImagePaths[i],"ROTATE" + outputImagePaths[i],45));
        }
                return subtasks;
            }

    private static void distributeTasks(List<ClientHandler> clients, List<Runnable> subtasks) {
        int numClients = clients.size();
        int numTasks = subtasks.size();

        for (int i = 0; i < numTasks; i++) {
            ClientHandler client = clients.get(i % numClients);
            client.assignTask(subtasks.get(i));
        }
    }

    private static void displayStatistics(List<ClientHandler> clients, int numClients) {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        System.out.println("----- Statistics -----");
        for (ClientHandler client : clients) {
            System.out.println("Client ID: " + client.getClientID());
            System.out.println("CPU Utilization: " + ((int) (osBean.getSystemCpuLoad() * 100)/numClients) + "%");
            System.out.println("Memory Usage: " + ((osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize())/numClients) + " bytes");
            System.out.println("Response Time: " + client.getResponseTime() + " ms");
            System.out.println("Active Connections: " + ClientHandler.getActiveConnections());
            System.out.println("-----------------------");
        }
    }
}
