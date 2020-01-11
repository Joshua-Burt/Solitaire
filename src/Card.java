import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card {
    private String index;
    private String suit;
    private BufferedImage img;

    public Card(String index, String suit) throws IOException {
        this.index = index;
        this.suit = suit.toUpperCase();

        File file = new File("img/" + index + suit.toUpperCase() + ".png");
        img = ImageIO.read(file);
    }

    public String getIndex() {
        return index;
    }

    public String getSuit() {
        return suit;
    }

    public BufferedImage getImg() {
        return img;
    }
}
