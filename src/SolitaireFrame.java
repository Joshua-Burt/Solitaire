import javax.swing.*;

public class SolitaireFrame extends JFrame {
    public SolitaireFrame() {
        add(new SolitairePanel());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
