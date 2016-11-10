package asteroidsclient;

public class Ship {

    private int lives;
    private String imageFileName;
    private java.io.File imageFile;

    Ship(int lives, String imageFileName) {
        this.lives = lives;
        this.imageFile = new java.io.File(imageFileName);
    }

    public int getLives() {
        return lives;
    }

    public java.io.File getImageFile() {
        return imageFile;
    }

}
