import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class FindDialog extends JDialog {

    private WorldsEditor owner;
    private JTextField searchField;
    private JButton findNextButton;
    private JButton findPreviousButton;

    public FindDialog(WorldsEditor owner) {
        super(owner, "Find", false); // false = no modal
        this.owner = owner;

        // --- Creación de Componentes ---
        searchField = new JTextField(20);
        findNextButton = createNavButton("assets/down.png", "Find Next");
        findPreviousButton = createNavButton("assets/up.png", "Find Previous");

        // --- Configuración del Layout ---
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Find:"));
        panel.add(searchField);
        panel.add(findPreviousButton);
        panel.add(findNextButton);
        
        // --- Listeners para la funcionalidad ---
        findNextButton.addActionListener(e -> search(true));
        findPreviousButton.addActionListener(e -> search(false));
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(true); }
            public void removeUpdate(DocumentEvent e) { search(true); }
            public void changedUpdate(DocumentEvent e) { search(true); }
        });

        // --- Configuración Final del Diálogo ---
        setContentPane(panel);
        pack(); // Ajusta el tamaño al contenido
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    }

    private JButton createNavButton(String path, String tooltip) {
        JButton button;
        try {
            ImageIcon icon = new ImageIcon(owner.getClass().getResource(path));
            Image scaledImg = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            button = new JButton(new ImageIcon(scaledImg));
        } catch (Exception e) {
            button = new JButton(tooltip.equals("Find Next") ? ">" : "<");
        }
        button.setToolTipText(tooltip);
        button.setMargin(new Insets(2, 2, 2, 2));
        return button;
    }

    private void search(boolean forward) {
        JTable activeTable = owner.getActiveTable();
        if (activeTable == null) return;

        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) return;

        int startRow = activeTable.getSelectedRow();
        int startCol = activeTable.getSelectedColumn();

        if (startRow == -1) startRow = 0;
        if (startCol == -1) startCol = 0;

        int rowCount = activeTable.getRowCount();
        int colCount = activeTable.getColumnCount();

        int currentRow = startRow;
        int currentCol = startCol;

        // Bucle para buscar en la tabla
        for (int i = 0; i < rowCount * colCount; i++) {
            if (forward) {
                currentCol++;
                if (currentCol >= colCount) {
                    currentCol = 0;
                    currentRow++;
                    if (currentRow >= rowCount) {
                        currentRow = 0;
                    }
                }
            } else { // backward
                currentCol--;
                if (currentCol < 0) {
                    currentCol = colCount - 1;
                    currentRow--;
                    if (currentRow < 0) {
                        currentRow = rowCount - 1;
                    }
                }
            }

            Object cellValue = activeTable.getValueAt(currentRow, currentCol);
            if (cellValue != null && cellValue.toString().toLowerCase().contains(searchText)) {
                // Encontrado
                activeTable.changeSelection(currentRow, currentCol, false, false);
                activeTable.scrollRectToVisible(activeTable.getCellRect(currentRow, currentCol, true));
                return;
            }
        }
        
        // Si no se encuentra, se podría añadir un feedback (ej. borde rojo)
        searchField.setBackground(Color.PINK);
        Timer timer = new Timer(500, e -> searchField.setBackground(Color.WHITE));
        timer.setRepeats(false);
        timer.start();
    }
}