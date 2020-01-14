import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Deck extends ArrayList<Card> {
    String[] suits;
    String[] indices;

    public Deck() {
        this.suits = new String[]{"C","D","H","S"};
        this.indices = new String[]{"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

        createCards();
    }

    private void createCards() {
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
            int randomIndexToSwap = rand.nextInt(this.size());
            Card temp = this.get(randomIndexToSwap);
            this.set(randomIndexToSwap, this.get(i));
            this.set(i, temp);
        }
    }
}
