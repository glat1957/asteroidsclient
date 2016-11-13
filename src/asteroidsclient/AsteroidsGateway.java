package asteroidsclient;

import asteroids.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class AsteroidsGateway implements asteroids.AsteroidsConstants, Serializable {

    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    private ObjectOutputStream outputObjectToServer;
    private ObjectInputStream inputObjectFromServer;

    AsteroidsGateway() {
        try {
            // Create a server socket
            Socket serverSocket = new Socket("localhost", 8080);

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

    public ShipModel getShipModel(String shipName) {
        ShipModel temp = null;
        outputToServer.println(GET_SHIP_MODEL);
        outputToServer.println(shipName);
        outputToServer.flush();
        try {
            temp = (ShipModel) inputObjectFromServer.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
    }

    public int getPlayerNum() {
        int playerNum = 0;
        outputToServer.println(GET_PLAYER_NUM);
        outputToServer.flush();
        try {
            playerNum = Integer.parseInt(inputFromServer.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return playerNum;
    }

    public void sendPlayer1Rot(Double player1Rot) {
        outputToServer.println(SEND_PLAYER1_ROT);
        outputToServer.println(player1Rot);
        outputToServer.flush();
    }

    public double getPlayer1Rot() {
        outputToServer.println(GET_PLAYER1_ROT);
        outputToServer.flush();

        try {
            return Double.parseDouble(inputFromServer.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }

    public void sendPlayer2Rot(Double player2Rot) {
        outputToServer.println(SEND_PLAYER2_ROT);
        outputToServer.println(player2Rot);
        outputToServer.flush();
    }

    public double getPlayer2Rot() {
        outputToServer.println(GET_PLAYER2_ROT);
        outputToServer.flush();

        try {
            return Double.parseDouble(inputFromServer.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }
    
    public int getNumConnected(){
        outputToServer.println(NUM_CONNECTED);
        outputToServer.flush();
        
        try{
            return Integer.parseInt(inputFromServer.readLine());
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }
}
