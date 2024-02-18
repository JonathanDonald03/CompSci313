
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class OpenSocket implements Runnable {
    private Socket clientSocket;
    private String username;

    public OpenSocket(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
    }

    public void printMessage(String username, String message) throws IOException {
        PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
        String printMessage = "[" + username + "]" + " " + message; 
        socketOut.println(printMessage);
    }

    @Override
    public void run() {
        try {
            PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String clientInput;
            
            while ((clientInput = socketIn.readLine()) != null) { 
              boolean usernameAdded = ChatServer.checkName(clientInput);
              if (usernameAdded) {
                this.username = clientInput;
                break;
              } else { 
                socketOut.println("Username already in use please choose another one");
              }
            }
            while ((clientInput = socketIn.readLine()) != null) {
                // Assuming ChatServer.postMessage now takes an OpenSocket object
                
                System.out.println("[" + username + "]" + " " +  clientInput);
                ChatServer.postMessage(username, clientInput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class ChatServer {
    private static ServerSocket serverSocket;

    private static List<OpenSocket> threads = new ArrayList<>();
    private static List<String> usernames = new ArrayList<>();
    
    public static boolean checkName(String username) { 
      boolean available = !usernames.contains(username);

      if (available) { 
        usernames.add(username);
      }

      return available;
    }

    public static void postMessage(String username, String message) {
        try {
            for (OpenSocket clients : threads) { 
              clients.printMessage(username, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void acceptClients() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                OpenSocket client = new OpenSocket(clientSocket);
                Thread clientThread = new Thread(client);
                threads.add(client);
                clientThread.start(); // Start the thread
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int portNumber = 4444;

        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server is running and waiting for a client to connect.");

            acceptClients();

                    } catch (IOException e) {
            System.err.println("Exception caught when trying to listen on port " + portNumber);
            System.err.println(e.getMessage());
        }
    }
}
