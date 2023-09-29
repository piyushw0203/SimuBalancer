import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.MemoryMXBean;

public class ClientHandler implements Runnable {
    private static int clientCount = 0;
    private int clientID;
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.clientID = ++clientCount;
    }

    public void assignTask(Runnable task) {
        System.out.println("Task");
        Thread taskThread = new Thread(task);
        taskThread.start();
    }

    @Override
    public void run() {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = in.readLine();
            if (message.equals("Ready")) {
                System.out.println("Client " + clientSocket + " is ready.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public long getResponseTime() {
        // Simulated response time in milliseconds
        return (long) (Math.random() * 100);
    }

    public int getActiveConnections() {
        // Simulated active connections
        return (int) (Math.random() * 10);
    }

    public int getClientID() {
        return clientID;
    }
}
