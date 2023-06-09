import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class menuScreen {
    private JPanel menuScreenPanel;
    private JLabel userName;
    private JButton btnAddWord;
    private JButton btnMultipleChoiceMode;
    private JButton btnFlashcardsMode;
    private JButton btnWordsList;
    private JButton btnLearningMode;
    private JButton btnProgress;
    private JButton btnRanking;
    private JLabel logout;
    private Login login;

    public menuScreen(String name, Login login) {
        $$$setupUI$$$();
        userName.setText(name);
        this.login = login;
        btnAddWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWord wordAdder = new addWord(name);
                JFrame addWordFrame = new JFrame("Add Word");
                addWordFrame.setContentPane(wordAdder.getAddWordPanel());
                addWordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addWordFrame.pack();
                addWordFrame.setLocationRelativeTo(menuScreenPanel); // Set frame to the center of menuScreenPanel
                addWordFrame.setVisible(true);
            }
        });

        btnWordsList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wordsList wordViewer = new wordsList(name);
                JFrame wordsListFrame = new JFrame("Words List");
                wordsListFrame.setContentPane(wordViewer.getWordsListPanel());
                wordsListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                wordsListFrame.pack();
                wordsListFrame.setLocationRelativeTo(menuScreenPanel); // Set frame to the center of menuScreenPanel
                wordsListFrame.setVisible(true);
            }
        });

        btnFlashcardsMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LearningMode learningMode = new LearningMode(name);
                if (learningMode.isReady()) {
                    FlashcardsMode flashcardsMode = new FlashcardsMode(name);
                    JFrame flashcardsModeFrame = new JFrame("Flashcards Mode");
                    flashcardsModeFrame.setContentPane(flashcardsMode.getFlashcardsModePanel());
                    flashcardsModeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    flashcardsModeFrame.pack();
                    flashcardsModeFrame.setLocationRelativeTo(menuScreenPanel); // Set frame to the center of menuScreenPanel
                    flashcardsModeFrame.setVisible(true);
                }
            }
        });

        btnMultipleChoiceMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MultipleChoiceMode multipleChoiceMode = new MultipleChoiceMode(name);
                if (multipleChoiceMode.isReady()) {
                    JFrame multipleChoiceModeFrame = new JFrame("Multiple Choice Mode");
                    multipleChoiceModeFrame.setContentPane(multipleChoiceMode.getMultipleChoiceModePanel());
                    multipleChoiceModeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    multipleChoiceModeFrame.pack();
                    multipleChoiceModeFrame.setLocationRelativeTo(menuScreenPanel); // Set frame to the center of menuScreenPanel
                    multipleChoiceModeFrame.setVisible(true);
                }
            }
        });

        btnLearningMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LearningMode learningMode = new LearningMode(name);
                if (learningMode.isReady()) {
                    JFrame learningModeFrame = new JFrame("Learning Mode");
                    learningModeFrame.setContentPane(learningMode.getLearningModePanel());
                    learningModeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    learningModeFrame.pack();
                    learningModeFrame.setLocationRelativeTo(menuScreenPanel); // Set frame to the center of menuScreenPanel
                    learningModeFrame.setVisible(true);
                }
            }
        });
        btnProgress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LearningProgress learningProgress = new LearningProgress(name);
                JFrame learningProgressFrame = new JFrame("Learning Progress");
                learningProgressFrame.setContentPane(learningProgress.getLearningProgressPanel());
                learningProgressFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                learningProgressFrame.pack();
                learningProgressFrame.setLocationRelativeTo(menuScreenPanel); // Set frame to the center of menuScreenPanel
                learningProgressFrame.setVisible(true);
            }
        });

        btnRanking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rankingSystem rankingScreen = new rankingSystem();
                JPanel rankingPanel = (JPanel) rankingScreen.getRankingSystemPanel();
                JFrame rankingFrame = new JFrame("Ranking System");
                rankingFrame.setContentPane(rankingPanel);
                rankingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                rankingFrame.pack();
                rankingFrame.setLocationRelativeTo(null); // Set frame to the center of the screen
                rankingFrame.setVisible(true);
            }
        });

        logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    JFrame menuFrame = (JFrame) SwingUtilities.getWindowAncestor(menuScreenPanel);
                    menuFrame.dispose(); // 메뉴 화면 닫기

                    SwingUtilities.invokeLater(() -> {
                        startScreen start = new startScreen(); // 새로운 시작 화면 생성
                    });

                    JFrame menuScreenFrame = login.getMenuScreenFrame();
                    if (menuScreenFrame != null) {
                        menuScreenFrame.dispose(); // 메뉴 화면 닫기
                    }
                }
            }
        });


    }

    private void createUIComponents() {
        menuScreenPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("C:\\Users\\82106\\IdeaProjects\\VocabBoost\\src\\mainMenu.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
    }

    public JPanel getMenuScreenPanel() {
        return menuScreenPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menu Screen");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuScreen menu = new menuScreen("Test User", null);
            frame.setContentPane(menu.getMenuScreenPanel());
            frame.setSize(1040, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        // Layout settings and additional component initialization
        // The actual code is auto-generated by IntelliJ, please do not manually modify.
    }
}
