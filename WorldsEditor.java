import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldsEditor extends JFrame {

    private JTabbedPane tabbedPane;
    private List<TabInfo> openTabsInfo = new ArrayList<>();
    private FindDialog findDialog;

    // Clase interna actualizada para usar los nuevos nombres de Persister
    private static class TabInfo {
        File originalFile;
        Persister.BookmarkData originalData;
        JTable table;
        DefaultTableModel tableModel;
        String fileType;

        TabInfo(File file, Persister.BookmarkData data, JTable table, String type) {
            this.originalFile = file;
            this.originalData = data;
            this.table = table;
            this.tableModel = (DefaultTableModel) table.getModel();
            this.fileType = type;
        }
    }

    public WorldsEditor() {
        setTitle("Worldmarks & Avatars list editor");
        setSize(1200, 800);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        try {
            setIconImage(new ImageIcon(getClass().getResource("/assets/icon.ico")).getImage()); 
        } catch (Exception e) { System.err.println("Icono 'assets/icon.ico' no encontrado."); }

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        tabbedPane = createTabbedPane();
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createButtonPanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(45, 45, 45));
        container.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(45, 45, 45));
        buttonPanel.add(createIconButton("assets/newlist.png", "New List", e -> onNewList()));
        buttonPanel.add(createIconButton("assets/folder.png", "Open File", e -> onOpenFile()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(createIconButton("assets/new.png", "Add Row", e -> onAddRow()));
        buttonPanel.add(createIconButton("assets/delete.png", "Delete Row", e -> onDeleteRow()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(createIconButton("assets/up.png", "Move Row Up", e -> onMoveRow(-1)));
        buttonPanel.add(createIconButton("assets/down.png", "Move Row Down", e -> onMoveRow(1)));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(createIconButton("assets/save.png", "Save File", e -> onSaveFile()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(createIconButton("assets/find.png", "Find", e -> onFind()));
        container.add(buttonPanel);
        return container;
    }
    
    private JButton createIconButton(String path, String tooltip, ActionListener listener) {
        JButton button;
        try {
            button = new JButton(new ImageIcon(getClass().getResource(path)));
        } catch (Exception e) {
            System.err.println("Icono '" + path + "' no encontrado.");
            button = new JButton(tooltip.substring(0, 1));
        }
        button.setToolTipText(tooltip);
        button.addActionListener(listener);
        button.setPreferredSize(new Dimension(40, 40));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }
    
    private JTabbedPane createTabbedPane() {
        return new JTabbedPane();
    }

    private void onNewList() {
        Object[] options = {".worldsmarks", ".avatars"};
        int choice = JOptionPane.showOptionDialog(this, "What type of file do you want to create?", "New List", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == JOptionPane.CLOSED_OPTION) return;
        if (choice == JOptionPane.YES_OPTION) {
            List<Persister.BookmarkEntry> entries = new ArrayList<>();
            entries.add(new Persister.BookmarkEntry("New entry", "home:new/new.world"));
            Persister.BookmarkData data = new Persister.BookmarkData("NET.worlds.console.BookmarkMenuItem", 7, entries);
            addNewTab("Untitled.worldsmarks", null, data, ".worldsmarks");
        } else {
            List<Persister.BookmarkEntry> entries = new ArrayList<>();
            entries.add(new Persister.BookmarkEntry("New avatar", "avatar:body.code.rwg"));
            Persister.BookmarkData data = new Persister.BookmarkData("NET.worlds.console.AvatarMenuItem", 7, entries);
            addNewTab("Untitled.avatars", null, data, ".avatars");
        }
    }

    private void onOpenFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Supported Files", "worldsmarks", "avatars"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                Persister.BookmarkData data = Persister.loadFromFile(selectedFile);
                String fileType = selectedFile.getName().toLowerCase().endsWith(".avatars") ? ".avatars" : ".worldsmarks";
                addNewTab(selectedFile.getName(), selectedFile, data, fileType);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Could not read the file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void addNewTab(String title, File file, Persister.BookmarkData data, String fileType) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Name", "Value"}, 0);
        for (Persister.BookmarkEntry entry : data.entries) {
            model.addRow(new Object[]{entry.name.trim(), entry.value.trim()});
        }
        JTable table = new JTable(model);
        
        table.setBackground(new Color(45, 45, 45));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));
        table.getTableHeader().setBackground(new Color(30, 30, 30));
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(45, 45, 45));
        scrollPane.setBorder(null);

        int tabIndex = tabbedPane.getTabCount();
        tabbedPane.addTab(title, scrollPane);

        JPanel tabComponent = new JPanel(new BorderLayout(5, 0));
        tabComponent.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        JButton closeButton = new JButton("x");
        closeButton.setUI(new BasicButtonUI());
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusable(false);
        closeButton.setBorder(BorderFactory.createEtchedBorder());
        closeButton.setForeground(Color.WHITE);
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setPreferredSize(new Dimension(18, 18));
        closeButton.addActionListener(e -> {
            int i = tabbedPane.indexOfTabComponent(tabComponent);
            if (i != -1) {
                tabbedPane.remove(i);
                openTabsInfo.remove(i);
            }
        });
        
        tabComponent.add(titleLabel, BorderLayout.CENTER);
        tabComponent.add(closeButton, BorderLayout.EAST);
        tabbedPane.setTabComponentAt(tabIndex, tabComponent);
        
        openTabsInfo.add(new TabInfo(file, data, table, fileType));
        tabbedPane.setSelectedIndex(tabIndex);
    }

    private void onAddRow() {
        TabInfo currentTab = getActiveTabInfo();
        if (currentTab == null) return;
        int insertPos = currentTab.table.getSelectedRow() + 1;
        if (insertPos == 0) insertPos = currentTab.tableModel.getRowCount();
        if (currentTab.fileType.equals(".avatars")) {
            currentTab.tableModel.insertRow(insertPos, new Object[]{"New avatar", "avatar:body.code.rwg"});
        } else {
            currentTab.tableModel.insertRow(insertPos, new Object[]{"New entry", "home:new/new.world"});
        }
    }
    
    private void onDeleteRow() {
        TabInfo currentTab = getActiveTabInfo();
        if (currentTab == null) return;
        int[] selectedRows = currentTab.table.getSelectedRows();
        if (selectedRows.length == 0) return;
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + selectedRows.length + " row(s)?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                currentTab.tableModel.removeRow(selectedRows[i]);
            }
        }
    }
    
    private void onMoveRow(int direction) {
        TabInfo currentTab = getActiveTabInfo();
        if (currentTab == null) return;
        int row = currentTab.table.getSelectedRow();
        if (row == -1) return;
        int targetRow = row + direction;
        if (targetRow >= 0 && targetRow < currentTab.tableModel.getRowCount()) {
            currentTab.tableModel.moveRow(row, row, targetRow);
            currentTab.table.setRowSelectionInterval(targetRow, targetRow);
        }
    }

    private void onFind() {
        if (getActiveTable() == null) {
            JOptionPane.showMessageDialog(this, "Please open a file first.", "No Active Tab", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (findDialog == null) {
            findDialog = new FindDialog(this);
        }
        findDialog.setVisible(true);
        findDialog.requestFocusInWindow();
    }
    
    private void onSaveFile() {
        TabInfo currentTab = getActiveTabInfo();
        if (currentTab == null) {
            JOptionPane.showMessageDialog(this, "There is no open tab to save.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (currentTab.table.isEditing()) {
            currentTab.table.getCellEditor().stopCellEditing();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save File As");
        fileChooser.setFileFilter(new FileNameExtensionFilter(currentTab.fileType + " files", currentTab.fileType.substring(1)));
        fileChooser.setSelectedFile(currentTab.originalFile != null ? currentTab.originalFile : new File("Untitled" + currentTab.fileType));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            
            List<Persister.BookmarkEntry> currentEntries = new ArrayList<>();
            for (int i = 0; i < currentTab.tableModel.getRowCount(); i++) {
                String name = (String) currentTab.tableModel.getValueAt(i, 0);
                String value = (String) currentTab.tableModel.getValueAt(i, 1);
                currentEntries.add(new Persister.BookmarkEntry(name, value));
            }
            Persister.BookmarkData saveData = new Persister.BookmarkData(
                currentTab.originalData.type,
                currentTab.originalData.version,
                currentEntries
            );
            
            try {
                Persister.saveToFile(saveFile, saveData, currentTab.originalFile);
                currentTab.originalFile = saveFile;
                currentTab.originalData = saveData;
                
                JOptionPane.showMessageDialog(this, "File saved successfully at:\n" + saveFile.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Could not save the file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public JTable getActiveTable() {
        TabInfo info = getActiveTabInfo();
        return (info != null) ? info.table : null;
    }
    
    private TabInfo getActiveTabInfo() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex == -1) return null;
        return openTabsInfo.get(selectedIndex);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("control", new Color(45, 45, 45));
            UIManager.put("info", new Color(45, 45, 45));
            UIManager.put("nimbusBase", new Color(30, 30, 30));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusLightBackground", new Color(45, 45, 45));
            UIManager.put("nimbusSelectionBackground", new Color(60, 60, 60));
            UIManager.put("text", new Color(230, 230, 230));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new WorldsEditor().setVisible(true));
    }
}