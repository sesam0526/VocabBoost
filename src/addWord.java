import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class addWord {
    private JTextField tfEnglish;
    private JTextField tfMeaning;
    private JButton btnOK;
    private JPanel addWordPanel;
    private String user;

    public addWord(String user) {
        this.user = user;
        addWordPanel.setPreferredSize(new Dimension(800, 600));

        ActionListener addWordActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWordToDatabase();
            }
        };

        tfEnglish.addActionListener(addWordActionListener);
        tfMeaning.addActionListener(addWordActionListener);
        btnOK.addActionListener(addWordActionListener);
    }

    private void addWordToDatabase() {
        String englishWord = tfEnglish.getText();
        String meaning = tfMeaning.getText();

        if (englishWord.isEmpty() || meaning.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Both fields must be filled");
            return;
        }

        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";
        final String INSERT_QUERY = "INSERT INTO words (userName, englishWord, meaning) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {

            preparedStatement.setString(1, user);
            preparedStatement.setString(2, englishWord);
            preparedStatement.setString(3, meaning);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Word successfully added");
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong, please try again");
            }

            tfEnglish.setText("");
            tfMeaning.setText("");

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while connecting to the database");
        }
    }

    public Container getAddWordPanel() {
        return addWordPanel;
    }

    private void createUIComponents() {
        addWordPanel = new JPanel() {
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
}
