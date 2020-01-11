import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card {
    private int index;
    private String suit;
    private BufferedImage img;

    public Card(int index, String suit) throws IOException {
        this.index = index;
        this.suit = suit.toUpperCase();

        File file = new File("img/" + index + suit.toUpperCase() + ".png");
        img = ImageIO.read(file);
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
}
