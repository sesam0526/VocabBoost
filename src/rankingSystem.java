import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class rankingSystem {
    private JPanel rankingSystemPanel;
    private JTable rankingTable;
    private DefaultTableModel model;

    public rankingSystem() {
        rankingTable = new JTable();
        rankingSystemPanel = new JPanel(new BorderLayout());
        rankingSystemPanel.setPreferredSize(new Dimension(800, 600));
        JScrollPane scrollPane = new JScrollPane(rankingTable);
        rankingSystemPanel.add(scrollPane, BorderLayout.CENTER);

        // Change table header color
        rankingTable.getTableHeader().setBackground(new Color(212, 185, 150));
        rankingTable.getTableHeader().setForeground(Color.WHITE);

        // Set table font
        Font tableFont = new Font("Pyunji R", Font.BOLD, 32);
        rankingTable.setFont(tableFont);

        // Set row height
        int rowHeight = 40;
        rankingTable.setRowHeight(rowHeight);

        // columns
        String[] columns = {"No.", "유저", "점수", "정답률"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        rankingTable.setModel(model);

        // Set cell renderer for data cells
        DefaultTableCellRenderer dataCellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(Color.WHITE);
                c.setForeground(new Color(212, 185, 150));
                return c;
            }
        };

        // Set cell renderer for header cells
        DefaultTableCellRenderer headerCellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(212, 185, 150));
                c.setForeground(Color.WHITE);
                return c;
            }
        };

        for (int i = 0; i < rankingTable.getColumnCount(); i++) {
            rankingTable.getColumnModel().getColumn(i).setCellRenderer(dataCellRenderer);
            rankingTable.getColumnModel().getColumn(i).setHeaderRenderer(headerCellRenderer);
        }

        // load data
        loadRankingData();

        // Set table height based on row count
        int rowCount = rankingTable.getRowCount();
        int height = rowCount * rowHeight;
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, height));
    }

    private void loadRankingData() {
        final String DB_URL = "jdbc:mysql://localhost/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "SELECT userName, score, percentage FROM ranking ORDER BY score DESC, percentage DESC";
            ResultSet resultSet = stmt.executeQuery(sql);

            int rank = 0;
            while (resultSet.next()) {
                rank++;
                String user = resultSet.getString("userName");
                int score = resultSet.getInt("score");
                double percentage = resultSet.getDouble("percentage");
                model.addRow(new Object[]{rank, user, score, percentage});
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Container getRankingSystemPanel() {
        return rankingSystemPanel;
    }
}