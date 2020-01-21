package cards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Deck extends ArrayList<Card> {
    public ArrayList<String> suits;
    public ArrayList<String> indices;

    public Deck() {
        this.suits = new ArrayList<>(Arrays.asList("C","D","S","H"));
        this.indices = new ArrayList<>(Arrays.asList("A","2","3","4","5","6","7","8","9","10","J","Q","K"));

        createCards();
    }

    private void createCards() {
        // Two for loops to create each combination of card
        for(String suit : suits) {
            for (String index : indices) {
                try {
                    Card card = new Card(index, suit);
                    this.add(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void shuffle() {
        Random rand = new Random();

        for (int i = 0; i < this.size(); i++) {
            int cardToSwapIndex = rand.nextInt(this.size());
            Card temp = this.get(cardToSwapIndex);
            this.set(cardToSwapIndex, this.get(i));
            this.set(i, temp);
        }
    }
}
