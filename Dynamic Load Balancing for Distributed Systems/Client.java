import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : "localhost";  // default to "localhost" if no argument
        int port = (args.length > 1) ? Integer.parseInt(args[1]) : 12345;  // default to 12345 if no argument

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the number of clients to connect: ");
            int numClients = scanner.nextInt();

            for (int i = 0; i < numClients; i++) {
                connectToServer(host, port);
            }
        }
    }

    private static void connectToServer(String host, int port) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println("Hello from client " + Thread.currentThread().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

