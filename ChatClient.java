
import java.io.*;
import java.net.*;

class Listen implements Runnable { 
  private Socket clientSocket; 
  
  public Listen(Socket clientSocket) { 
    this.clientSocket = clientSocket;
  }

  @Override 
  public void run() { 
    try { 
      BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
      String serverOutput;

      while ((serverOutput = socketIn.readLine()) != null) { 
        System.out.println(serverOutput);
      }
    } catch (IOException e) { 
      e.printStackTrace();
    }
  }
}

public class ChatClient { 
  public static void main(String[] args) throws IOException { 
    int portNumber = 4444;
    String hostName = "fedora";
    
    Socket chatSocket = new Socket(hostName, portNumber);
    
    // Create and start the Listen thread
    Listen listenServer = new Listen(chatSocket);
    Thread listenThread = new Thread(listenServer);
    listenThread.start();

    try ( 
        PrintWriter socketOut = new PrintWriter(chatSocket.getOutputStream(), true);
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    ) {
      System.out.println("Client is connected to the server");
      System.out.println("Please Choose a username");
      String userMessage;
      while ((userMessage = stdIn.readLine()) != null) { 
        socketOut.println(userMessage);
      }
    } catch (IOException e) { 
      System.err.println("Couldn't get connection to " + hostName);  
    } finally {
      // Close the socket after use
      chatSocket.close();
    }
  }
}
