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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AsteroidsGateway implements asteroids.AsteroidsConstants, Serializable {

    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    private ObjectOutputStream outputObjectToServer;
    private ObjectInputStream inputObjectFromServer;

    private final Lock lock = new ReentrantLock();

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

    public synchronized ShipModel getShipModel(String shipName) {
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

    public synchronized int getPlayerNum() {
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

    public synchronized void sendPlayer1Rot(Double player1Rot) {
        outputToServer.println(SET_PLAYER1_ROT);
        outputToServer.println(player1Rot);
        outputToServer.flush();
    }

    public synchronized double getPlayer1Rot() {
        outputToServer.println(GET_PLAYER1_ROT);
        outputToServer.flush();

        try {
            return Double.parseDouble(inputFromServer.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }

    public synchronized void sendPlayer2Rot(Double player2Rot) {
        outputToServer.println(SET_PLAYER2_ROT);
        outputToServer.println(player2Rot);
        outputToServer.flush();
    }

    public synchronized double getPlayer2Rot() {
        outputToServer.println(GET_PLAYER2_ROT);
        outputToServer.flush();

        try {
            return Double.parseDouble(inputFromServer.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }

    public synchronized int getNumConnected() {
        outputToServer.println(NUM_CONNECTED);
        outputToServer.flush();

        try {
            return Integer.parseInt(inputFromServer.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public synchronized void disconnectPlayer() {
        outputToServer.println(DISCONNECT_PLAYER);
        outputToServer.flush();
    }

    public synchronized void incrementScore() {
        outputToServer.println(INC_SCORE);
        outputToServer.flush();
    }

    public synchronized int getScore() {
        outputToServer.println(GET_SCORE);
        outputToServer.flush();
        try {
            return Integer.parseInt(inputFromServer.readLine());
        } catch (Exception ex) {
        }
        return -1;
    }

    public synchronized void setPlayer1Lives(int lives) {
        try {
            outputToServer.println(SET_PLAYER1_LIVES);
            outputToServer.println(lives);
            outputToServer.flush();
        } catch (Exception ex) {
        }
    }

    public synchronized void setPlayer2Lives(int lives) {
        outputToServer.println(SET_PLAYER2_LIVES);
        outputToServer.println(lives);
        outputToServer.flush();

    }

    public synchronized int getPlayer1Lives() {
        outputToServer.println(GET_PLAYER1_LIVES);
        outputToServer.flush();
        try {
            return Integer.parseInt(inputFromServer.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public synchronized int getPlayer2Lives() {
        outputToServer.println(GET_PLAYER2_LIVES);
        outputToServer.flush();
        try {
            return Integer.parseInt(inputFromServer.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public synchronized void playerNewBullet(double x, double y, double rotation) {
        outputToServer.println(PLAYER_NEW_BULLET);
        outputToServer.println(x);
        outputToServer.println(y);
        outputToServer.println(rotation);
        outputToServer.flush();
    }

    public synchronized List<Bullet> playerGetBullets() {
        List<Bullet> temp = Collections.synchronizedList(new ArrayList<>());;
        outputToServer.println(PLAYER_GET_BULLETS);
        outputToServer.flush();

        try {
            //temp = (List<Bullet>) inputObjectFromServer.readObject();
            int length = Integer.parseInt(inputFromServer.readLine());
            for(int i = 0; i < length; i++){
                temp.add((Bullet) inputObjectFromServer.readObject());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Got list of length: " + temp.size());
        return temp;
    }
}
