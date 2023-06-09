
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JDialog {
    private JTextField tfUserName;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JButton btnRegisterNow;
    private JPanel loginPanel;
    private JFrame startScreenFrame;
    private JFrame menuScreenFrame;
    private JFrame administratorModeFrame;
    public User user;

    public Login(JFrame startScreenFrame, JFrame menuScreenFrame, JFrame administratorModeFrame) {
        this.startScreenFrame = startScreenFrame;
        this.menuScreenFrame = menuScreenFrame;
        this.administratorModeFrame = administratorModeFrame;

        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(350, 450));
        setModal(true);
        setLocationRelativeTo(startScreenFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        AbstractAction loginAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = tfUserName.getText();
                String password = String.valueOf(pfPassword.getPassword());

                user = getAuthenticatedUser(userName, password);

                if (user != null) {
                    if (user.userName.equals("Administrator")) {
                        openAdministratorModePanel();
                    } else {
                        openMenuScreenPanel();
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(Login.this,
                            "Invalid User Name or Password",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        tfUserName.addActionListener(loginAction);
        pfPassword.addActionListener(loginAction);

        btnLogin.addActionListener(loginAction);

        btnRegisterNow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Register register = new Register(null, Login.this);
            }
        });

        setVisible(true);
    }


    private void openMenuScreenPanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menu Screen");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuScreen menu = new menuScreen(user.userName, Login.this);
            frame.setContentPane(menu.getMenuScreenPanel());
            frame.setSize(1040, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void openAdministratorModePanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Administrator Mode");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            administratorMode admin = new administratorMode(user.userName, Login.this);
            frame.setContentPane(admin.getAdministratorPanel());
            frame.setSize(1040, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    public JFrame getStartScreenFrame() {
        return startScreenFrame;
    }

    public JFrame getMenuScreenFrame() {
        return menuScreenFrame;
    }

    public JFrame getAdministratorModeFrame() {
        return administratorModeFrame;
    }


    private User getAuthenticatedUser(String userName, String password) {
        User user = null;

        final String DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
        final String USERNAME = "root";
        final String PASSWORD = "0000";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE userName COLLATE utf8mb4_bin = ? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String dbUserName = resultSet.getString("userName");

                // Check if the username from the DB matches the input username exactly, including case
                if (dbUserName != null && dbUserName.equals(userName)) {
                    user = new User();
                    user.userName = dbUserName;
                    user.password = resultSet.getString("password");
                    user.email = resultSet.getString("email");
                }
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        JFrame startScreenFrame = new JFrame();
        JFrame menuScreenFrame = new JFrame();
        JFrame administratorModeFrame = new JFrame();

        Login login = new Login(startScreenFrame, menuScreenFrame, administratorModeFrame);
        User user = login.user;

        if (user != null) {
            System.out.println("Successful Authentication of: " + user.userName);
            System.out.println("Email: " + user.email);
        } else {
            System.out.println("Authentication canceled");
        }
    }
}