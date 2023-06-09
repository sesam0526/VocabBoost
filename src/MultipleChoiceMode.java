import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MultipleChoiceMode extends GameUtils {
    private JButton choice1;
    private JButton choice2;
    private JButton choice3;
    private JButton choice4;
    private JPanel MultipleChoiceModePanel;
    private JLabel englishWord;
    private JLabel answer;

    private Map<String, String> unusedWordsAndMeanings;
    private String correctAnswer;

    public MultipleChoiceMode(String user) {
        super(user);

        fetchWords();

        MultipleChoiceModePanel.setPreferredSize(new Dimension(800, 600));

        if (allWordsAndMeanings.size() < 4) {
            JOptionPane.showMessageDialog(null, "단어를 4개 이상 등록하세요.");
            return;
        }

        unusedWordsAndMeanings = new HashMap<>(allWordsAndMeanings);
        askQuestion();

        ActionListener answerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton selectedButton = (JButton) e.getSource();
                String selectedAnswer = selectedButton.getText();
                if (selectedAnswer.equals(correctAnswer)) {
                    answer.setText("Correct!");
                    correctCount++;
                } else {
                    answer.setText("Wrong!");
                    wrongCount++;
                    updateWrongListTable(englishWord.getText(), correctAnswer);
                }

                if (unusedWordsAndMeanings.size() == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Quiz finished! Correct answers: " + correctCount + ", Wrong answers: " + wrongCount);
                    updateRankingTable();
                    return;
                }

                askQuestion();
            }
        };

        choice1.addActionListener(answerListener);
        choice2.addActionListener(answerListener);
        choice3.addActionListener(answerListener);
        choice4.addActionListener(answerListener);
    }

    // Other existing methods and variables

    private void askQuestion() {
        List<Map.Entry<String, String>> unusedEntries = new ArrayList<>(unusedWordsAndMeanings.entrySet());
        Random random = new Random();

        Map.Entry<String, String> questionEntry = unusedEntries.get(random.nextInt(unusedEntries.size()));
        String englishWordText = questionEntry.getKey();
        correctAnswer = questionEntry.getValue();
        unusedWordsAndMeanings.remove(englishWordText);

        englishWord.setText(englishWordText);

        JButton[] choices = new JButton[] {choice1, choice2, choice3, choice4};
        int correctChoice = random.nextInt(choices.length);

        List<Map.Entry<String, String>> allEntries = new ArrayList<>(allWordsAndMeanings.entrySet());
        Set<String> usedChoices = new HashSet<>();
        usedChoices.add(correctAnswer);

        for (int i = 0; i < choices.length; i++) {
            if (i == correctChoice) {
                choices[i].setText(correctAnswer);
            } else {
                Map.Entry<String, String> wrongAnswerEntry;
                String wrongAnswer;

                do {
                    wrongAnswerEntry = allEntries.get(random.nextInt(allEntries.size()));
                    wrongAnswer = wrongAnswerEntry.getValue();
                } while (usedChoices.contains(wrongAnswer));

                usedChoices.add(wrongAnswer);
                choices[i].setText(wrongAnswer);
            }
        }
    }

    public JPanel getMultipleChoiceModePanel() {
        return MultipleChoiceModePanel;
    }

    private void createUIComponents() {
        MultipleChoiceModePanel = new JPanel() {
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

    public boolean isReady() {
        return allWordsAndMeanings.size() >= 4;
    }
}
