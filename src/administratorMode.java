import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class administratorMode {
    private JLabel userName;
    private JLabel logout;
    private JButton btnAddWord;
    private JButton btnWordsList;
    private JButton btnProgress;
    private JButton btnRanking;
    private JButton btnUsersList;
    private JPanel administratorModePanel;
    private Login login;

    public administratorMode(String name, Login login) {
        $$$setupUI$$$();
        userName.setText(name);
        this.login = login;

        btnUsersList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserListDialog("User List");
            }
        });

        btnAddWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserListDialog("Add Word");
            }
        });

        btnWordsList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserListDialog("Words List");
            }
        });

        btnProgress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserListDialog("Learning Progress");
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
                    JFrame administratorFrame = (JFrame) SwingUtilities.getWindowAncestor(administratorModePanel);
                    administratorFrame.dispose(); // 메뉴 화면 닫기

                    SwingUtilities.invokeLater(() -> {
                        startScreen start = new startScreen(); // 새로운 시작 화면 생성
                    });

                    JFrame administratorModeFrame = login.getAdministratorModeFrame();
                    if (administratorModeFrame != null) {
                        administratorModeFrame.dispose(); // 메뉴 화면 닫기
                    }
                }
            }
        });
    }

    private void showUserListDialog(String title) {
        JFrame userListFrame = new JFrame(title);
        userListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        usersList userListScreen = new usersList();
        JPanel userListPanel = userListScreen.getUsersListPanel();
        JList<String> usersList = userListScreen.getUsersList();

        usersList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedUser = usersList.getSelectedValue();
                    if (selectedUser != null) {
                        String userName = selectedUser.substring(selectedUser.indexOf("User: ") + 6, selectedUser.indexOf(" |")).trim();
                        if (title.equals("Add Word")) {
                            showAddWordDialog(userName);
                        } else if (title.equals("Words List")) {
                            showWordsListDialog(userName);
                        } else if (title.equals("Learning Progress")) {
                            showLearningProgressDialog(userName);
                        }
                    }
                }
            }
        });

        userListFrame.setContentPane(userListPanel);
        userListFrame.setSize(1100, 600);
        userListFrame.setLocationRelativeTo(null);
        userListFrame.setVisible(true);
    }

    private void showAddWordDialog(String userName) {
        JFrame addWordFrame = new JFrame("Add Word");
        addWordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWord addWordScreen = new addWord(userName);
        Container addWordPanel = addWordScreen.getAddWordPanel();

        addWordFrame.setContentPane(addWordPanel);
        addWordFrame.pack();
        addWordFrame.setLocationRelativeTo(null);
        addWordFrame.setVisible(true);
    }

    private void showWordsListDialog(String userName) {
        JFrame wordsListFrame = new JFrame("Words List");
        wordsListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        wordsList wordsListScreen = new wordsList(userName);
        Container wordsListPanel = wordsListScreen.getWordsListPanel();

        wordsListFrame.setContentPane(wordsListPanel);
        wordsListFrame.pack();
        wordsListFrame.setLocationRelativeTo(null);
        wordsListFrame.setVisible(true);
    }

    private void showLearningProgressDialog(String userName) {
        JFrame progressFrame = new JFrame("Learning Progress");
        progressFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        LearningProgress progressScreen = new LearningProgress(userName);
        Container progressPanel = progressScreen.getLearningProgressPanel();

        progressFrame.setContentPane(progressPanel);
        progressFrame.pack();
        progressFrame.setLocationRelativeTo(null);
        progressFrame.setVisible(true);
    }

    private void createUIComponents() {
        administratorModePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("C:\\Users\\82106\\IdeaProjects\\VocabBoost\\src\\mainMenu.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
    }

    public JPanel getAdministratorPanel() {
        return administratorModePanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Administrator Mode");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            administratorMode admin = new administratorMode("Administrator", null);
            frame.setContentPane(admin.getAdministratorPanel());
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
