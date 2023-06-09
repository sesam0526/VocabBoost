import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class wordsList {
    private JList wordList;
    private JPanel wordsListPanel;
    private JButton btnEdit;

    private JButton btnRemove;
    private String user;
    private String currentMeaning;

    public wordsList(String user) {
        this.user = user;
        fetchWords();
        wordsListPanel.setPreferredSize(new Dimension(800, 600));

        btnEdit.addActionListener(e -> {
            String selectedWord = (String) wordList.getSelectedValue();
            if (selectedWord == null) {
                JOptionPane.showMessageDialog(null, "Please select a word first.");
                return;
            }

            String[] wordAndMeaning = selectedWord.split(": ");
            String newMeaning = JOptionPane.showInputDialog(null, "Edit meaning", wordAndMeaning[1]);

            if(newMeaning != null && !newMeaning.equals(wordAndMeaning[1])){
                updateMeaning(wordAndMeaning[0], newMeaning);
                fetchWords();  // Refresh the word list
            }
        });

        btnRemove.addActionListener(e -> {
            String selectedWord = (String) wordList.getSelectedValue();
            if (selectedWord == null) {
                JOptionPane.showMessageDialog(null, "Please select a word first.");
                return;
            }

            String[] wordAndMeaning = selectedWord.split(": ");
            int dialogResult = JOptionPane.showConfirmDialog (null,
                    "Would you like to delete " + wordAndMeaning[0] + "?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION);

            if(dialogResult == JOptionPane.YES_OPTION){
                removeWord(wordAndMeaning[0]);
                fetchWords();  // Refresh the word list
            }
        });
    }

    private void updateMeaning(String englishWord, String newMeaning) {
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";
        final String UPDATE_QUERY = "UPDATE words SET meaning = ? WHERE userName = ? AND englishWord = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {

            preparedStatement.setString(1, newMeaning);
            preparedStatement.setString(2, user);
            preparedStatement.setString(3, englishWord);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Word meaning successfully updated.");
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong, please try again.");
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while updating word meaning in the database.");
        }
    }

    private void removeWord(String englishWord) {
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";
        final String DELETE_QUERY = "DELETE FROM words WHERE userName = ? AND englishWord = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {

            preparedStatement.setString(1, user);
            preparedStatement.setString(2, englishWord);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Word successfully removed.");
            } else {
                JOptionPane.showMessageDialog(null, "Something went wrong, please try again.");
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while removing word from the database.");
        }
    }


    private void fetchWords() {
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";
        final String SELECT_QUERY = "SELECT englishWord, meaning FROM words WHERE userName = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {

            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            Vector<String> wordsAndMeanings = new Vector<>();
            while (resultSet.next()) {
                wordsAndMeanings.add(resultSet.getString("englishWord") + ": " + resultSet.getString("meaning"));
            }

            wordList.setListData(wordsAndMeanings);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while fetching words from the database");
        }
    }

    public JPanel getWordsListPanel() {
        return wordsListPanel;
    }

    private void createUIComponents() {
        wordsListPanel = new JPanel() {
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
