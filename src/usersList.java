import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class usersList {
    private JButton btnAdd;
    private JButton btnDelete;
    private JList<String> usersList;
    private JPanel usersListPanel;
    private Vector<String> userListData;

    public usersList() {
        userListData = new Vector<>(); // userListData 변수 초기화
        fetchUsers();
        usersListPanel.setPreferredSize(new Dimension(1100, 600));
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField userNameField = new JTextField();
                JPasswordField passwordField = new JPasswordField();
                JTextField emailField = new JTextField();

                Object[] fields = {
                        "User Name:", userNameField,
                        "Password:", passwordField,
                        "Email:", emailField
                };

                int option = JOptionPane.showConfirmDialog(null, fields, "Add User", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String userName = userNameField.getText();
                    String password = passwordField.getText();
                    String email = emailField.getText();

                    addUser(userName, password, email);
                    fetchUsers();  // Refresh the user list
                }
            }
        });


        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = usersList.getSelectedValue();
                if (selectedUser == null) {
                    JOptionPane.showMessageDialog(null, "Please select a user first.");
                    return;
                }
                String userName = selectedUser.substring(selectedUser.indexOf("User: ") + 6, selectedUser.indexOf(" |")).trim();

                int dialogResult = JOptionPane.showConfirmDialog(null,
                        "Would you like to delete User: " + userName + "?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    removeUser(userName);
                    fetchUsers();  // Refresh the user list
                }
            }
        });
    }

    private void addUser(String userName, String password, String email) {
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String DB_USERNAME = "root";
        final String DB_PASSWORD = "0000";
        final String SELECT_QUERY = "SELECT userName FROM users WHERE userName = ?";
        final String INSERT_QUERY = "INSERT INTO users (userName, password, email) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement selectStatement = connection.prepareStatement(SELECT_QUERY);
             PreparedStatement insertStatement = connection.prepareStatement(INSERT_QUERY)) {

            // Check if the user already exists
            selectStatement.setString(1, userName);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "User already exists.");
                return;
            }

            // Add the user
            insertStatement.setString(1, userName);
            insertStatement.setString(2, password);
            insertStatement.setString(3, email);

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "User successfully added.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add user.");
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while adding user to the database.");
        }
    }


    private void removeUser(String userName) {
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String DB_USERNAME = "root";
        final String DB_PASSWORD = "0000";
        final String DELETE_USER_QUERY = "DELETE FROM users WHERE userName = ?";
        final String DELETE_RANKING_QUERY = "DELETE FROM ranking WHERE userName = ?";
        final String DELETE_WORDS_QUERY = "DELETE FROM words WHERE userName = ?";
        final String DELETE_WRONGLIST_QUERY = "DELETE FROM wronglist WHERE userName = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement deleteUserStatement = connection.prepareStatement(DELETE_USER_QUERY);
             PreparedStatement deleteRankingStatement = connection.prepareStatement(DELETE_RANKING_QUERY);
             PreparedStatement deleteWordsStatement = connection.prepareStatement(DELETE_WORDS_QUERY);
             PreparedStatement deleteWrongListStatement = connection.prepareStatement(DELETE_WRONGLIST_QUERY)) {

            deleteUserStatement.setString(1, userName);
            deleteRankingStatement.setString(1, userName);
            deleteWordsStatement.setString(1, userName);
            deleteWrongListStatement.setString(1, userName);

            connection.setAutoCommit(false); // Start a transaction
            try {
                deleteRankingStatement.executeUpdate();
                deleteWordsStatement.executeUpdate();
                deleteWrongListStatement.executeUpdate();
                int rowsAffected = deleteUserStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "User successfully removed.");
                    connection.commit(); // Commit the transaction
                } else {
                    JOptionPane.showMessageDialog(null, "Something went wrong, please try again.");
                    connection.rollback(); // Rollback the transaction
                }
            } catch (SQLException sqlException) {
                connection.rollback(); // Rollback the transaction
                sqlException.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error while removing user from the database.");
            } finally {
                connection.setAutoCommit(true); // Reset auto-commit mode
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while removing user from the database.");
        }
    }


    private void fetchUsers() {
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String DB_USERNAME = "root";
        final String DB_PASSWORD = "0000";
        final String SELECT_QUERY = "SELECT userName, password, email FROM users";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_QUERY)) {

            userListData.clear();

            while (resultSet.next()) {
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                String formattedUser = String.format("User: %-20s| Password: %-10s| Email: %-20s", userName, password, email);
                userListData.addElement(formattedUser);
            }

            usersList.setListData(userListData);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while removing user from the database.");
        }
    }


    public JPanel getUsersListPanel() {
        return usersListPanel;
    }

    public JList<String> getUsersList() {
        return usersList;
    }

    private void createUIComponents() {
        usersListPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image backgroundImage = ImageIO.read(new File("C:\\Users\\82106\\IdeaProjects\\VocabBoost\\src\\popUpScreen.jpg"));
                    Image newImage = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    g.drawImage(newImage, 0, 0, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("User List");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new usersList().getUsersListPanel());
                frame.setPreferredSize(new Dimension(1100, 600));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
