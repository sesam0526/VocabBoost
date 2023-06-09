import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class LearningMode extends GameUtils {
    private JPanel LearningModePanel;
    private JLabel englishWord;
    private JLabel answer;
    private JTextField tfMeaning;
    private String currentEnglishWord;
    private String currentMeaning;

    public LearningMode(String user) {
        super(user);
        askQuestion();

        LearningModePanel.setPreferredSize(new Dimension(800, 600));

        tfMeaning.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = tfMeaning.getText();
                if (userAnswer.trim().equals(currentMeaning.trim())) {
                    answer.setText("Correct!");
                    correctCount++;
                } else {
                    answer.setText("Wrong!");
                    wrongCount++;
                    updateWrongListTable(currentEnglishWord, currentMeaning);
                }

                tfMeaning.setText("");

                if (allWordsAndMeanings.size() == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Quiz finished! Correct answers: " + correctCount + ", Wrong answers: " + wrongCount);
                    updateRankingTable();
                    return;
                }
                askQuestion();
            }
        });
    }

    private void askQuestion() {
        if (allWordsAndMeanings.isEmpty()) {
            JOptionPane.showMessageDialog(null, "단어를 1개 이상 등록하세요.");
            return;
        }

        List<String> keys = new ArrayList<>(allWordsAndMeanings.keySet());
        Random random = new Random();

        currentEnglishWord = keys.get(random.nextInt(keys.size()));
        currentMeaning = allWordsAndMeanings.get(currentEnglishWord);
        englishWord.setText(currentEnglishWord);

        allWordsAndMeanings.remove(currentEnglishWord);
    }

    public JPanel getLearningModePanel() {
        return LearningModePanel;
    }

    public boolean isReady() {
        return allWordsAndMeanings.size() >= 1;
    }

    private void createUIComponents() {
        LearningModePanel = new JPanel() {
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
