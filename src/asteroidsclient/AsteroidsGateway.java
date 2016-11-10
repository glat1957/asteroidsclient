package asteroidsclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class AsteroidsGateway implements asteroids.AsteroidsConstants, Serializable{

    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    private ObjectOutputStream outputObjectToServer;
    private ObjectInputStream inputObjectFromServer;

    AsteroidsGateway() {
        try {
            // Create a server socket
            Socket serverSocket = new Socket("localhost", 80);

            // Create an output stream to send data to the server
            outputToServer = new PrintWriter(serverSocket.getOutputStream());
            // Create an object output stream to send objects to server.
            outputObjectToServer = new ObjectOutputStream(serverSocket.getOutputStream());
            
            // Create an input stream to read data from the server
            inputFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            // Create an object input stream to read objects from server.
            inputObjectFromServer = new ObjectInputStream(serverSocket.getInputStream());
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getShip(){
        outputToServer.println(SET_SHIP);
        
    }
}
