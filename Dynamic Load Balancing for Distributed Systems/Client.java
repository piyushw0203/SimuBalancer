import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the number of clients to connect: ");
            int numClients = scanner.nextInt();

            for (int i = 0; i < numClients; i++) {
                connectToServer();
            }
        }
    }

    private static void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 12345);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Hello from client " + Thread.currentThread().getId());

            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
