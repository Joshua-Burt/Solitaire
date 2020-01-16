import cards.Card;
import cards.Deck;
import cards.Pile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

public class SolitairePanel extends JPanel {
    private BufferedImage cardOutline;
    Deck deck;
    ArrayList<Pile> bottomPiles, topPiles;
    Pile discardPile, playableDiscard;
    int panelHeight = 0;
    int panelWidth = 0;
    ArrayList<Point> cardDrawLocations;

    public SolitairePanel() {
        setBackground(new Color(27, 117, 33));

        cardDrawLocations = null;
        prepareDeck();
        preparePiles();
        dealCards();
    }

    //Instantiates and shuffles a deck object and retrieves the card outline image
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

    //Instantiates each pile object and adds them to arraylists
    private void preparePiles() {
        bottomPiles = new ArrayList<>();
        topPiles = new ArrayList<>();

        // Instantiate the piles at the top left. One for the face down and one for face up cards
        discardPile = new Pile();
        playableDiscard = new Pile();

        for(int i = 0; i < 7; i++) {
            bottomPiles.add(new Pile());
        }
        for(int i = 0; i < 4; i++) {
            topPiles.add(new Pile());
        }
    }

    //Plays the initial card placements into their piles
    private void dealCards() {
        //The last index of the deck that has been placed
        int workingIndex = 0;

        //Bottom row
        for(int i = 0; i < 7; i++) {
            for(int o = 0; o <= i; o++) {
                bottomPiles.get(i).add(deck.get(workingIndex));

                try {
                    bottomPiles.get(i).get(o).setFaceDown(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                workingIndex++;
            }
        }

        //Add the left over cards into the pile at the top left
        for(int i = workingIndex; i < deck.size(); i++) {
            try {
                deck.get(i).setFaceDown(true);
                discardPile.add(deck.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Set each card to face up or face down
        for (Pile bottomPile : bottomPiles) {
            try {
                bottomPile.get(bottomPile.size() - 1).setFaceDown(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Point> getCardDrawLocations(int panelWidth, int panelHeight) {
        ArrayList<Point> arr = new ArrayList<>();

        //This goes through and determines the location where each card will be drawn
        //It starts from the bottom row, left to right
        for(int i = 0; i < bottomPiles.size(); i++) {
            for (int o = 0; o <= i; o++) {
                arr.add(new Point(15 + (110 * i), panelHeight / 2 + 5 + (20 * o)));
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

        if(this.getWidth() != panelWidth || this.getHeight() != panelHeight) {
            panelWidth = this.getWidth();
            panelHeight = this.getHeight();
            cardDrawLocations = getCardDrawLocations(panelWidth, panelHeight);
        }

        drawCardOutlines(g, panelWidth, panelHeight);

        if(bottomPiles != null && topPiles != null & discardPile != null && playableDiscard != null && cardDrawLocations != null) {
            int workingIndex = 0;
            for(int i = 0; i < bottomPiles.size(); i++) {
                for (int o = 0; o <= i; o++) {
                    Card card = bottomPiles.get(i).get(o);

                    //TODO: add ability for each card to know where it is in the piles and which pile
                    // Do this by creating an arraylist for each of the seven piles, and will be able to compare
                    // easily, such as the last card will always be able to be picked up and moved, but if multiple
                    // cards are face up in a row AND in order, they can all be moved
                    // Paint will draw each card using it's index in each arraylist
                    // For the pile at the top right, it only needs to be drawn once and not for each card

                    g.drawImage(card.getImg(), cardDrawLocations.get(workingIndex).x, cardDrawLocations.get(workingIndex).y, 90, 120, null);
                    workingIndex++;
                }
            }
            if(discardPile.size() != 0) {
                //Just draw one card instead of overlaying all of them
                g.drawImage(discardPile.get(0).getImg(), 15, 15, 90, 120, null);
            }

            if(playableDiscard.size() != 0) {
                g.drawImage(playableDiscard.get(playableDiscard.size() - 1).getImg(), 15, 15, 90, 120, null);
            }
        }
    }

    //Draws the outlines for where the cards can be placed
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
