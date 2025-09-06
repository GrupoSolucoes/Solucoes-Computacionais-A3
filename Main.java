import view.LoginView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            // Define o Look and Feel para o do sistema operacional (ex: Windows 10/11)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Garante que a UI seja criada na thread correta
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}