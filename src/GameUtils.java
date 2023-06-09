import javax.swing.*;
import java.sql.*;
import java.util.*;

public class GameUtils {
    protected String user;
    protected Map<String, String> allWordsAndMeanings;
    protected int correctCount = 0;
    protected int wrongCount = 0;

    public GameUtils(String user) {
        this.user = user;
        fetchWords();
    }

    protected void fetchWords() {
        allWordsAndMeanings = new HashMap<>();

        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";
        final String SELECT_QUERY = "SELECT englishWord, meaning FROM words WHERE userName = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {

            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                allWordsAndMeanings.put(resultSet.getString("englishWord"), resultSet.getString("meaning"));
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while fetching words from the database");
        }
    }

    protected void updateRankingTable() {
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";
        final String SELECT_QUERY = "SELECT * FROM ranking WHERE userName = ?";
        final String UPDATE_QUERY = "UPDATE ranking SET score = score + ?, percentage = (percentage + ?) / 2 WHERE userName = ?";
        final String INSERT_QUERY = "INSERT INTO ranking (userName, score, percentage) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement selectPreparedStatement = connection.prepareStatement(SELECT_QUERY);
             PreparedStatement updatePreparedStatement = connection.prepareStatement(UPDATE_QUERY);
             PreparedStatement insertPreparedStatement = connection.prepareStatement(INSERT_QUERY)) {

            selectPreparedStatement.setString(1, user);
            ResultSet resultSet = selectPreparedStatement.executeQuery();

            if (resultSet.next()) {
                updatePreparedStatement.setInt(1, getScore());
                updatePreparedStatement.setDouble(2, getCorrectRate());
                updatePreparedStatement.setString(3, user);
                updatePreparedStatement.executeUpdate();
            } else {
                insertPreparedStatement.setString(1, user);
                insertPreparedStatement.setInt(2, getScore());
                insertPreparedStatement.setDouble(3, getCorrectRate());
                insertPreparedStatement.executeUpdate();
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while updating the ranking table");
        }
    }

    protected void updateWrongListTable(String englishWord, String meaning) {
        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";
        final String SELECT_QUERY = "SELECT * FROM wronglist WHERE userName = ? AND englishWord = ?";
        final String UPDATE_QUERY = "UPDATE wronglist SET count = count + 1 WHERE userName = ? AND englishWord = ?";
        final String INSERT_QUERY = "INSERT INTO wronglist (userName, englishWord, meaning, count) VALUES (?, ?, ?, 1)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement selectPreparedStatement = connection.prepareStatement(SELECT_QUERY);
             PreparedStatement updatePreparedStatement = connection.prepareStatement(UPDATE_QUERY);
             PreparedStatement insertPreparedStatement = connection.prepareStatement(INSERT_QUERY)) {

            selectPreparedStatement.setString(1, user);
            selectPreparedStatement.setString(2, englishWord);
            ResultSet resultSet = selectPreparedStatement.executeQuery();

            if (resultSet.next()) {
                updatePreparedStatement.setString(1, user);
                updatePreparedStatement.setString(2, englishWord);
                updatePreparedStatement.executeUpdate();
            } else {
                insertPreparedStatement.setString(1, user);
                insertPreparedStatement.setString(2, englishWord);
                insertPreparedStatement.setString(3, meaning);
                insertPreparedStatement.executeUpdate();
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while updating the wronglist table");
        }
    }

    public int getScore() {
        return correctCount * 10;
    }

    public double getCorrectRate() {
        return 100.0 * correctCount / (correctCount + wrongCount);
    }
}
