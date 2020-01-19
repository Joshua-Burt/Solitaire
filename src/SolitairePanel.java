import cards.Card;
import cards.Deck;
import cards.Pile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SolitairePanel extends JPanel {
    private BufferedImage cardOutline;
    private Deck deck;
    private ArrayList<Pile> bottomPiles, topPiles;
    private ArrayList<Point> bottomRowLocations, topRowLocations;
    private Pile discardPile, playableDiscard;
    private int panelHeight = 0;
    private int panelWidth = 0;

    public SolitairePanel() {
        setBackground(new Color(27, 117, 33));
        addMouseListener(new PressListener());

        bottomRowLocations = null;
        topRowLocations = null;
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

    private ArrayList<Point> getBottomRowLocations() {
        ArrayList<Point> arr = new ArrayList<>();
        int cardDist = 110;

        //This goes through and determines the location where each card will be drawn
        //It starts from the bottom row, left to right
        for(int i = 0; i < bottomPiles.size(); i++) {
            for (int o = 0; o <= i; o++) {
                arr.add(new Point(15 + (cardDist * i), panelHeight / 2 + 5 + (20 * o)));
            }
        }
        return arr;
    }

    private ArrayList<Point> getTopRowLocations() {
        ArrayList<Point> arr = new ArrayList<>();
        int cardDist = 110;

        //This goes through and determines the location where each card will be drawn
        //It starts from the bottom row, left to right
        for(int i = 0; i < topPiles.size(); i++) {
            arr.add(new Point(5 + panelWidth - cardDist * (4 - i), 15));
        }
        return arr;
    }

    //Each paint cycle this will be called to check if the card placements
    // need to be rearranged
    private void checkPanelSize() {
        if(this.getWidth() != panelWidth || this.getHeight() != panelHeight) {
            panelWidth = this.getWidth();
            panelHeight = this.getHeight();
            bottomRowLocations = getBottomRowLocations();
            topRowLocations = getTopRowLocations();
        }
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        checkPanelSize();
        drawCardOutlines(g, panelWidth, panelHeight);

        if(bottomPiles != null && topPiles != null & discardPile != null &&
           playableDiscard != null && bottomRowLocations != null) {

            drawBottomRowCards(g);
            drawTopRowCards(g);
            //Add later
            for(int i = 0; i < topPiles.size(); i++) {
                if(topPiles.get(i).size() != 0) {
                    g.drawImage(topPiles.get(i).get(0).getImg(), topRowLocations.get(i).x, topRowLocations.get(i).y, null);
                }
            }
            if(discardPile.size() != 0) {
                //Just draw one card instead of overlaying all of them
                g.drawImage(discardPile.get(0).getImg(), 15, 15, 90, 120, null);
            }

            if(playableDiscard.size() != 0) {
                //Just draw one card instead of overlaying all of them
                g.drawImage(playableDiscard.get(playableDiscard.size() - 1).getImg(), 125, 15, 90, 120, null);
            }
        }
    }

    private void drawBottomRowCards(Graphics g) {
        int workingIndex = 0;

        for(Pile bottomPile : bottomPiles) {
            if(bottomPile.size() != 0) {
                for (Card card : bottomPile) {
                    g.drawImage(card.getImg(), bottomRowLocations.get(workingIndex).x, bottomRowLocations.get(workingIndex).y, 90, 120, null);
                    workingIndex++;
                }
            }
        }
    }

    private void drawTopRowCards(Graphics g) {
        for(int i = 0; i < topPiles.size(); i++) {
            if (topPiles.get(i).size() != 0) {
                Card card = topPiles.get(i).get(0);

                g.drawImage(card.getImg(), topRowLocations.get(i).x, topRowLocations.get(i).y, 90, 120, null);
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

    private class PressListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            Card closestCard = getClosetCard(e.getPoint());


        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        private Card getClosetCard(Point mousePoint) {
            Card closestCard = null;
            int shortestDist = 1000000;

            for (Pile bottomPile : bottomPiles) {
                if(bottomPile.size() > 0) {
                    for (Card card : bottomPile) {
                        int workingDist = dist(mousePoint.x, mousePoint.y, card.getLocation().x, card.getLocation().y);
                        if (workingDist < shortestDist) {
                            shortestDist = workingDist;
                            closestCard = card;
                        }
                    }
                }
            }

            for (Pile topPile : topPiles) {
                if(topPile.size() > 0) {
                    for (Card card : topPile) {
                        int workingDist = dist(mousePoint.x, mousePoint.y, card.getLocation().x, card.getLocation().y);
                        if (workingDist < shortestDist) {
                            shortestDist = workingDist;
                            closestCard = card;
                        }
                    }
                }
            }

            return closestCard;
        }

        private int dist(int x1, int y1, int x2, int y2) {
            //Using sqrt((x2 − x1)^2 + (y2 − y1)^2)
            return (int)Math.round(Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2)));
        }
    }
}
