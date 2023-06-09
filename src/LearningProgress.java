import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class LearningProgress {
    private JPanel LearningProgressPanel;
    private JList<String> wrongList;
    private JLabel score;
    private JLabel percentage;
    private String user;

    public LearningProgress(String user) {
        this.user = user;
        fetchUserProgress();
        fetchWrongWords();

        LearningProgressPanel.setPreferredSize(new Dimension(800, 600));
    }

    private void fetchUserProgress() {
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";
        final String SELECT_QUERY = "SELECT score, percentage FROM ranking WHERE userName = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {

            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                score.setText(String.valueOf(resultSet.getInt("score")));
                percentage.setText(resultSet.getDouble("percentage") + "%");
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while fetching user progress from the database");
        }
    }

    private void fetchWrongWords() {
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";
        final String SELECT_QUERY = "SELECT englishWord, meaning, count FROM wronglist WHERE userName = ? ORDER BY count DESC";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {

            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            Vector<String> wrongWordsData = new Vector<>();
            while (resultSet.next()) {
                String data = resultSet.getString("englishWord") + " : " +
                        resultSet.getString("meaning") + " (틀린 횟수 : " +
                        resultSet.getInt("count") + "번)";
                wrongWordsData.add(data);
            }

            wrongList.setListData(wrongWordsData);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while fetching wrong words from the database");
        }
    }

    public JPanel getLearningProgressPanel() {
        return LearningProgressPanel;
    }
    private void createUIComponents() {
        LearningProgressPanel = new JPanel() {
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