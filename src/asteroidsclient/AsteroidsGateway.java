package asteroidsclient;

import asteroids.*;
import java.io.IOException;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AsteroidsGateway implements asteroids.AsteroidsConstants, Serializable {

    private ObjectOutputStream outputObjectToServer;
    private ObjectInputStream inputObjectFromServer;


    AsteroidsGateway() {
        try {
            // Create a server socket
            Socket serverSocket = new Socket("localhost", 8000);

            // Create an object output stream to send objects to server.
            outputObjectToServer = new ObjectOutputStream(serverSocket.getOutputStream());

            // Create an object input stream to read objects from server.
            inputObjectFromServer = new ObjectInputStream(serverSocket.getInputStream());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized ShipModel getShipModel(String shipName) {
        ShipModel temp = null;
        try {
            outputObjectToServer.writeInt(GET_SHIP_MODEL);
            outputObjectToServer.writeUTF(shipName);
            outputObjectToServer.flush();
            temp = (ShipModel) inputObjectFromServer.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
    }

    public synchronized int getPlayerNum() {
        int playerNum = 0;

        try {
            outputObjectToServer.writeInt(GET_PLAYER_NUM);
            outputObjectToServer.flush();
            playerNum = inputObjectFromServer.readInt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return playerNum;
    }

    public synchronized void sendPlayer1Rot(Double player1Rot) {
        try {
            outputObjectToServer.writeInt(SET_PLAYER1_ROT);
            outputObjectToServer.writeDouble(player1Rot);
            outputObjectToServer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public synchronized double getPlayer1Rot() {

        try {
            outputObjectToServer.writeInt(GET_PLAYER1_ROT);
            outputObjectToServer.flush();
            return inputObjectFromServer.readDouble();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }

    public synchronized void sendPlayer2Rot(Double player2Rot) {
        try {
            outputObjectToServer.writeInt(SET_PLAYER2_ROT);
            outputObjectToServer.writeDouble(player2Rot);
            outputObjectToServer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized double getPlayer2Rot() {

        try {
            outputObjectToServer.writeInt(GET_PLAYER2_ROT);
            outputObjectToServer.flush();
            return inputObjectFromServer.readDouble();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }

    public synchronized int getNumConnected() {

        try {
            outputObjectToServer.writeInt(NUM_CONNECTED);
            outputObjectToServer.flush();

            return inputObjectFromServer.readInt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public synchronized void disconnectPlayer() {
        try {
            outputObjectToServer.writeInt(DISCONNECT_PLAYER);
            outputObjectToServer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void incrementScore() {
        try {
            outputObjectToServer.writeInt(INC_SCORE);
            outputObjectToServer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized int getScore() {

        try {
            outputObjectToServer.writeInt(GET_SCORE);
            outputObjectToServer.flush();
            return inputObjectFromServer.readInt();
        } catch (Exception ex) {
        }
        return -1;
    }

    public synchronized void setPlayer1Lives(int lives) {
        try {
            outputObjectToServer.writeInt(SET_PLAYER1_LIVES);
            outputObjectToServer.writeInt(lives);
            outputObjectToServer.flush();
        } catch (Exception ex) {
        }
    }

    public synchronized void setPlayer2Lives(int lives) {
        try {
            outputObjectToServer.writeInt(SET_PLAYER2_LIVES);
            outputObjectToServer.writeInt(lives);
            outputObjectToServer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public synchronized int getPlayer1Lives() {

        try {
            outputObjectToServer.writeInt(GET_PLAYER1_LIVES);
            outputObjectToServer.flush();
            return inputObjectFromServer.readInt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public synchronized int getPlayer2Lives() {

        try {
            outputObjectToServer.writeInt(GET_PLAYER2_LIVES);
            outputObjectToServer.flush();
            return inputObjectFromServer.readInt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public synchronized void playerNewBullet(double x, double y, double rotation) {
        try {
            outputObjectToServer.writeInt(PLAYER_NEW_BULLET);
            outputObjectToServer.writeDouble(x);
            outputObjectToServer.writeDouble(y);
            outputObjectToServer.writeDouble(rotation);
            outputObjectToServer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized List<Bullet> playerGetBullets() {
        List<Bullet> temp = Collections.synchronizedList(new ArrayList<>());;

        try {
            outputObjectToServer.writeInt(PLAYER_GET_BULLETS);
            outputObjectToServer.flush();
            
            int length = inputObjectFromServer.readInt();
            for (int i = 0; i < length; i++) {
                temp.add(new Bullet(inputObjectFromServer));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
    }
    
    public synchronized List<Asteroid> playerGetAsteroid() {
        List<Asteroid> temp = Collections.synchronizedList(new ArrayList<>());;

        try {
            outputObjectToServer.writeInt(PLAYER_GET_ASTEROIDS);
            outputObjectToServer.flush();

            int length = inputObjectFromServer.readInt();
            for (int i = 0; i < length; i++) {
                temp.add(new Asteroid(inputObjectFromServer));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
    }
}
