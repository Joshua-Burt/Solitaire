package cards;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card {
    private int index;
    private String suit;
    private Point location;
    private BufferedImage img;
    private boolean isFaceDown;

    public Card(int index, String suit) {
        this.index = index;
        this.suit = suit.toUpperCase();

        try {
            File file = new File("src//img//cards//" + index + suit.toUpperCase() + ".png");
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getIndex() {
        return index;
    }

    public String getSuit() {
        return suit;
    }

    public BufferedImage getImg() {
        return img;
    }

    public boolean isFaceDown() {
        return isFaceDown;
    }

    public void setFaceDown(boolean faceDown) {
        isFaceDown = faceDown;

        if(isFaceDown) {
            try {
                File file = new File("src//img//cards//blue_back.png");
                img = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File file = new File("src//img//cards//" + index + suit.toUpperCase() + ".png");
                img = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
