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
    private int cardImageWidth;
    private int cardImageHeight;

    public SolitairePanel() {
        setBackground(new Color(27, 117, 33));
        addMouseListener(new PressListener());

        bottomRowLocations = null;
        topRowLocations = null;
        prepareDeck();
        preparePiles();
        dealCards();

        cardImageWidth = 90;
        cardImageHeight = 120;
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
                bottomPiles.get(i).get(o).setFaceDown(true);
                workingIndex++;
            }
        }

        //Add the left over cards into the pile at the top left
        for(int i = workingIndex; i < deck.size(); i++) {
            deck.get(i).setFaceDown(true);
            discardPile.add(deck.get(i));
        }

        //Set each card to face up or face down
        for (Pile bottomPile : bottomPiles) {
            bottomPile.get(bottomPile.size() - 1).setFaceDown(false);
        }
    }

    private ArrayList<Point> getBottomRowLocations() {
        ArrayList<Point> arr = new ArrayList<>();
        int cardDist = 110;

        //This goes through and determines the location where each card will be drawn
        //It starts from the bottom row, left to right
        for(int i = 0; i < bottomPiles.size(); i++) {
            for(int o = 0; o < bottomPiles.get(i).size(); o++) {
                int x = 15 + (cardDist * i);
                int y = panelHeight / 2 + 5 + (20 * o);

                arr.add(new Point(x,y));
                bottomPiles.get(i).get(o).setLocation(new Point(x + cardImageWidth / 2, y));
            }
        }
        return arr;
    }

    private ArrayList<Point> getTopRowLocations() {
        ArrayList<Point> arr = new ArrayList<>();
        int cardDist = 110;

        discardPile.get(0).setLocation(new Point(15 + cardImageWidth / 2, 15));

        //This goes through and determines the location where each card will be drawn
        //It starts from the bottom row, left to right
        for(int i = 0; i < topPiles.size(); i++) {
            int x = 5 + panelWidth - cardDist * (4 - i);
            int y = 15;
            arr.add(new Point(x,y));
            if(topPiles.get(i).size() > 0) {
                topPiles.get(i).get(0).setLocation(new Point(x + cardImageWidth / 2, y));
            }
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
            drawDiscardPileCards(g);
        }
    }

    private void drawBottomRowCards(Graphics g) {
        int workingIndex = 0;
        for (Pile bottomPile : bottomPiles) {
            for (Card card : bottomPile) {
                Point workingPoint = bottomRowLocations.get(workingIndex);
                g.drawImage(card.getImg(), workingPoint.x, workingPoint.y, cardImageWidth, cardImageHeight, null);
                workingIndex++;
            }
        }
        repaint();
    }

    private void drawTopRowCards(Graphics g) {
        for(int i = 0; i < topPiles.size(); i++) {
            if (topPiles.get(i).size() != 0) {
                Card card = topPiles.get(i).get(0);

                g.drawImage(card.getImg(), topRowLocations.get(i).x, topRowLocations.get(i).y, cardImageWidth, cardImageHeight, null);
            }
        }
        repaint();
    }

    private void drawDiscardPileCards(Graphics g) {
        if(discardPile.size() != 0) {
            //Just draw one card instead of overlaying all of them
            g.drawImage(discardPile.get(0).getImg(), 15, 15, cardImageWidth, cardImageHeight, null);
        }

        if(playableDiscard.size() != 0) {
            //Just draw one card instead of overlaying all of them
            g.drawImage(playableDiscard.get(playableDiscard.size() - 1).getImg(), 125, 15, cardImageWidth, cardImageHeight, null);
        }
        repaint();
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
        repaint();
    }

    private class PressListener implements MouseListener {
        Card card1 = null;
        Card card2 = null;
        Card closestCard = null;
        int shortestDist = 1000000;

        @Override
        public void mouseClicked(MouseEvent e) {
            getClosetCard(e.getPoint());

            if(card1 == null) {
                card1 = closestCard;
            } else {
                card2 = closestCard;
            }

            if(card1 != null && card2 != null) {
                int card1Index = card1.getIndex();
                int card2Index = card2.getIndex();
                String card1Suit = card1.getSuit();
                String card2Suit = card2.getSuit();

                Pile card1Pile = null;
                Pile card2Pile = null;

                boolean isMatch = checkCardSuits(card1Suit, card2Suit);

                if(card1Index == card2Index - 1 && isMatch) {
                    for (Pile bottomPile : bottomPiles) {
                        if (bottomPile.contains(card1)) {
                            card1Pile = bottomPile;
                        }
                        if (bottomPile.contains(card2)) {
                            card2Pile = bottomPile;
                        }
                    }
                    for (Pile topPile : topPiles) {
                        if (topPile.contains(card1)) {
                            card1Pile = topPile;
                        }
                        if (topPile.contains(card2)) {
                            card2Pile = topPile;
                        }
                    }
                    card1.setLocation(new Point(card2.getLocation().x, card2.getLocation().y + 20));

                    if(card1Pile != null && card2Pile != null) {
                        card2Pile.add(card1);
                        card1Pile.remove(card1);

                        //card1Ple.size() != 0 if it is the last card in the pile
                        if (card1Pile.size() != 0 && card1Pile.get(card1Pile.size() - 1).isFaceDown()) {
                            card1Pile.get(card1Pile.size() - 1).setFaceDown(false);
                        }

                        bottomRowLocations = getBottomRowLocations();
                        topRowLocations = getTopRowLocations();

                        closestCard = null;
                        shortestDist = 1000000;
                        card1 = null;
                        card2 = null;
                    }
                }
            }
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

        private void getClosetCard(Point mousePoint) {
            closestCard = checkPile(bottomPiles, mousePoint, null);
            closestCard = checkPile(topPiles, mousePoint, closestCard);

//            //Check the bottom piles for the distance
//            for (int i = 0; i < bottomPiles.size(); i++) {
//                Pile bottomPile = bottomPiles.get(i);
//                if (bottomPile.size() > 0) {
//                    for (Card card : bottomPile) {
//                        int workingDist = dist(mousePoint.x, mousePoint.y, card.getLocation().x, card.getLocation().y);
//                        if (workingDist < shortestDist && !card.isFaceDown()) {
//                            shortestDist = workingDist;
//                            closestCard = card;
//                        }
//                    }
//                }
//            }

//            //Check the top piles for the distance
//            for (Pile topPile : topPiles) {
//                if(topPile.size() > 0) {
//                    for (Card card : topPile) {
//                        int workingDist = dist(mousePoint.x, mousePoint.y, card.getLocation().x, card.getLocation().y);
//                        if (workingDist < shortestDist && !card.isFaceDown()) {
//                            shortestDist = workingDist;
//                            closestCard = card;
//                        }
//                    }
//                }
//            }

            //Check the discard pile for the distance
            if(discardPile.size() > 0) {
                Point pnt = discardPile.get(0).getLocation();
                int workingDist = dist(mousePoint.x, mousePoint.y, pnt.x, pnt.y);
                if(workingDist < shortestDist) {
                    shortestDist = workingDist;
                    closestCard = discardPile.get(0);
                }

                //Check the bottom of the discard pile as well
                workingDist = dist(mousePoint.x, mousePoint.y, pnt.x, pnt.y + cardImageHeight);
                if(workingDist < shortestDist) {
                    shortestDist = workingDist;
                    closestCard = discardPile.get(0);
                }
            }

            //Check the playable discard pile for the distance
            if(playableDiscard.size() > 0) {
                Point pnt = playableDiscard.get(0).getLocation();
                int workingDist = dist(mousePoint.x, mousePoint.y, pnt.x, pnt.y);
                if(workingDist < shortestDist) {
                    closestCard = playableDiscard.get(0);
                }

                //Check the bottom of the discard pile as well
                workingDist = dist(mousePoint.x, mousePoint.y, pnt.x, pnt.y + cardImageHeight);
                if(workingDist < shortestDist) {
                    closestCard = playableDiscard.get(0);
                }
            }
            System.out.println(closestCard.getIndex() + closestCard.getSuit());
        }

        //Returns the distance between two given points
        private int dist(int x1, int y1, int x2, int y2) {
            //Using sqrt((x2 − x1)^2 + (y2 − y1)^2)
            return (int)Math.round(Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2)));
        }

        private Card checkPile(ArrayList<Pile> Pile, Point mousePoint, Card closestCard) {
            for (Pile pile : Pile) {
                if(pile.size() > 0) {
                    for (Card card : pile) {
                        int workingDist = dist(mousePoint.x, mousePoint.y, card.getLocation().x, card.getLocation().y);
                        if (workingDist < shortestDist && !card.isFaceDown()) {
                            shortestDist = workingDist;
                            closestCard = card;
                        }
                    }
                }
            }

            return closestCard;
        }

        //Returns true if the chosen cards are opposite suits
        private boolean checkCardSuits(String card1Suit, String card2Suit) {
            boolean isMatch = false;
            switch(card1Suit) {
                case "H":
                case "D":
                    if(card2Suit.equals("S") || card2Suit.equals("C")) {
                        isMatch = true;
                    }
                    break;
                case "S":
                case "C":
                    if(card2Suit.equals("H") || card2Suit.equals("D")) {
                        isMatch = true;
                    }
                    break;
            }
            return isMatch;
        }
    }
}