import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SolitairePanel extends JPanel {
    private BufferedImage img;

    public SolitairePanel() {
        Deck deck = new Deck();

        try {
            File file = new File("src//img//cards//background.png");
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
