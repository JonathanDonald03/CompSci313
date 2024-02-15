import java.io.*;
import java.net.*;

public class ChatClient { 
  public static void main(String[] args) throws IOException { 
    int portNumber = 4444;
    String hostName = "fedora";
    try ( 
        Socket chatSocket = new Socket(hostName, portNumber);
        PrintWriter socketOut = new PrintWriter(chatSocket,portNumber);
        BufferedReader socketIn = new BufferedReader(new InputSt)
        BufferedReader stdIn = new BufferedReader(System.in)
        ) { 
          System.out.println("Client is connected to server");
        } catch (IOException e) { 
          System.err.println("Couldn't get connection to " + hostName);  
        }
  }
}
