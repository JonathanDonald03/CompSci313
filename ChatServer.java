
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class OpenSocket implements Runnable {
    private Socket clientSocket;
    public String username;

    public OpenSocket(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
    }

    public void printMessage(String sender,String message) throws IOException {
        PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
        String printMessage = "[" + sender + "] " + message; 
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
                String joinMessage = "[" + username + " has connected to the server]";
                ChatServer.postMessage(this , joinMessage);
                break;
              } else { 
                socketOut.println("Username already in use. Please choose another one.");
              }
            }

            while ((clientInput = socketIn.readLine()) != null) {
                // Assuming ChatServer.postMessage now takes an OpenSocket object
                if (clientInput.startsWith("/")) {
                  String targetUsername = getFirstWord(clientInput.substring(1));
                  if (!ChatServer.checkNameWhisper(targetUsername)) { 
                    printMessage("Server", "Username does not exist");
                  } else { 
                    ChatServer.whisper(this.username, targetUsername, clientInput);
                  }
                } else {
                    System.out.println("[" + this.username + "] " +  clientInput);
                    ChatServer.postMessage(this, clientInput);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFirstWord(String input) {
        if (input != null && !input.trim().isEmpty()) {
            String[] words = input.trim().split("\\s+");

            if (words.length > 0) {
                return words[0];
            }
        }
        return null;
    }
}

public class ChatServer {
    private static ServerSocket serverSocket;
    private static List<OpenSocket> threads = new ArrayList<>();
    private static List<String> usernames = new ArrayList<>();
    
    public static String removeFirstWord(String message) { 
      String[] words = message.trim().split("\\s+", 2);
      return words[1];
    }

    public static void whisper(String senderUsername, String targetUsername, String message) throws IOException {
      String cleanedMessage = removeFirstWord(message);
      OpenSocket messageTarget = null;
      for (OpenSocket client : threads) { 
        if (client.username.equals(targetUsername)) { 
          messageTarget = client;
          break;
        }
      }

      if (messageTarget != null) {
        messageTarget.printMessage("[Whisper from " + senderUsername + "] " , cleanedMessage);
      } else {
        // Handle case where the target username does not exist
        // For now, print to the sender's console
        System.out.println("[" + senderUsername + "] Whisper failed: Username does not exist.");
      }
    }

    public static boolean checkNameWhisper(String username) { 
      return usernames.contains(username);
    }

    public static boolean checkName(String username) { 
      boolean available = !usernames.contains(username);

      if (available) { 
        usernames.add(username);
      }

      return available;
    }

    public static void postMessage(OpenSocket sender, String message) {
        try {
            for (OpenSocket client : threads) { 
              // Don't send the message back to the sender
              
              client.printMessage(sender.username, message);
              
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
