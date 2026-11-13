import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import GUI.TelaPrincipal;

public class Principal {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", true);
            
        } catch (Exception ex) {
            System.err.println("Falha ao inicializar o FlatLaf. Usando o padrão do sistema.");
        }

        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}