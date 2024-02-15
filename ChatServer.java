import java.net.*;
import java.io.*;

public class ChatServer {
    public static void main(String[] args) {
        int portNumber = 4444;

        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
        ) {
            // Server logic goes here
            System.out.println("Server is running and waiting for a client to connect.");

        } catch (IOException e) {
            System.err.println("Exception caught when trying to listen on port " + portNumber);
            System.err.println(e.getMessage());
        }
    }
}
