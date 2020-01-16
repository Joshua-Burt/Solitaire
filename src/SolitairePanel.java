import cards.Card;
import cards.Deck;
import cards.Pile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SolitairePanel extends JPanel {
    private BufferedImage cardOutline;
    Deck deck;
    ArrayList<Pile> piles;

    public SolitairePanel() {
        setBackground(new Color(27, 117, 33));
        preparePlayField();
        prepareDeck();
    }

    private void preparePlayField() {
        piles = new ArrayList<>();

        //Creating and adding each arraylist of a pile to the 'piles' arraylist
        for(int i = 0; i < 8; i++) {
            Pile pile = new Pile();
            piles.add(pile);
        }
    }

    private void prepareDeck() {
        //Create the cards.Deck object and shuffle it
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

    //TODO: Find a better way of doing this
    private ArrayList<Point> getCardDrawLocations(int panelWidth, int panelHeight) {
        ArrayList<Point> arr = new ArrayList<>();

        int y = panelHeight / 2 + 5;

        //This goes through and determines the location where each card will be drawn
        //It starts from the bottom row, right and goes to the left
        for(int i = 0; i < 7; i++) {
            for(int o = 6; o >= i; o--) {
                arr.add(new Point(15 + (110 * o), y + (20 * i)));
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
                // Do this by creating an arraylist for each of the seven piles, and will be able to compare
                // easily, such as the last card will always be able to be picked up and moved, but if multiple
                // cards are face up in a row AND in order, they can all be moved
                // Paint will draw each card using it's index in each arraylist
                // For the pile at the top right, it only needs to be drawn once and not for each card

                try {
                    if (i <= 27) {
                        if (i == 6 || i == 12 || i == 17 || i == 21 || i == 24 || i == 26 || i == 27) {
                            card.setFaceDown(false);
                        } else {
                            card.setFaceDown(true);
                        }

                        g.drawImage(card.getImg(), cardDrawLocations.get(i).x, cardDrawLocations.get(i).y, 90, 120, null);
                    } else {
                        card.setFaceDown(true);
                        g.drawImage(card.getImg(), 15, 15, 90, 120, null);
                    }
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawCardOutlines(Graphics g, int panelWidth, int panelHeight) {
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
