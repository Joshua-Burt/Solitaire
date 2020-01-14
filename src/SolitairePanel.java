import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

public class SolitairePanel extends JPanel {
    private BufferedImage cardOutline;
    Deck deck;

    public SolitairePanel() {
        setBackground(new Color(27, 117, 33));
        prepareDeck();
    }

    private void prepareDeck() {
        //Create the Deck object and shuffle it
        deck = new Deck();
        deck.shuffle();

        //Import the card outline image
        try {
            File file = new File("src/img/cards/cardOutline.png");
            cardOutline = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Point> getCardDrawLocations(int panelWidth, int panelHeight) {
        ArrayList<Point> arr = new ArrayList<>();
        int cardOutlineDist = 110;


        //This goes through and determines the location where each card will be drawn
        //It starts from the bottom row, right and goes to the left
        for(int i = 0; i < 6; i++) {
            for(int o = 6; o >= i; o--) {
                Point p = new Point();
                p.setLocation(15 + 110 * o, panelHeight / 2 + 5 + (20 * i));
                arr.add(p);
            }
        }

        //Top left
        Point p = new Point();
        p.setLocation(15,15);
        arr.add(p);

        return arr;
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        int panelHeight = this.getHeight();
        int panelWidth = this.getWidth();

        ArrayList<Point> cardDrawLocations;

        cardDrawLocations = getCardDrawLocations(panelWidth, panelHeight);

        drawCardOutlines(g, panelWidth, panelHeight);

        if(deck != null) {
            for (int i = 0; i < deck.size(); i++) {
                Card card = deck.get(i);

                //TODO: add ability for each card to know where it is in the piles and which pile

                if(i <= 27) {
                    if(i != 0) {
                        try {
                            card.setFaceDown(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    g.drawImage(card.getImg(), cardDrawLocations.get(i).x, cardDrawLocations.get(i).y, 90, 120, null);

                } else {
                    try {
                        card.setFaceDown(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    g.drawImage(card.getImg(), 15, 15, 90, 120, null);
                }
            }
        }
    }

    private void drawCardOutlines(@NotNull Graphics g, int panelWidth, int panelHeight) {
        // Distance between each card outline image
        int cardOutlineDist = 110;

        //Top card outlines
        g.drawImage(cardOutline, 10, 10, null);
        for(int i = 0; i < 5; i++) {
            g.drawImage(cardOutline, panelWidth - cardOutlineDist * i, 10, null);
        }

        //Bottom card outlines
        for(int i = 0; i < 7; i++) {
            g.drawImage(cardOutline, 10 + cardOutlineDist * i, panelHeight / 2, null);
        }
    }
}
