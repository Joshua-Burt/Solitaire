import javax.swing.*;

public class SolitaireFrame extends JFrame {
    public SolitaireFrame() {
        add(new SolitairePanel());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[]args) {
        SolitaireFrame frame = new SolitaireFrame();
    }
}
