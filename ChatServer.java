import java.net.*;
import java.io.*;

class openSocket implements Runnable { 
  private Socket clientSocket; 

  public openSocket(Socket clientSocket) { 
    this.clientSocket = clientSocket;
  }

  @Override
  public void run() { 
    PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
    BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  }
}

public class ChatServer {

  public static void acceptClients() { 
  List<Thread> threads = new ArrayList<>();

  while (true) { 
    try { 
        Socket clientSocket = serverSocket.accept();
        openSocket client = new openSocket(clientSocket); 
        Thread clientThread = new Thread(client); 
        threads.add(clientThread);
        } catch (IOException e) { 

        }
    }
  }

  public static void main(String[] args) {
        int portNumber = 4444;

        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
                     
        ) {
            acceptClients();
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
