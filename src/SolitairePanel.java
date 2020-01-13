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
        deck = new Deck();
        setBackground(new Color(27, 117, 33));

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
        g.drawImage(cardOutline, panelWidth - cardDist * 4, 10, null);
        g.drawImage(cardOutline, panelWidth - cardDist * 3, 10, null);
        g.drawImage(cardOutline, panelWidth - cardDist * 2, 10, null);
        g.drawImage(cardOutline, panelWidth - cardDist, 10, null);

        //Bottom card outlines
        g.drawImage(cardOutline, 10, panelHeight / 2, null);
        g.drawImage(cardOutline, 10 + cardDist, panelHeight / 2, null);
        g.drawImage(cardOutline, 10 + cardDist * 2, panelHeight / 2, null);
        g.drawImage(cardOutline, 10 + cardDist * 3, panelHeight / 2, null);
        g.drawImage(cardOutline, 10 + cardDist * 4, panelHeight / 2, null);
        g.drawImage(cardOutline, 10 + cardDist * 5, panelHeight / 2, null);
        g.drawImage(cardOutline, 10 + cardDist * 6, panelHeight / 2, null);
    }
}
