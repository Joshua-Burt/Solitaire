import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SolitairePanel extends JPanel {
    private BufferedImage cardOutline;
    Deck deck;

    public SolitairePanel() {
        setBackground(new Color(27, 117, 33));
        prepareDeck();
    }

    private void prepareDeck() {
        deck = new Deck();
        deck.shuffle();

        try {
            File file = new File("src/img/cards/cardOutline.png");
            cardOutline = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g)
    {
        int cardDist = 110; // Distance between each card outline being drawn

        super.paintComponent(g);

        drawCardOutlines(g, cardDist);

        if(deck != null) {
            for (Card card : deck) {
                g.drawImage(card.getImg(), 15, 15, 90, 120, null);
            }
        }
    }

    private void drawCardOutlines(Graphics g, int cardDist) {
        int panelHeight = this.getHeight();
        int panelWidth = this.getWidth();

        //Top card outlines
        g.drawImage(cardOutline, 10, 10, null);
        for(int i = 0; i < 5; i++) {
            g.drawImage(cardOutline, panelWidth - cardDist * i, 10, null);
        }

        //Bottom card outlines
        for(int i = 0; i < 7; i++) {
            g.drawImage(cardOutline, 10 + cardDist * i, panelHeight / 2, null);
        }
    }
}
