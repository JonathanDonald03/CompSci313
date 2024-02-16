import java.io.*;
import java.net.*;

public class ChatClient { 
  public static void main(String[] args) throws IOException { 
    int portNumber = 4444;
    String hostName = "fedora";
    try ( 
        Socket chatSocket = new Socket(hostName, portNumber);
        PrintWriter socketOut = new PrintWriter(chatSocket.getOutputStream(),true);
        BufferedReader socketIn = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
          System.out.println("Client is connected to server");
          String userMessage;
          while ((userMessage = stdIn.readLine()) != null) { 
            socketOut.println(userMessage);
          }
          
        } catch (IOException e) { 
          System.err.println("Couldn't get connection to " + hostName);  
        }
  }
}
