import java.net.*;
import java.io.*;

class openSocket implements Runnable { 
  @Override 
  public void run() { 
    
  }
}

public static void acceptClients() { 
  while (true) { 
    try { 
          Socket clientSocket = serverSocket.accept();
          PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
          BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) { 

        }
  }
}

public class ChatServer {
    public static void main(String[] args) {
        int portNumber = 4444;

        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
            acceptClients();
            
        ) {
            System.out.println("Server is running and waiting for a client to connect.");
            String clientInput; 
            while ((clientInput = socketIn.readLine()) != null) { 
              System.out.println(clientInput);
            }
        } catch (IOException e) {
            System.err.println("Exception caught when trying to listen on port " + portNumber);
            System.err.println(e.getMessage());
        }
    }
}
