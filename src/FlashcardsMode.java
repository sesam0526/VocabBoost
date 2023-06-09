import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class FlashcardsMode {
    private JPanel FlashcardsModePanel;
    private JButton btnMeaning;
    private JButton btnPrev;
    private JButton btnNext;
    private JLabel englishWord;
    private String user;

    private List<Map.Entry<String, String>> wordsAndMeanings;
    private int currentIndex = -1;

    public FlashcardsMode(String user) {
        this.user = user;
        fetchWords();

        FlashcardsModePanel.setPreferredSize(new Dimension(800, 600));

        if (wordsAndMeanings.isEmpty()) {
            JOptionPane.showMessageDialog(null, "단어를 1개 이상 등록하세요.");
            return;
        }

        showNextWord();
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextWord();
            }
        });

        btnPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPreviousWord();
            }
        });

        btnMeaning.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex >= 0 && currentIndex < wordsAndMeanings.size()) {
                    btnMeaning.setText(wordsAndMeanings.get(currentIndex).getValue());
                }
            }
        });
    }

    private void fetchWords() {
        wordsAndMeanings = new ArrayList<>();

        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";
        final String SELECT_QUERY = "SELECT englishWord, meaning FROM words WHERE userName = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {

            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                wordsAndMeanings.add(new AbstractMap.SimpleEntry<>(resultSet.getString("englishWord"), resultSet.getString("meaning")));
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while fetching words from the database");
        }
    }

    private void showNextWord() {
        if (currentIndex < wordsAndMeanings.size() - 1) {
            currentIndex++;
            englishWord.setText(wordsAndMeanings.get(currentIndex).getKey());
            btnMeaning.setText("Meaning");
        } else {
            JOptionPane.showMessageDialog(null, "You've learned all words!");
        }
    }

    private void showPreviousWord() {
        if (currentIndex > 0) {
            currentIndex--;
            englishWord.setText(wordsAndMeanings.get(currentIndex).getKey());
            btnMeaning.setText("Meaning");
        }
    }

    public JPanel getFlashcardsModePanel() {
        return FlashcardsModePanel;
    }

    public boolean isReady() {
        return wordsAndMeanings.size() >= 1;
    }

    private void createUIComponents() {
        FlashcardsModePanel = new JPanel() {
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